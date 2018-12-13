package com.example.android.popular_movies_adrianadodge.utils;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.example.android.popular_movies_adrianadodge.model.Movie;
import com.example.android.popular_movies_adrianadodge.model.Review;
import com.example.android.popular_movies_adrianadodge.model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.ArrayList;
import java.util.Scanner;


/**
 * These utilities will be used to communicate with the network. *
 *
 */

public final class NetworkUtils {

    final static String PARAM_PAGE = "1";
    final static String PARAM_LANG = "language";
    final static String PARAM_KEY = "api_key";

    /**
     * Builds the URL used to query themoviedb.org. this will allow me to send parameters to the URL and build it on the fly
     * like when they toggle the sort order of the movies by: most popular, highest rated.
     *
     * @param movieDBSortBy the sort order we will need popular
     * @param movieDBapiKEY the KEY of the developer or company
     * @return The URL to use to query the THEMOVIEDB.
     * https://api.themoviedb.org/3/movie/popular?api_key=KEY&language=en-US&page=1
     */
    public static URL buildUrl(String movieDBSortBy, String movieDBapiKEY) {
        Uri.Builder builder = new Uri.Builder();
        if(movieDBSortBy.equals("popular")){
            builder.scheme("https")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("movie")
                    .appendPath("popular")
                    .appendQueryParameter(PARAM_KEY, movieDBapiKEY)
                    .appendQueryParameter(PARAM_LANG, "en")
                    .appendQueryParameter(PARAM_PAGE, "1");
        }else{
            builder.scheme("https")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("movie")
                    .appendPath("top_rated")
                    .appendQueryParameter(PARAM_KEY, movieDBapiKEY)
                    .appendQueryParameter(PARAM_LANG, "en")
                    .appendQueryParameter(PARAM_PAGE, "1");

        }


        URL url = null;
        try {
            url = new URL(builder.build().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }


    /**
     * Stage 2 Builds the URL with id in it used to query themoviedb.org. this will allow me to send parameters to the URL and build it on the fly
     * to get videos or reviews
     *
     * @param movieDBapiKEY the KEY of the developer or company
     * @param id the primary Key of the movie we need
     * @return The URL to use to query the THEMOVIEDB.
     * http://api.themoviedb.org/3/movie/338952/videos?api_key=KEY&language=en-US&page=1
     */
    public static URL buildUrlById(String filter, String movieDBapiKEY, String id) {
        Uri.Builder builder = new Uri.Builder();
        if(filter.equals("videos")){
            //videos-trailers
            builder.scheme("https")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("movie")
                    .appendPath(id)
                    .appendPath("videos")
                    .appendQueryParameter(PARAM_KEY, movieDBapiKEY)
                    .appendQueryParameter(PARAM_LANG, "en")
                    .appendQueryParameter(PARAM_PAGE, "1");
        }else{
            //reviews
            builder.scheme("https")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("movie")
                    .appendPath(id)
                    .appendPath("reviews")
                    .appendQueryParameter(PARAM_KEY, movieDBapiKEY)
                    .appendQueryParameter(PARAM_LANG, "en")
                    .appendQueryParameter(PARAM_PAGE, "1");

        }


        URL url = null;
        try {
            url = new URL(builder.build().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }



    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }







    /**
     * This method parse the JSON response string and changes it into an ArrayList of movies
     * so it can be read easily and get each item
     * @param responseFromHttpUrl
     * @return ArrayList<Movie>
     *
     */
    public static ArrayList<Movie> extractFeatureFromJson(String responseFromHttpUrl) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(responseFromHttpUrl)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Movie> movies = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // Create a JSONArray from the JSON response string
            JSONObject jObj = new JSONObject(responseFromHttpUrl);
            JSONArray baseJsonResponse = jObj.getJSONArray("results");
            // For each movie in the baseJsonResponse, create a {@link Movie} object
            for (int i = 0; i < baseJsonResponse.length(); i++) {

                JSONObject currentMovie= baseJsonResponse.getJSONObject(i);
                //get movie id
                String movieIdString = currentMovie.getString("id");

                // get the name of the movie
                String movieName = currentMovie.getString("title");
                //get synopsis
                String movieOverview = currentMovie.getString("overview");
                //get image path
                String movieImgPath = currentMovie.getString("poster_path");
                //get backdrop image path
                String movieBackdropPath = currentMovie.getString("backdrop_path");
                //get vote
                String movieVoteAverageString = currentMovie.getString("vote_average");
                Float movieVoteAverage = Float.parseFloat(movieVoteAverageString);
                //get release date
                String movieReleaseDate = currentMovie.getString("release_date");
                // Create a new {@link Movie} object from the JSON response.
                Movie movie = new Movie(movieIdString, movieName, movieOverview, movieImgPath, movieBackdropPath, movieVoteAverage,movieReleaseDate);
                // Add the new {@link Movie} to the list of movies.
                movies.add(movie);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the JSON results", e);
        }

        // Return the list of movies
        return movies;
    }



    /**
     * This method parse the JSON response string and changes it into an ArrayList of movies
     * so it can be read easily and get each item
     * @param responseFromHttpUrl
     * @return ArrayList<Movie>
     *
     */
    public static ArrayList<Trailer> extractFeatureFromJsonTrailer(String responseFromHttpUrl) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(responseFromHttpUrl)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Trailer> trailers = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // Create a JSONArray from the JSON response string
            JSONObject jObj = new JSONObject(responseFromHttpUrl);
            JSONArray baseJsonResponse = jObj.getJSONArray("results");
            // For each movie in the baseJsonResponse, create a {@link Movie} object
            for (int i = 0; i < baseJsonResponse.length(); i++) {

                JSONObject currentTrailer= baseJsonResponse.getJSONObject(i);
                //get trailer id
                String trailerIdString = currentTrailer.getString("id");

                // get the name of the trailer
                String trailerName = currentTrailer.getString("name");
                //get website where trailer is hosted
                String trailerSite = currentTrailer.getString("site");
                //get trailer YouTubeKey
                String trailerYouTubeKey = currentTrailer.getString("key");
                //get trailerSize
                String trailerSize = currentTrailer.getString("size");
                //get type:Trailer or Teaser
                String trailerType = currentTrailer.getString("type");
                if(trailerType.equals("Trailer")){
                    // Create a new {@link Movie} object from the JSON response.
                    Trailer trailer =  new Trailer(trailerIdString, trailerName, trailerSite, trailerYouTubeKey,trailerSize, trailerType);
                    // Add the new {@link Movie} to the list of movies.
                    trailers.add(trailer);
                }

            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the JSON results on trailer feed", e);
        }

        // Return the list of movies
        return trailers;
    }


    /**
     * This method parse the JSON response string and changes it into an ArrayList of movies
     * so it can be read easily and get each item
     * @param responseFromHttpUrl
     * @return ArrayList<Movie>
     *
     */
    public static ArrayList<Review> extractFeatureFromJsonReview(String responseFromHttpUrl) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(responseFromHttpUrl)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Review> reviews = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // Create a JSONArray from the JSON response string
            JSONObject jObj = new JSONObject(responseFromHttpUrl);
            JSONArray baseJsonResponse = jObj.getJSONArray("results");
            // For each movie in the baseJsonResponse, create a {@link Movie} object
            for (int i = 0; i < baseJsonResponse.length(); i++) {

                JSONObject currentReview= baseJsonResponse.getJSONObject(i);
                //get trailer id
                String reviewIdString = currentReview.getString("id");

                // get the author of review
                String reviewAuthor = currentReview.getString("author");
                //get review conetnt
                String reviewContent = currentReview.getString("content");
                //get reviewURL
                String reviewURL = currentReview.getString("url");


                // Create a new {@link Movie} object from the JSON response.
                Review review =  new Review(reviewIdString, reviewAuthor, reviewContent,reviewURL);
                // Add the new {@link Movie} to the list of movies.
                reviews.add(review);


            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the JSON results on Review feed", e);
        }

        // Return the list of movies
        return reviews;
    }



}
