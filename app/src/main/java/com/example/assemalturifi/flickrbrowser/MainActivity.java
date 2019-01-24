package com.example.assemalturifi.flickrbrowser;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

//api, JSON
//in the file there is a diagram

                                                                //step21 implements GetData.OnDownloadComplete
                                                                //step41, mainActivity creates a GetFlickrJsonData object
// and calls its executes on same thread method. Now GetFlickrJsonData class creates GetData object and tells it to execute
// on a separate thread when that finishes it calls out onDownloadComplete() and the that in turn calls the onDataAvailable
// method of main activity,so we need to make mainactivity implement teh onDataAvailable interface n write that callback method
                                                                //step41
//public class MainActivity extends AppCompatActivity implements GetFlickrJsonData.OnDataAvailable {

                                                                                                //step81
//step97 extends baseActivity
public class MainActivity extends BaseActivity implements GetFlickrJsonData.OnDataAvailable,
        RecyclerItemClickListener.OnRecyclerClickListener {
    private static final String TAG = "MainActivity";
    //step73
    private FlickrRecyclerViewAdapter mFlickerRecyclerViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //all the next 5 lines are default
        Log.d(TAG, "onCreate: starts");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //step98
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        //step99
        //this time we passed "false" because this is the mainActivity, and no other activities are calling this activity
        activateToolbar(false);
        //this time we passed "false" because this is the main activity and no other activities are calling this one
        //and because we dont want the home button



        //step74
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        //reyclerViewis actually doesnt take care of the layouts. that's done by a layout manager
        //recyerView is simpler than ListView because it doesnt do much beyond recycling views and
        // everything else left to other objects. So the data in the view to display are provided by recyclerAdapter,
        // the actual layout is performed by a layout manager and the view themselves live in a viewHolder. ListView
        // doesnt have this mechanism, thats why recylerView becomes far more flexible and performs a lot better.
        // now we dont ahve to write our own layout manager. We will just have to create a new linear layout manager
        // object and tell the recyclerView to use that(setLatyoutManager)
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //step84
        //now we will create an instance of the recyclerItemClickListener class and add it as a touch listener to the recyclerView
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,recyclerView,this));
        //now run the app and interact with the UI. Whenever an image is clicked

        //step74
        mFlickerRecyclerViewAdapter=new FlickrRecyclerViewAdapter(this,new ArrayList<Photo>());
        recyclerView.setAdapter(mFlickerRecyclerViewAdapter);
        //At this point what we gotta to do is to put out data in the adapter. The place to do that, when we
        // receive a new data which happens onDataAvailable()


        //step44
