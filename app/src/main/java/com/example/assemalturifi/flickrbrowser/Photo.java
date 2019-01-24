package com.example.assemalturifi.flickrbrowser;

import java.io.Serializable;

//step22
//this class will hold all the data relating to a single photo
//these fields are from the json
//step102
class Photo implements Serializable {
    //step103
    //Java code can run on many different platforms using different versions and types of the Java Virtual Machine.
    //Google have written their own dalvik on earlier version of Android and art on later versions. if you rely on
    // teh default iplmenetation of serializable then the data may not be recoverable usng a different vertual
    // machine or version of Java. but here there wont be problem with this app. But its always a good idea to do
    // things properly, now you may for exampple decide to provide a bookmark feature to allow users to store
    // certain photo details in a favorities file. and if Google subsequently update the version of the java or the
    // jvm, your app may still run but the favorites may not be serialized correctly. so considering how easy it
    // is to fix that issue there's no real reason ot to do. all we have to do is to add a serial version UID
    // to the class.
    private static final long serialVersionUID=1L;
    //step104
    //the version numbers user by the serialization code to check that the data its retrieving is the same
    // version as the data that was stored. If you dont create your own serial version UID then java generates
    // one for you and different versions of java may generate different UID's which is why you can get into
    // problems by not actually defining your own UID
    //step105
    //until now when you press long press you can see that the photo is opening in another screen and just one photo

    //step22
    private String mTitle;
    private String mAuthor;
    private String mAuthorId;
    private String mLink;
    private String mTags;
    private String mImage;


    public Photo(String mTitle, String mAuthor, String mAuthorId, String mLink, String mTags, String mImage) {
        this.mTitle = mTitle;
        this.mAuthor = mAuthor;
        this.mAuthorId = mAuthorId;
        this.mLink = mLink;
        this.mTags = mTags;
        this.mImage = mImage;
    }

    String getmTitle() {
        return mTitle;
    }

    String getmAuthor() {
        return mAuthor;
    }

    String getmAuthorId() {
        return mAuthorId;
    }

    String getmLink() {
        return mLink;
    }

    String getmTags() {
        return mTags;
    }

    String getmImage() {
        return mImage;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "mTitle='" + mTitle + '\'' +
                ", mAuthor='" + mAuthor + '\'' +
                ", mAuthorId='" + mAuthorId + '\'' +
                ", mLink='" + mLink + '\'' +
                ", mTags='" + mTags + '\'' +
                ", mImage='" + mImage + '\'' +
                '}';
    }
}
