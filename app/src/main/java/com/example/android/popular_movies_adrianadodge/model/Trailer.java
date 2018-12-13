package com.example.android.popular_movies_adrianadodge.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Trailer implements Parcelable {
    public static final String SITE_YOUTUBE = "YouTube";

    private String mId;
    private String mName;
    private String mSite;
    private String mYouTubeKEY;
    private String mSize;
    private String mType;

    public Trailer() {

    }
    // Constructor
    public Trailer(String id, String name, String site, String youtubeKEY, String size, String type){
        mId = id;
        mName = name;
        mSite = site;
        mYouTubeKEY= youtubeKEY;
        mSize= size;
        mType= type;
    }


    protected Trailer(Parcel in) {
        mId = in.readString();
        mName = in.readString();
        mSite = in.readString();
        mYouTubeKEY = in.readString();
        mSize = in.readString();
        mType = in.readString();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    public  String getTrailerUrl(Trailer trailer) {
        return "https://www.youtube.com/watch?v="+trailer.getYOUTUBE_KEY();
    }

        public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getSite() {
        return mSite;
    }

    public void setSite(String site) {
        this.mSite = site;
    }

    public String getYOUTUBE_KEY() {
        return mYouTubeKEY;
    }

    public void setYOUTUBE_KEY(String youtubeKEY) {
        this.mYouTubeKEY = youtubeKEY;
    }



    public String getSize() {
        return mSize;
    }

    public void setSize(String size) {
        this.mSize = size;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        this.mType = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mId);
        parcel.writeString(mName);
        parcel.writeString(mSite);
        parcel.writeString(mYouTubeKEY);
        parcel.writeString(mSize);
        parcel.writeString(mType);
    }
}
