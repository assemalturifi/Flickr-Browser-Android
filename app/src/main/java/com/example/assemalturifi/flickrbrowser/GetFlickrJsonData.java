package com.example.assemalturifi.flickrbrowser;

//step23
//this class is responsible for calling our async downloadTask. when it gets the data
// back and it will parse out the information we want and create a photo object for each photo thats actually downloaded
//getData will send a string back to This class. and This class will send a list of photoes back to MainActivity,
// and MainActivity will send that list to the adapter so it can feed the photoes to the RecyclerView.
//The recyclerView will receive ViewHolders from the FlicrRecyclerViewAdapter


import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

//step24
//this class is gonna get the callbacks from GetData so it has to implement the onDataDownloadComplete interface
//public class GetFlickrJsonData implements GetData.OnDownloadComplete {

    //step45
    //what would happen if there a load of data and a json parsing takes a long time?it is possible that the internet
    // is slow and also it is possible that the parsing itself could also take a while,so the solution is to make
    // this class(parsing) getFlickrJsonData class also async task so that it does the parsing on a background thread as well.
//the first parameter is string containing the query you want to use to actually filter the flickr feed, it is the same parameter
// that we're currently parsing to execute on the same thread.
//second parameter is void because we dont want to show any sort of progress bars
//third parameter is List because we wll be returning a list of photoes
public class GetFlickrJsonData extends AsyncTask<String,Void,List<Photo>> implements GetData.OnDownloadComplete {

    //step24
    private static final String TAG = "GetFlickrJsonData";
    private List<Photo> photoList=null;
    //this is the base URL before I add the parameters to end things like the search tags in the
    // format sort of the raw link thats going to get the data from Json
    private String baseURL;
    //also i m gonna allow the language to be specified, in the flickr feed there is a parameter for different languages
    private String mLanguage;
    //this one is to choose between matching all the search terms or any of them going to store a flag for that paramater
    private boolean mMatchAll;

    //step25
    //this class calls getData which does run asynchronously on a backGround thread, so
    // that means that anything using this getFlickrJsonData class wont get any data back immediately
    //so im gonna use the same callback mechanism as I did for getData
    private final OnDataAvailable mCallBack;


    //step52
    //we've got an issue with the mCallBack.onDataAvailable being called from two places from onDownloadComplete and onPostExecute()
        //so we need to invoke the callback method in both places but depending on whether we're running as an async task or not
        //that why we created this field
    private boolean runningOnSameThread=false;

    //step26
    interface  OnDataAvailable{
        void onDataAvailable(List<Photo> data, DownloadStatus status);
    }

    //step28
    //consructor
    public GetFlickrJsonData(String baseURL, String mLanguage, boolean mMatchAll, OnDataAvailable mCallBack) {
        Log.d(TAG, "GetFlickrJsonData called.");
        this.baseURL = baseURL;
        this.mLanguage = mLanguage;
        this.mMatchAll = mMatchAll;
        this.mCallBack = mCallBack;
    }

    //step29
    //main activity will call this
    void executeOnSameThread(String searchCriteria){
        Log.d(TAG, "executeOnSameThread starts");
        //step53
        runningOnSameThread=true;

        //step29
        String destinationURI = createURI(searchCriteria, mLanguage, mMatchAll);

        GetData getData = new GetData(this);
        getData.execute(destinationURI);
        Log.d(TAG, "executeOnSameThread ends");
    }

        //step50
        // /this class is already running as background object when doInBackground()is invoked
        //so in doInBackground() it uses getdata to downLoad the data, once GetData is finished downloading,
        // it will call out onDownloadComplete() which will parse the jSon and build up a list of photoes
        //once all the parsing is finished doInBackground finishes and returns list of photoes
        //the async task will then call onPostExecute because we're running getFlickrJsonData as an async task
        @Override
        protected void onPostExecute(List<Photo> photos) {
            Log.d(TAG, "onPostExecute starts");
            if(mCallBack!=null){
                mCallBack.onDataAvailable(photoList,DownloadStatus.OK);
            }
            Log.d(TAG, "onPostExecute ends");
            //step51
            //there is actually a problem here that mCallback that onDataAvailable has already been called from
            //onDownloadComplete so if we just go back and have a look at on download complete notice that we
            // calling mCallback.onDataAvailable there as well.
            //we really don't want to call it twice here so you might be wondering why would
            //we bother with onPostExecute if onDownloadComplete is already returning
            //the data but the callback in on download complete won't work when
            //running this asynchronously everything that happens in an async task
            //is happening in a background thread and that thread can't communicate
            //directly with the main UI thread that's the complexity that using an
            //async task takes care of for us the only way to communicate with the calling
            //process is by the on post execute method when that's called its run on the main thread
            //so that it can communicate with objects on the main thread. but will we run asynchronously
            //though onDownloadComplete will be executed on the background thread so if
            //it calls that callback that will also be run on the background thread. at the
            //moment we wouldn't notice that because the method in main activity that we're
            //calling back doesn't attempt to access any UI elements all its doing at the
            //moment is logging the fact that it's been called once we've created the user
            //interface though the onDataAvailable method in main activity will be sent out
            //our data to the recyclerview widget so that it can display the photos so any visible
            //objects unavailable on the background thread so what would happen is we get an
            //exception thrown if we tried to do that. so we don't want to invoke the callback
            //in on download complete
            //when we're running asynchronously. bottom line here is we need to invoke the
            //callback method in both places but depending on whether we're running as an
            //async task or not.

        }

