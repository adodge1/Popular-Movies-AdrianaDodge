package com.example.android.popular_movies_adrianadodge.model;
import android.os.Parcel;
import android.os.Parcelable;


public class Movie implements Parcelable{
    private String mId;
    private String mTitle;
    private String mOverview;
    private String mImagePath;
    private String mBackdropPath;
    private Float mVoteAverage;
    private String mReleaseDate;

    /**
     * No args constructor for use in serialization
     */
    public Movie() {
    }
    // Constructor
    public Movie(String id, String title, String overview, String imagePath, String backdropPath, Float voteAverage, String releaseDate){
        mId = id;
        mTitle = title;
        mOverview = overview;
        mImagePath= imagePath;
        mBackdropPath = backdropPath;
        mVoteAverage= voteAverage;
        mReleaseDate= releaseDate;
    }


    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getOverview() {
        return mOverview;
    }

    public void setOverview(String overview) {
        mOverview = overview;
    }

    public String getImagePath() {
        return mImagePath;
    }

    public void setImagePath(String imagePath) {
        mImagePath = imagePath;
    }

    public String getBackdropPath() {
        return mBackdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        mBackdropPath = backdropPath;
    }

    public Float getVoteAverage() {
        return mVoteAverage;
    }

    public void setVoteAverage(Float voteAverage) {
        mVoteAverage = voteAverage;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String realeaseDate) {
        mReleaseDate = realeaseDate;
    }



    // Parcelling part
    public Movie(Parcel parcel_in){
        mId = parcel_in.readString();
        mTitle = parcel_in.readString();
        mOverview =  parcel_in.readString();
        mImagePath = parcel_in.readString();
        mBackdropPath = parcel_in.readString();
        mVoteAverage = parcel_in.readFloat();
        mReleaseDate = parcel_in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mTitle);
        dest.writeString(mOverview);
        dest.writeString(mImagePath);
        dest.writeString(mBackdropPath);
        dest.writeFloat(mVoteAverage);
        dest.writeString(mReleaseDate);
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id='" + mId + '\'' +
                ", Title='" + mTitle + '\'' +
                ", overview='" + mOverview + '\'' +
                ", image_path='" + mImagePath + '\'' +
                ", backDrop_path='" + mBackdropPath + '\'' +
                ", vote_Average='" + mVoteAverage + '\'' +
                ", release_date='" + mReleaseDate + '\'' +
                '}';
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

}
