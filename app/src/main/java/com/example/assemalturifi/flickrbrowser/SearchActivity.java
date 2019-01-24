package com.example.assemalturifi.flickrbrowser;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.SearchView;
// step58
//created this activity and in the layout two xml created itself
//content_main.xml is deleted

                //step91
public class SearchActivity extends BaseActivity {
                    private static final String TAG = "SearchActivity";

    //step122
    private SearchView mSaearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //step92

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //step93
        //

        // the searchActivity it is an activity that was called by another activity
        activateToolbar(true);
        Log.d(TAG, "onCreate: ends");
    }

    //step123
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: starts");
        getMenuInflater() .inflate(R.menu.meanu_search,menu);

        //step126
        //so the search manager provides access to the system search services and a way to get a
        // search manager instance is to call get system service with a search_service constant to
        // identify which service we want.
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSaearchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
        mSaearchView.setSearchableInfo(searchableInfo);
//        Log.d(TAG, "onCreateOptionsMenu: " + getComponentName().toString());
//        Log.d(TAG, "onCreateOptionsMenu: hint is " + mSaearchView.getQueryHint());
//        Log.d(TAG, "onCreateOptionsMenu: searchable info is "+searchableInfo.toString());

        mSaearchView.setIconified(false);//no extra step when you click on the search box

        //step127
        mSaearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTextSubmit: called");
                //step128
                //now what we need to do is connect two things together so that our search query whatever has been typed in
                // by the user is used as a sear h query and we can see the results coming back from flickr. So all we
                // have to do now before search activity finishes is tio store the query text somewhere that main
                // activvity can retrieve it from. we will use shared preferences
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());//we got an object
                // of SharedPrefs object by calling the preferene managers.getDefault..(). Here we passed getApplicationContext()
                // rather than using "this" because the data is going to be retrieved by different activity to the one that saved.
                //  so search activity will store the data and main activity will retrieve it. Thats why we are using
                // the getApplicationContext method there
                prefs.edit().putString(FLICKR_QUERY, query).apply();


                mSaearchView.clearFocus();//for clearing after you press enter, when you enter it will go to the previous page
                finish();
                return true;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        //step131
        //when the user instead of typing something, the user clicks on the "x" instead of typing and when the user clicks
        // the "x" we want it to go to the images page.
        mSaearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                finish();
                return false;

            }
        });

        Log.d(TAG, "onCreateOptionsMenu: returned "+true);
        return true;
        }
}