        //step46
        @Override
        protected List<Photo> doInBackground(String... params) {
            Log.d(TAG, "doInBackground starts");
            String destinationURI = createURI(params[0],mLanguage,mMatchAll);

            // "this" using this class parsing it as an object so we can get the callback
            GetData data = new GetData(this);
            //step47
            //this code will already be running on a background thread so the system wont let us create another one
            //here we're creating GetData object and executing on a separate thread, but android studio wont lets us do that because
            //it knows that we're are already running this code on a background thread, so it wont create another one.
            //so we will create another method so we will be treating it as just another class rather than as an async task
//            data.execute(destinationURI);
//            Log.d(TAG, "doInBackground ends");
//            return photoList;
            //step49
            data.runningInSameThread(destinationURI);
            Log.d(TAG, "doInBackground ends");
            return photoList;
        }

        //step30
    private String createURI(String searchCriteria,String language,boolean matchAll){
        //step31
        Log.d(TAG, "createURI starts");

//        //33
//        //here i am building the parameters on top of the URL, so iam creating this Uri.Builder object and get a builder using the buildUpon()
//        Uri uri = Uri.parse(baseURL);
//        Uri.Builder builder = uri.buildUpon();
//        //so here basically im using the queries appendQueryParameter to add each parameter to the uri.
//        //the appendQueryParameter method takes care of separating the parameters with either a question mark or an ampersand(&)
//        //as appropriate and make sure that each step results in a valid uri, so each time i am appending another parameter and getting
//        //It's appending the various parameters to the URI, to get the complete URL
//        //he code basically forms the final URL string for downloading.
//        builder = builder.appendQueryParameter("tags", searchCriteria);
//        builder = builder.appendQueryParameter("tagmode", matchAll ? "ALL" : "ANY");//if matchAll is equal to true return text ALL otherwise  "ANY"
//        builder = builder.appendQueryParameter("lang", language);
//        builder = builder.appendQueryParameter("format", "json");
//        builder = builder.appendQueryParameter("nojsoncallback", "1");
//        uri = builder.build();



        //step32
        //step33 above code is as same as this one but this one is short
        return Uri.parse(baseURL).buildUpon().appendQueryParameter("tags", searchCriteria)
                .appendQueryParameter("tagmode", matchAll ? "ALL" : "ANY").appendQueryParameter("lang", language)
                .appendQueryParameter("format", "json").appendQueryParameter("nojsoncallback", "1").build().toString();

    }

    //step27
    //this class implements onDownloadComplete so it can get Callback from getData and it als defines
    // its own interface so it can send a call back to mainActivity when we have data to send back to it
    //step34
    @Override
    public void onDownloadComplete(String data, DownloadStatus status) {
        Log.d(TAG, "onDownloadComplete starts. Status "+status);
        //if some data is downloaded
        if(status==DownloadStatus.OK){
            photoList = new ArrayList<>();

            try{
                //step35
                JSONObject jsonData = new JSONObject(data);//parsing the data
                JSONArray itemsArray = jsonData.getJSONArray("items");//getting the items, check the URL all the things(link, author)is under items

                //step36
                for(int i=0;i<itemsArray.length();i++){
                    //processing the individual data
                    JSONObject jsonPhoto=itemsArray.getJSONObject(i);
                    String title = jsonPhoto.getString("title");
                    String author = jsonPhoto.getString("author");
                    String authorId = jsonPhoto.getString("author_id");
                    String tags = jsonPhoto.getString("tags");

                    //step37
                    //for the photo;the photo(m) is smaller
                    //the photoURL will become the image field of the Photo object
                    //i will be using photoURL field to display the image for each photo in the list
                    //if you check the Json data the link is under media then m comes, thats why it needs anothe rprocess
                    JSONObject jsonMedia = jsonPhoto.getJSONObject("media");
                    String photoURL = jsonMedia.getString("m");

                    //step38
                    //for the link(this is again a photo but larger)
                    //the photoURL will become the image field of the Photo object
                    //i will be using photoURL field to display the image for each photo in the list
                    //when the image(photoURL) is tapped, what is gonna happen is that we're gonna launch another activity to display the
                    //photo(link) in much larger so it fills the screen, for that w will use the link value
                    //in this code below we are replacing m with b, b is bigger version of the picture
                    //it turned out, flickr photoes are stored in a number of different sizes
                    String link = photoURL.replaceFirst("_m.", "_b.");

                    Photo photoObject = new Photo(title, author, authorId, link, tags, photoURL);
                    photoList.add(photoObject);
                    Log.d(TAG, "onDownloadComplete "+photoObject.toString());

                }

            }
            //step39
             catch (JSONException e) {
                e.printStackTrace();
                 Log.e(TAG, "onDownloadComplete: Error processing Json data "+ e.getMessage());
                 status = DownloadStatus.FAILED_OR_EMPTY;
            }
        }
//        //step40
//        //now i need to notify the calling class that everything is done and send it the list of photoes that i have created
//        if(mCallBack!=null){
//            //now inform the caller that processing is done-possibly returning null if there is an erorr
//            mCallBack.onDataAvailable(photoList,status);
//        }
//        Log.d(TAG, "onDownloadComplete ends");

        //step54
        //now we are testing the value for and only invoke the callback interface if we're running on the same thread as the calling process
        if(runningOnSameThread&&mCallBack!=null){
            mCallBack.onDataAvailable(photoList,status);
        }
        Log.d(TAG, "onDownloadComplete ends");

    }
}
