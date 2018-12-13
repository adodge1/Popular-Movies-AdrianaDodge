package com.example.android.popular_movies_adrianadodge.model;

import android.os.Parcel;
import android.os.Parcelable;


public class Review implements Parcelable
{
    private String mId;
    private String mAuthor;
    private String mContent;
    private String mURL;

    public Review()
    {

    }

    public Review(String id, String author, String content, String url)
    {
        this.mId=id;
        this.mAuthor = author;
        this.mContent = content;
        this.mURL = url;


    }

    protected Review(Parcel in)
    {
        mId = in.readString();
        mAuthor = in.readString();
        mContent = in.readString();
        mURL = in.readString();
    }

    public static final Creator<Review> CREATOR = new Creator<Review>()
    {
        @Override
        public Review createFromParcel(Parcel in)
        {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size)
        {
            return new Review[size];
        }
    };

    public String getId()
    {
        return mId;
    }

    public void setId(String id)
    {
        this.mId = id;
    }

    public String getAuthor()
    {
        return mAuthor;
    }

    public void setAuthor(String author)
    {
        this.mAuthor = author;
    }

    public String getContent()
    {
        return mContent;
    }

    public void setContent(String content)
    {
        this.mContent = content;
    }

    public String getUrl()
    {
        return mURL;
    }

    public void setUrl(String url)
    {
        this.mURL = url;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeString(this.mId);
        parcel.writeString(this.mAuthor);
        parcel.writeString(this.mContent);
        parcel.writeString(this.mURL);
    }

    @Override
    public String toString() {
        return "Review:" + mContent +"Author-"+ mAuthor;
    }



}