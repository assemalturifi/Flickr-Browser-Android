package com.example.assemalturifi.flickrbrowser;
//step62
//till now we have list of photo objects and a user interface to display them on,now we need to tie these two things together
//we will be sending all these data to recycler Adapter and the recyclerView can the use them
//we will use RecyclerView to display the data that we parsed out

//the adapter will take data from photo objects and packages the data to ViewHolder object which it sends to the recyclerView
//whenever it requires more data.

//Now the recyclerView  will send back views as they scrolled off the screen for
//the recycler adapter to reuse which saves creating hundreds of views.
//because we are encapsulated in a viewholder this also saves us having to call
//find view by ID all the time to get the actual widgets in the view
//just like with the list view, when we run the program that only a
//few view holders are actually created, so the diagram is supposed to represent a
// few view holders being created by the recycler adapter that a sort of circular
// exchange at the bottom of the diagram is old views are sent to the adapter for
//recycling.

//our recycler adapters are going to do a bit more than that because the photo
//objects only contain the URL for the thumbnails not the actual images
//themselves
//so it'll be the job of the adapter to fetch the thumbnails as the list is
//scrolled up and down that's what the flickr cloud at the top left is supposed
//to represent the thumbnails being downloaded from flickr as they're
//actually needed



import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.List;
                                                    //step64 import the interface
                                                    // extending Adapter of type<FlickerImageViewHolder>(the inner class of this class)
class FlickrRecyclerViewAdapter extends RecyclerView.Adapter<FlickrRecyclerViewAdapter.FlickrImageViewHolder> {
    private static final String TAG = "FlickrRecyclerViewAdapt";
    private List<Photo> photoList;
    private Context context;
    //context for, rather than trying to find it everytime we will store it in a field that as well,
    // and we will be getting that from the constructor. Context isnt always needed in an adapter but we're going to be using
    //an external library to take care of downloading those thumbnails for us from flickr and that needs context

    //step65
    public FlickrRecyclerViewAdapter(Context context,List<Photo> photoList) {
        this.context = context;
        this.photoList = photoList;

    }



    //step66
    //this method will inflate a view from the browse.xml layout we created and then return the view
    @Override
    public FlickrImageViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        //called by the layout manager when it needs a new view
        Log.d(TAG, "onCreateViewHolder: new view requested");
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.browse, viewGroup, false);
        //"false" here tells the inflater whether to attach the view to its root or not we dont want want
        //to do that, the recyler view is gonna do that

        return new FlickrImageViewHolder(view);
    }

    //step70, this method is called by recyclerView when it wants new data to be stored in a viewHolder
    // so that it can display it. As item scrolled off the screen the recyclerView will provide a
    // recycledViewHolder object and tell us the position of the data object that it needs to display.
    // in this method, we will get that item from the list and out its values into the viewHolder widgets.
    // we can just store the photes title in the textView and the thumbnail image in the image View
    // and looking at  FlickrImageViewHolder class it's got a textView called title and an imageView called thumbnail,
    // the problem here now we're not actually storing the actual photo, we are storing the current URL shown where to
    // get that photo from. we can create an async task to download the image, but there is a better way
    //there an open source library called Picasso that does exactly what we want. this library
    // maintains a cache(zuladaki mallar) of the photoes we downloaded so they're not downloaded it
    // again. that can really improve performance as well as saving the device;s battery because it's
    // not using the WIFI or data connection as much.Next step in gradle downloading some depencincy
    @Override
    public void onBindViewHolder(@NonNull FlickrImageViewHolder flickrImageViewHolder, int position) {
//        //step72
//        //called by the layout manager when it wants new data in an existing row
//        Photo photo = photoList.get(position);//retrieveed the current photo obj from the list
//        Log.d(TAG, "onBindViewHolder: "+photo.getmTitle()+" -->"+position);
//        Picasso.with(context).load(photo.getmImage()).error(R.drawable.placeholder).placeholder(R.drawable.placeholder).into(flickrImageViewHolder.thumbNail);
//
//        flickrImageViewHolder.title.setText(photo.getmTitle());


        //step131
        //now we gotta notify the user if there is no photo matched their search query

        if((photoList==null)||(photoList.size()==0)){
            flickrImageViewHolder.thumbNail.setImageResource(R.drawable.placeholder);
            flickrImageViewHolder.title.setText("No photoes match your search.\n\nUser the search icon to search for photos.");
        }
        else{
            Photo photo = photoList.get(position);//retrieveed the current photo obj from the list
            Log.d(TAG, "onBindViewHolder: "+photo.getmTitle()+" -->"+position);
            Picasso.with(context).load(photo.getmImage()).error(R.drawable.placeholder).placeholder(R.drawable.placeholder).into(flickrImageViewHolder.thumbNail);

            flickrImageViewHolder.title.setText(photo.getmTitle());

        }

    }
    //step67
    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: called");
        //(if photoList is not null and 0) return its size if it is null or 0 return 0
        return ((photoList!=null)&&(photoList.size()!=0)?photoList.size():1); //step132 we made 1 instead of 0
    }
    //step68,
    // this when the query changes and new data is downloaded we need to be able to provide
    // the adapter with the new list
    void loadNewData(List<Photo>newPhotos){
        photoList = newPhotos;
        notifyDataSetChanged();//this method tells the recylerView that the data has changed
        // so it cango ahead and refresh the display

    }
    //step69, we will get the full picture when the user taps one of the thumbnails in the list, so main
    // activity will need to get the photo record for the item that was tapped
    public Photo getPhoto(int position){
        return ((photoList!=null)&&(photoList.size()!=0)?photoList.get(position):null);


    }

    //step63, inner class
    static class FlickrImageViewHolder extends RecyclerView.ViewHolder{
        private static final String TAG = "FlickrImageViewHolder";
        //63b
        ImageView thumbNail=null;
        TextView title=null;

        public FlickrImageViewHolder(@NonNull View itemView) {
            //63c
            super(itemView);
            Log.d(TAG, "FlickrImageViewHolder: starts");
            this.thumbNail = itemView.findViewById(R.id.thumbnail);
            this.title = itemView.findViewById(R.id.title);
        }
    }




}
