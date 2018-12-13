package com.example.android.popular_movies_adrianadodge;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popular_movies_adrianadodge.database.FavoriteEntry;
import com.example.android.popular_movies_adrianadodge.model.Movie;
import com.example.android.popular_movies_adrianadodge.model.Review;
import com.example.android.popular_movies_adrianadodge.model.Trailer;
import com.example.android.popular_movies_adrianadodge.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements TrailerRecyclerAdapter.TrailerRecyclerAdapterOnClickHandler {

    public static final String MOVIE_KEY = "YOUR_API_KEY_HERE";

    private TrailerRecyclerAdapter mTrailerAdapter;
    private RecyclerView mTrailerRecyclerView;
    private ReviewRecyclerAdapter mReviewAdapter;
    private RecyclerView mReviewRecyclerView;
    private ImageView mIvToggle;
    private MoviesViewModel mMovieViewModel;



    /**
     * This overriding onCreate method this is called once the new activity gets loaded
     * we have a parcel in the intent saved as extra so we get it and this is our object movie
     * that was selected initially
     * we call populateDetailActivity so we load the movie details
     * @param savedInstanceState
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        final Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }
        try{
            Movie movie  = intent.getParcelableExtra("Movie.Details");
            if (movie == null) {

                FavoriteEntry FavMovie = intent.getParcelableExtra("MovieFaves.Details");

                 if (FavMovie == null) {
                    closeOnError();
                    return;
                }else{

                     Float favMovieFloatAvg = Float.parseFloat(FavMovie.getMovieVoteAverage());
                     Movie movieFavorite = new Movie(FavMovie.getMovieId(),FavMovie.getMovieTitle(),FavMovie.getMovieOverview(),FavMovie.getMovieImage(),FavMovie.getMovieBackdropPath(),favMovieFloatAvg,FavMovie.getMovieReleaseDate());

                     populateDetailActivity(movieFavorite);
                }
            }else{
                populateDetailActivity(movie);
            }



        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //Stage 2
    private void makeDBMovieQuerySpecificMovieId(String filter,String typeRequest, String id) {
        //Build URL
        URL moviesUrlId = NetworkUtils.buildUrlById(filter,MOVIE_KEY,id);
        //Do HTTPS request on background Thread
        if(typeRequest.equals("trailers")){
            new DetailActivity.HTTPrequestBackgroundThreadForTrailers().execute(moviesUrlId);
        }else{
            new DetailActivity.HTTPrequestBackgroundThreadForReviews().execute(moviesUrlId);
        }

    }

    //Stage 2
    public class HTTPrequestBackgroundThreadForTrailers extends AsyncTask<URL, Void, ArrayList<Trailer>> {

        @Override
        protected ArrayList<Trailer> doInBackground(URL... params) {
            URL searchUrl = params[0];

            String responseFromHttpUrl = null;
            ArrayList<Trailer> trailers =null;

            ConnectivityManager ConnectionManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo=ConnectionManager.getActiveNetworkInfo();
            if(networkInfo != null && networkInfo.isConnected()==true )
            {
                //Toast.makeText(MainActivity.this, "Network Available", Toast.LENGTH_LONG).show();
                try {
                    responseFromHttpUrl = NetworkUtils.getResponseFromHttpUrl(searchUrl);
                    trailers = NetworkUtils.extractFeatureFromJsonTrailer(responseFromHttpUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(DetailActivity.this, "Network Not Available", Toast.LENGTH_LONG).show();

            }

            //return responseFromHttpUrl;
            return trailers;

        }

        @Override
        protected void onPostExecute(ArrayList<Trailer> results) {

            mTrailerAdapter.setTrailerData(results);

        }

    }

    @Override
    public void onClick(Trailer selectedTrailer) {
        String YouTubeVideoURL = selectedTrailer.getTrailerUrl(selectedTrailer);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YouTubeVideoURL));
        startActivity(browserIntent);
        //Toast.makeText(DetailActivity.this, "Item Clicked", Toast.LENGTH_LONG).show();
    }



    //Stage 2
    public class HTTPrequestBackgroundThreadForReviews extends AsyncTask<URL, Void, ArrayList<Review>> {

        @Override
        protected ArrayList<Review> doInBackground(URL... params) {
            URL searchUrl = params[0];

            String responseFromHttpUrl = null;
            ArrayList<Review> reviews =null;

            ConnectivityManager ConnectionManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo=ConnectionManager.getActiveNetworkInfo();
            if(networkInfo != null && networkInfo.isConnected()==true )
            {
                //Toast.makeText(MainActivity.this, "Network Available", Toast.LENGTH_LONG).show();
                try {
                    responseFromHttpUrl = NetworkUtils.getResponseFromHttpUrl(searchUrl);
                    reviews = NetworkUtils.extractFeatureFromJsonReview(responseFromHttpUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(DetailActivity.this, "Network Not Available", Toast.LENGTH_LONG).show();

            }

            //return responseFromHttpUrl;
            return reviews;

        }

        @Override
        protected void onPostExecute(ArrayList<Review> results) {

            mReviewAdapter.setReviewData(results);

        }

    }




    /**
     * This method has the movie object as a parameter and with the METHODS of the Movie class we get
     * it's qualities
     * @param movie
     *
     */

    private void populateDetailActivity(Movie movie) {

        TextView movieReleaseDisplay = findViewById(R.id.release_tv);
        movieReleaseDisplay.setText("Released Date: " + movie.getReleaseDate());

        setTitle(movie.getTitle());

        ImageView moviePosterIv = findViewById(R.id.poster_iv);

        String imageBaseUrl = "http://image.tmdb.org/t/p/w780";
        Picasso.with(this)
                .load(imageBaseUrl + movie.getBackdropPath())
                .into(moviePosterIv);

        TextView movieOverviewDisplay = findViewById(R.id.overview_tv);
        movieOverviewDisplay.setText(movie.getOverview());

        TextView voteAverageDisplayText = findViewById(R.id.voting_tv);
        voteAverageDisplayText.setText("Vote Average: " +movie.getVoteAverage().toString());

        RatingBar voteAverageDisplay = findViewById(R.id.rating_detail);
        voteAverageDisplay.setRating(movie.getVoteAverage()/2);

        //STAGE 2
        String movieId = movie.getId();

        //Trailers
        mTrailerRecyclerView = findViewById(R.id.rv_trailers);
        // use a grid layout manager
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mTrailerRecyclerView.setLayoutManager(layoutManager);
        // use this setting to improve performance
        mTrailerRecyclerView.setHasFixedSize(true);
        // specify an adapter
        mTrailerAdapter = new TrailerRecyclerAdapter(this);
        //set Adapter to recyclerView
        mTrailerRecyclerView.setAdapter(mTrailerAdapter);
        makeDBMovieQuerySpecificMovieId("videos","trailers", movieId);

        //Reviews
        mReviewRecyclerView = findViewById(R.id.rv_reviews);
        // use a grid layout manager
        LinearLayoutManager layoutManagerReviews
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mReviewRecyclerView.setLayoutManager(layoutManagerReviews);
        // use this setting to improve performance
        mReviewRecyclerView.setHasFixedSize(true);
        // specify an adapter
        mReviewAdapter = new ReviewRecyclerAdapter();
        //set Adapter to recyclerView
        mReviewRecyclerView.setAdapter(mReviewAdapter);
        makeDBMovieQuerySpecificMovieId("reviews","reviews",movieId);


        final String favMovId = movie.getId();
        final String favMovTitle = movie.getTitle();
        final String favMovOverview = movie.getOverview();
        final String favMovImage = movie.getImagePath();
        final String favMovBackdropImg = movie.getBackdropPath();
        final Float favMovVoteAvg = movie.getVoteAverage();
        final String favMovAvgString = favMovVoteAvg.toString();
        final String favMovReleaseDate = movie.getReleaseDate();


        // Create a TaskListViewModel instance
        final MoviesViewModel mMoviesViewModel = ViewModelProviders.of(this).get(MoviesViewModel.class);


        LiveData<List<FavoriteEntry>> mAllFavorites = mMoviesViewModel.getAllFavorites();

        mAllFavorites.observe(this,
        new Observer<List<FavoriteEntry>>() {
            @Override
            public void onChanged(@Nullable final List<FavoriteEntry> favoriteMovieEntries) {
                if (favoriteMovieEntries != null) {
                    if (favoriteMovieEntries.size() > 0 ) {
                        for (FavoriteEntry temp : favoriteMovieEntries) {
                             String favoriteId = temp.getMovieId();
                             if(favoriteId.equals(favMovId)){
                                 mIvToggle.setActivated(true);
                             }
                        }
                    }
                }

            }
        });


        mIvToggle = (ImageView) findViewById(R.id.iv_toggle);
        mIvToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(!mIvToggle.isActivated()){
                mIvToggle.setActivated(true);
                ///INSERT
                FavoriteEntry favorite = new FavoriteEntry(favMovId,favMovTitle,favMovOverview,favMovImage,favMovBackdropImg,favMovAvgString,favMovReleaseDate);
                mMoviesViewModel.insert(favorite);

            }else{

                mIvToggle.setActivated(false);
                ///DELETE
                mMoviesViewModel.deleteModelFav(favMovId);
            }
            }
        });

    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }


}
