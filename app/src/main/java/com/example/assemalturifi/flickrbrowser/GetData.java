package com.example.assemalturifi.flickrbrowser;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


//step2
//an enum is a way of having multiple objects for something so if you think of
// an int, a field that we declare to be an int can have an infinite number of
// values but if we've got a few values that a field can have and that's where an enum can come in and be useful.
//this enum is for to hold a list of download statuses
//we want a way to tell calling processes whether the download succeeded or whether the state of the download is
enum DownloadStatus{IDLE,PROCESSING, NOT_INITIALIZED,FAILED_OR_EMPTY, OK}
//idle means is not actually processing anything at this point in time
//processing means its downloading the data
//not_initialized means that we havent got a valid URL to download, it is an error condition
//failed or empty means that we either failed to download anything or the data came back empty
//ok means we got some valid data and the download was successful.


//step 1
public class GetData extends AsyncTask<String,Void,String > {
    private static final String TAG = "GetData";

    //step3
    private DownloadStatus mDownloadStatus;
    //step19
//    private final MainActivity mCallBack;
    private final OnDownloadComplete mCallBack;

    //step18
    //an interface
    interface OnDownloadComplete{
        void onDownloadComplete(String data, DownloadStatus status);
    }

    //step4
//    public GetData(MainActivity callback) {
//        this.mDownloadStatus = DownloadStatus.IDLE;
//        mCallBack = callback;
//    }

    //step20   //step29 MainActivity to OnDownloadComplete
    public GetData(OnDownloadComplete callback) {
        this.mDownloadStatus = DownloadStatus.IDLE;
        mCallBack = callback;
    }



    //step5
    //the three... below is called variable-length argument(ellipsis).
    // it allows you to provide several values for the parameter or the
    // argument you provide must be of the same type, so here all of them
    // must be string,(what i mean you can provide multiple URL's and run them
    // in the background but this would not be a good idea, it is better onCreate to
    // provide different instances for eact URL)
    // we could pass in several URL's and have them downloaded
    //it like an array
    //in doInbackround all the code will run in the background
    // asynchronously in a background thread
    @Override
    protected String doInBackground(String... strings) {
        //step7
        HttpURLConnection connection=null;
        BufferedReader reader=null;
        //to read data from the inputStream
        StringBuilder result = new StringBuilder();

        //step8
        //we're checking to see whether we've been given a URL when the method's called, we dont know if it's valid URL
        if(strings ==null){
            mDownloadStatus = DownloadStatus.NOT_INITIALIZED;
            return null;
        }
        //step10
        try{
            mDownloadStatus=DownloadStatus.PROCESSING;
            //string[0]is the first URL
            URL url=new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");// by default the http request is already set to GET, but it is good if you specify it
            connection.connect();

            int response = connection.getResponseCode();
            Log.d(TAG, "doInBackground: The response code was "+response);

            InputStream inputStream = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);
            //we could have written the 3line of code above like;
            //BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            //step11
//            String line;
//
//            while (null != (line = reader.readLine())) {
//                result.append(line).append("\n");
//            }

            //step14
            // you can write step 11 like this
            for(String line=reader.readLine();line!=null;line=reader.readLine()){
                result.append(line).append("\n");
            }
            mDownloadStatus = DownloadStatus.OK;
            return result.toString();

        }
        //step9
        //if the URL is Valid
        catch (MalformedURLException e){
            Log.e(TAG, "doInBackground: Invalid URL "+e.getMessage() );
        }
        catch (IOException e){
            Log.e(TAG, "doInBackground: IO Exception reading data: " + e.getMessage());
        }
        catch (SecurityException e){
            Log.e(TAG, "doInBackground: Security Exception. Needs permission? "+e.getMessage() );
        }
        //step12
        //finally block is executed whether an exception is thrown or not
        finally {
            if (connection != null) {
                connection.disconnect();
            }
            if(reader!=null){
                try {
                    reader.close();
                }
                catch (IOException e){
                    Log.e(TAG, "doInBackground: Error closing stream "+e.getMessage() );
                }
            }

        }
        //step13
        mDownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
       return null;
    }

    //step48
    //here its doing what would have happened if we called the async task
    // execute method. When you call the execute method of an async task it creates a new thread and
    // runs the doInBackGround() when that completes the onPostExecute() is called on the mainThread. so all we have to do is the
    // same thing only without creating a new background thread so we call doing background and pass the return value from that
    // to onPostExecute. now if you're struggling to undertsand that just pretend that getData didnt extend asyncTask
    // if it didnt then the doInBackGround and postExecute() would just be ordinary methods that we can call just like any other method
    // and thats essentially what we're doing here with running the same thread method. GetData does extend async task however it can be used as
    // either an async task object or it can also be just used as regular getData object. when we call the execute method we are treating
    // it as an async task because the execuute method exists in the async tasks superclass to run doinbackGround on a separate thread
    // but when we run the runInSameThread() we are treating getData as a getData object, there;s noo superclass involved and no background
    // thread created so we've now got a class that can be used to download data in the background if we wanted to but they can also be called
    // to do exactly the same job only on a existing thread if we need to do that.
    void runningInSameThread(String s){
        Log.d(TAG, "runningInSameThread starts");
        onPostExecute(doInBackground(s));
        Log.d(TAG, "runningInSameThread ends");

    }

    //step6
    @Override
    protected void onPostExecute(String s) {
        //step15
        Log.d(TAG, "onPostExecute: ");
        if(mCallBack!=null){

            mCallBack.onDownloadComplete(s,mDownloadStatus);
        }
        Log.d(TAG, "onPostExecute: ends");
        //now when the download finishes this method is called,
        // it is gonna call the main activity onDownloadComplete() and provide it with the data and teh status results.

    }



}