//        //step16
//        //we are executing the asynTask
//        //"this" is the current object
//        GetData downloadData = new GetData(this);
//        downloadData.execute("https://api.flickr.com/services/feeds/photos_public.gne?tags=android,nougat,sdk&tagmode=any&format=json&noJsoncallback=1");


        Log.d(TAG, "onCreate: ends");

    }


    //step43
    @Override
    //instead of creating getFlickrJsonData object in onCreate, im gonna put it in onResume instead.because later we are
    // gonna launch another activity to search fr differnet photoes and when we return from that the onResume()will be called.
    //when an activity calls another activity onPause method is being called. when the new activity finishes and we return to
    // mainActivity its onResume() will be called.So i will create GetFlickrJsonData in onResume() and delete the gerData code from onCreate().
    protected void onResume() {
//        Log.d(TAG, "onResume starts");
//        super.onResume();
//        GetFlickrJsonData data = new GetFlickrJsonData("https://api.flickr.com/services/feeds/photos_public.gne", "en-us", true,this);
//        data.executeOnSameThread("android, nougat");
//        Log.d(TAG, "onResume ends");

        //step55
        Log.d(TAG, "onResume starts");
        super.onResume();

        //step129
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String queryResult = preferences.getString(FLICKR_QUERY, "");

        //step130
        if(queryResult.length()>0){
            GetFlickrJsonData data = new GetFlickrJsonData("https://api.flickr.com/services/feeds/photos_public.gne", "en-us", true,this);
//        data.executeOnSameThread("android, nougat");
            //we will treat this as an async task now
            data.execute(queryResult);

        }
//        GetFlickrJsonData data = new GetFlickrJsonData("https://api.flickr.com/services/feeds/photos_public.gne", "en-us", true,this);
////        data.executeOnSameThread("android, nougat");
//        //we will treat this as an async task now
//        data.execute("android,nougat");

        Log.d(TAG, "onResume ends");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //all default
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        Log.d(TAG, "onCreateOptionsMenu() returned: " + true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //all default
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        //step120
        if(id==R.id.action_search){
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
            return true;
        }

        Log.d(TAG, "onOptionsItemSelected() returned.");
        return super.onOptionsItemSelected(item);
    }

//step42(commenting out this below code)
//    //step17
//    @Override
//    public void onDownloadComplete(String data, DownloadStatus status) {
//
//        if(status==DownloadStatus.OK){
//            Log.d(TAG, "onDownloadComplete: data is "+data);
//        }
//        else{
//            //download or processing failed
//
//            Log.d(TAG, "onDownloadComplete failed with status "+status);
//        }
//        }


           //step42
           //chaged the above code to this
//    @Override
//    public void onDataAvailable(List<Photo> data, DownloadStatus status) {
//
//
//        if(status==DownloadStatus.OK){
//            Log.d(TAG, "onDataAvailable: data is "+data);
//        }
//        else{
//            //download or processing failed
//
//            Log.d(TAG, "onDataAvailable failed with status "+status);
//        }
//        }

        //step75
        @Override
        public void onDataAvailable(List<Photo> data, DownloadStatus status) {
            Log.d(TAG, "onDataAvailable: starts");

            if(status==DownloadStatus.OK){
                mFlickerRecyclerViewAdapter.loadNewData(data);
                Log.d(TAG, "onDataAvailable: data is "+data);
            }
            else{
                //download or processing failed

                Log.d(TAG, "onDataAvailable failed with status "+status);
            }
            Log.d(TAG, "onDataAvailable: ends");
            //step76
            //run the app
            //till now in the app, you can scroll down and see all the images with title.
            //now time to display the flickrBrowser and if you want to display the full photoes full
            // screen rather than just the thumbnails in the list, we're gonna need some way to detect that an
            // item in the recyclerView was tapped and which item was tapped
            //there are number of method for detecting when an item in the list is tapped and a common method is to implement the
            // recyclerView.onItemTouchListener interface in a class.
            //we will do something similar, will extend RecyclerView.SimpleOnItemTouchListener
        }
        //step100
       // it is time to make our onItemClick() and onItemLongClick() to do something useful
        //
    //step82
    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "onItemClick: starts");
        Toast.makeText(MainActivity.this, "Normal tap at position " + position, Toast.LENGTH_SHORT).show();

    }
    //step83
    @Override
    public void onItemLogClick(View view, int position) {
        Log.d(TAG, "onItemLogClick: starts");
        //step101
//        Toast.makeText(MainActivity.this, "Long tap at position " + position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, PhotoDetailActivity.class);
        //here we are telling the photoDatailActivity which photo it should display. it is storing data
        intent.putExtra(PHOTO_TRANSFER, mFlickerRecyclerViewAdapter.getPhoto(position));
        //here it gives error because you gott make the object serializable
        startActivity(intent);


    }
}
//i used flickr Json data downloading to the app using AsyncTask
//made http request and got Json data and parsed it RecyclerView and RecyclerViewAdaper
//used callback functions
//used picasso library to download images
//used RecyclerView.OnItemTouchListener interface and its methods to detect some clicks.
//implementted GestureDetectorCompat interface
//used GestureDetectorCompat class to detect clicks and implemented its methods .
//we transferred data between actyivities using putExtra
//used searchManager to allow the user to search for an image


