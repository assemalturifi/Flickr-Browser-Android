package com.example.assemalturifi.flickrbrowser;
//step87
//this class BaseActivity is that our activities(other activities) will all extend. The reason for doing that is
//to share methods and properties amongst a number of different activities three in our case.
//by defining common methods in a single class and then having the other classes extend it, they can access
//to everything that's defined in the base class. what that means is we don't have to include the same methods in every activity.


import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

//step88
//this class extends AppCompatActivity and we're doing this because our other classes
//such as searchActivity they are already extending BaseActivity. so that the other activities will
//essentially get that functionality also going to define a couple of string
//constants that will use when transferring data between activities
public class BaseActivity extends AppCompatActivity {
    //these constants are for transferring data between activities when we want to display a photo
    // in the photoDetailActivity we need to send the details of the photo to the new activity thats launched
    private static final String TAG = "BaseActivity";
    static final String FLICKR_QUERY = "FLICKR_QUERY";
    static final String PHOTO_TRANSFER = "PHOTO_TRANSFER";

    //step89
    //This method will be used to show the toolbar
    // and it will allow an activity to choose whether the toolbar should have the home button showing or
    // not. Now main activity won't need a home button because it's the home screen but the other activities will.
    // when we launch photo details activity from main activity for example the home button will
    //allow us to go back to main activity.
    void activateToolbar(boolean enableHome){
        Log.d(TAG, "activateToolbar: starts");
        ActionBar actionBar = getSupportActionBar();

        //step106
        //until now when you press long press you can see that the photo is opening in another screen and just one photo
        //there is a problem here tho, the home button is not showing for some reason
        //it was actionbar!=null. here basically we made it ==null which means if we havent got an action bar
        //we want to assign out toolbar
        if(actionBar==null){
            //step107
            //now we got the home button but its not actually working, the home button is not working because we didnt
            // use the hierarchical parent option when we created the activity from the wizard
            Toolbar toolbar = findViewById(R.id.toolbar);

            if(toolbar!=null){
                setSupportActionBar(toolbar);
                actionBar = getSupportActionBar();
            }
        }
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(enableHome);
        }
    }
    //step90
    //other activities in this app will extend this class instead of AppCompatActivity so they can use activate toolbar method

}

