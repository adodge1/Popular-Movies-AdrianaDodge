package com.example.android.popular_movies_adrianadodge.database;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;

import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;


@Entity(tableName = "favorite_table",primaryKeys={"movie_id"})
public class FavoriteEntry implements Parcelable {


    @NonNull
    @ColumnInfo(name = "movie_id")
    private String movieId;
    @ColumnInfo(name = "movie_title")
    private String movieTitle;
    @ColumnInfo(name = "movie_overview")
    private String movieOverview;
    @ColumnInfo(name = "movie_image")
    private String movieImage;
    @ColumnInfo(name = "movie_backdrop_path")
    private String movieBackdropPath;
    @ColumnInfo(name = "movie_vote_average")
    private String movieVoteAverage;
    @ColumnInfo(name = "movie_release_date")
    private String movieReleaseDate;




    public FavoriteEntry(){}

    public FavoriteEntry( String movieId, String title,String overview, String imagePath, String backgroundImg, String voteAverage, String releaseDate) {
        this.movieId = movieId;
        this.movieTitle = title;
        this.movieOverview = overview;
        this.movieImage = imagePath;
        this.movieBackdropPath= backgroundImg;
        this.movieVoteAverage = voteAverage;
        this.movieReleaseDate =releaseDate;
    }

    public String getMovieId() {
        return this.movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getMovieTitle() {
        return this.movieTitle;
    }

    public void setMovieTitle(String title) {
        this.movieTitle = title;
    }

    public String getMovieOverview() {
        return this.movieOverview;
    }

    public void setMovieOverview(String overview) {
        this.movieOverview = overview;
    }

    public String getMovieImage() {
        return movieImage;
    }

    public void setMovieImage(String imagePath) {
        movieImage = imagePath;
    }

    public String getMovieBackdropPath() {
        return movieBackdropPath;
    }

    public void setMovieBackdropPath(String imageBackdropPath) {
        movieBackdropPath = imageBackdropPath;
    }

    public String getMovieVoteAverage() {
        return movieVoteAverage;
    }

    public void setMovieVoteAverage(String voteAverage) {
        movieVoteAverage = voteAverage;
    }

    public String getMovieReleaseDate() {
        return movieReleaseDate;
    }

    public void setMovieReleaseDate(String releaseDate) {
        movieReleaseDate = releaseDate;
    }




    // Parcelling part
    @Ignore
    public FavoriteEntry(Parcel parcel_in){
        movieId = parcel_in.readString();
        movieTitle = parcel_in.readString();
        movieOverview=parcel_in.readString();
        movieImage =  parcel_in.readString();
        movieBackdropPath=  parcel_in.readString();
        movieVoteAverage =  parcel_in.readString();
        movieReleaseDate =  parcel_in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movieId);
        dest.writeString(movieTitle);
        dest.writeString(movieOverview);
        dest.writeString(movieImage);
        dest.writeString(movieBackdropPath);
        dest.writeString(movieVoteAverage);
        dest.writeString(movieReleaseDate);
    }


    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public FavoriteEntry createFromParcel(Parcel in) {
            return new FavoriteEntry(in);
        }

        public FavoriteEntry[] newArray(int size) {
            return new FavoriteEntry[size];
        }
    };


}
