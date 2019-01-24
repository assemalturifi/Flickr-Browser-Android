package com.example.assemalturifi.flickrbrowser;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

//step77
//in this class
// we're gonna need some way to detect that an item in the recyclerView was tapped and which item was tapped
//there are number of method for detecting when an item in the list is tapped and a common method is to implement the
// recyclerView.onItemTouchListener interface in a class. we will do something similar,
// will extend RecyclerView.SimpleOnItemTouchListener.

public class RecyclerItemClickListener extends RecyclerView.SimpleOnItemTouchListener {
    private static final String TAG = "RecyclerItemClickListen";

    //all the approaches to implement a recyclerViewListener to use a callback mechanism which is the usual way of
    // notifying an activity that something's been clicked, so we need to define an interface that we can use to callback on
    //im gonna call ours onRecylerClickistener

    interface OnRecyclerClickListener{
        void onItemClick(View view,int position);

        void onItemLogClick(View view, int position);

    }

    //step78
    private final OnRecyclerClickListener mListener;
    private final GestureDetectorCompat mGestureDetector;
    //so far we've responded to simple taps on widgets such as buttons or listView but
    // android actually provides a lot of ways that a user can interact with apps including
    // things like long tapping swiping and drawing patterns and these are all handled by GestureDetectorCompat


    //step79
    //constructor
    public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, final OnRecyclerClickListener mListener) {
        //we need Context for the gestureDetector to work and we also need a reference to the recylerView that we're detecting
        // the taps on and that's why they're actually added parameters in the constructor
        this.mListener = mListener;

        //with onInterceptTouchEvent() you can see what the user did, taped or touched or clicked or clicked two times
        //with the overwritetn methods you can do whatever you want with these two clicked or tapped,
        // there are other methods you can override
        mGestureDetector=new GestureDetectorCompat(context,new GestureDetector.SimpleOnGestureListener(){


            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                Log.d(TAG, "onSingleTapUp: starts");
                //step85
                View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if(childView!=null&&mListener!=null){
                    Log.d(TAG, "onSingleTapUp: calling listener.onItemClick");
                    mListener.onItemClick(childView,recyclerView.getChildAdapterPosition(childView));
                }
                return true;
            }

            //step86
            //this is for looong click
            @Override
            public void onLongPress(MotionEvent e) {
                Log.d(TAG, "onLongPress: starts");

                View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if(childView!=null&&mListener!=null){
                    Log.d(TAG, "onLongPress: calling listener.onItemLongClick");
                    mListener.onItemLogClick(childView,recyclerView.getChildAdapterPosition(childView));

                    //after all these we need to get the three activities working together to display the full photos and
                    //allow the tags to be changed to search for different photos.
                    //so in the process will review how to launch one activity from another using intents
                    //so lets get mainActivity to launch the photodetailsActivity when a photo tapped in the list
                }
            }

        });

    }

    //step80
    //this method will be called whenever any sort of touch happens whether it's a tap, double tap, a swipe or whatever
    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        Log.d(TAG, "onInterceptTouchEvent: starts");
        if(mGestureDetector!=null){
            boolean result = mGestureDetector.onTouchEvent(e);
            Log.d(TAG, "onInterceptTouchEvent: returned: "+result);
            return result;
        }
        else{
            Log.d(TAG, "onInterceptTouchEvent(): returned: false ");
            return false;
        }


    }
}
