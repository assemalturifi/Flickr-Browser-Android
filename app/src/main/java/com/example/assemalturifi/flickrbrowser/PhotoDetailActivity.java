package com.example.assemalturifi.flickrbrowser;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;
//step60 created this activity
//this activity, this layout for displaying the full photoes when we select one from the list


                //step94
public class PhotoDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        //step95
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
        //step96
        activateToolbar(true);

        //step110
        //so we will get the photoDetailActivity to actually display the photo because obviously we just seeing that placeholder
// image at the moment. First we will retrieve the details from the intent
        Intent intent = getIntent();
        Photo photo = (Photo) intent.getSerializableExtra(PHOTO_TRANSFER);
        if (photo != null) {
            //step111
            //we will get a reference to each of the widgets in the layout and set their values to display the fields of
            // the photo object
            TextView photoTitle = (TextView) findViewById(R.id.photo_title);
            //step116
//            photoTitle.setText("Title: " + photo.getmTitle());
            Resources resources = getResources();
            String text=resources.getString(R.string.photo_title_text,photo.getmTitle());
            photoTitle.setText(text);

            TextView photoTag = findViewById(R.id.photo_tags);
            //step117
//            photoTag.setText("Tags: " + photo.getmTags());
            photoTag.setText(resources.getString(R.string.photo_tags_text,photo.getmTags()));


            TextView photoAuthor = findViewById(R.id.photo_author);
            photoAuthor.setText(photo.getmAuthor());

            //step112
            //will get the image from the layout
            ImageView photoImage = findViewById(R.id.photo_image);
            //step113
            //copied this from flickerRecyclerViewAdapter class, and changed some of the methods
            Picasso.with(this).load(photo.getmLink()).error(R.drawable.placeholder).placeholder(R.drawable.placeholder).into(photoImage);


        }
    }

}
//step114
// material design
// which google introduced with android lollipop
// the concept of a material design is that its interactive and it's
//designed so that we have a set of principles or guidelines from google. and
//it relates to how the applications that we create look but also how our users
//the people playing or downloading and using our apps interact with those apps.
//material design is really the stuff for one of a better term that the buttons
//fields and other things are really constructed with.
//so why would we bother using the material design principles?
//well firstly it's very cool and makes your app look and become much more engaging
//obviously if you're using material design your apps are going to be more
//blended in and fit with the general look and feel of android apps
// one of the good things that Google did with material design was to make it
//backwards compatible so what that means is that we can have our apps running on
//older devices still look much like the current android nougat so we can retain
//the colors the styles and so forth because google gave us the ability to do
//that.
//step115
//we are gonna add search feature

