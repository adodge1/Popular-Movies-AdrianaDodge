package com.example.android.popular_movies_adrianadodge;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.android.popular_movies_adrianadodge.database.FavoriteEntry;
import com.example.android.popular_movies_adrianadodge.model.Movie;
import com.example.android.popular_movies_adrianadodge.utils.NetworkUtils;
import com.facebook.stetho.Stetho;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;




public class MainActivity extends AppCompatActivity implements ItemRecyclerAdapter.ItemRecyclerAdapterOnClickHandler,FavoriteRecyclerAdapter.FavoriteRecyclerAdapterOnClickHandler{

    public static final String MOVIE_KEY = "YOUR_API_KEY_HERE";

    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageDisplay;
    private ItemRecyclerAdapter mMovieAdapter;
    private RecyclerView mMovieRecyclerView;
    private GridLayoutManager layoutManager;
    private int mNumberColumns;
    private String mMovieMenuSort;
    private FavoriteRecyclerAdapter mFavoriteAdapter;
    private MoviesViewModel mMoviesViewModel;


    //save sort order persistence: https://developer.android.com/training/data-storage/shared-preferences#java
    private final String KEY_CURRENT_MENU = "key_current_menu";
    private static final String KEY_BUNDLE_CURRENT_MENU = "key_bundle_current_menu";
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private static final String PREFS_FILE ="com.example.android.popular_movies_adrianadodge.preferences";

    //save scroll position: https://stackoverflow.com/questions/27816217/how-to-save-recyclerviews-scroll-position-using-recyclerview-state
    private static final String BUNDLE_RECYCLER_LAYOUT = "key_bundle_recycler_layout";
    private Parcelable mSavedRecyclerLayoutState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = this;
        Stetho.initializeWithDefaults(this);

        mSharedPreferences = getSharedPreferences(PREFS_FILE,Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mMovieMenuSort = mSharedPreferences.getString(KEY_CURRENT_MENU,"popular");

        setContentView(R.layout.activity_main);
        mMovieRecyclerView = findViewById(R.id.rv_movies);

        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);
        mNumberColumns = calculateNoOfColumns(this);
        // use a grid layout manager
        layoutManager = new GridLayoutManager(this, mNumberColumns, GridLayoutManager.VERTICAL, false);

        mMovieRecyclerView.setLayoutManager(layoutManager);

        // use this setting to improve performance
        mMovieRecyclerView.setHasFixedSize(true);


        populateUI(mMovieMenuSort);
    }



   @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putString(KEY_BUNDLE_CURRENT_MENU, mMovieMenuSort);

       savedInstanceState.putParcelable(BUNDLE_RECYCLER_LAYOUT, mMovieRecyclerView.getLayoutManager().onSaveInstanceState());


   }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        mMovieMenuSort = savedInstanceState.getString(KEY_BUNDLE_CURRENT_MENU);

        if(savedInstanceState != null)
        {
            mSavedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            mMovieRecyclerView.getLayoutManager().onRestoreInstanceState(mSavedRecyclerLayoutState);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        mEditor.putString(KEY_CURRENT_MENU,mMovieMenuSort);
        mEditor.apply();
    }


    private void populateUI(String sortBy) {

        if(sortBy.equals("favorites")){
            // specify an adapter
            mFavoriteAdapter = new FavoriteRecyclerAdapter(this);
            //set Adapter to recyclerView
            mMovieRecyclerView.setAdapter(mFavoriteAdapter);

            mMoviesViewModel = ViewModelProviders.of(this).get(MoviesViewModel.class);

            mMoviesViewModel.getAllFavorites().observe(this, new Observer<List<FavoriteEntry>>() {
                @Override
                public void onChanged(@Nullable final List<FavoriteEntry> favorites) {
                    // Update the cached copy of the movies in the adapter.
                    mFavoriteAdapter.setFavoriteMovieData(favorites);
                }
            });

        }else {


            // specify an adapter
            mMovieAdapter = new ItemRecyclerAdapter(this);
            //set Adapter to recyclerView
            mMovieRecyclerView.setAdapter(mMovieAdapter);
            mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
            //Build URL
            URL moviesUrl = NetworkUtils.buildUrl(sortBy, MOVIE_KEY);
            //Do HTTPS request on background Thread
            new HTTPrequestBackgroundThread().execute(moviesUrl);
        }


    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 200;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        if(noOfColumns < 3)
            noOfColumns = 3;
        return noOfColumns;
    }


    private void showMovieDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mMovieRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mMovieRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    /*
    * background thread AsyncTask enables proper and easy use of the UI thread. This class allows you to perform background
    * operations and publish results on the UI thread without having to manipulate threads and/or handlers.
    * extends AsyncTask
    * overrides: doInBackground and onPostExecute
    *
     */

    public class HTTPrequestBackgroundThread extends AsyncTask<URL, Void, ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(URL... params) {
            URL searchUrl = params[0];

            String responseFromHttpUrl = null;
            ArrayList<Movie> movies =null;

            ConnectivityManager ConnectionManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo=ConnectionManager.getActiveNetworkInfo();
            if(networkInfo != null && networkInfo.isConnected()==true )
            {
                //Toast.makeText(MainActivity.this, "Network Available", Toast.LENGTH_LONG).show();
                try {
                    responseFromHttpUrl = NetworkUtils.getResponseFromHttpUrl(searchUrl);
                    movies = NetworkUtils.extractFeatureFromJson(responseFromHttpUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(MainActivity.this, "Network Not Available", Toast.LENGTH_LONG).show();

            }

            //return responseFromHttpUrl;
            return movies;

        }

        @Override
        protected void onPostExecute(ArrayList<Movie> results) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (results != null) {
                showMovieDataView();
                mMovieAdapter.setMovieData(results);
                mMovieRecyclerView.getLayoutManager().onRestoreInstanceState(mSavedRecyclerLayoutState);
            } else {
                showErrorMessage();
            }
        }

    }


    @Override
    public void onClick(Movie selectedMovie) {
        Intent intentToStartDetailActivity = new Intent(MainActivity.this, DetailActivity.class);
        intentToStartDetailActivity.putExtra("Movie.Details",selectedMovie);
        startActivity(intentToStartDetailActivity);
    }


    @Override
    public void onClick(FavoriteEntry selectedFaves) {
        Intent intentToStartDetailActivity = new Intent(MainActivity.this, DetailActivity.class);
        intentToStartDetailActivity.putExtra("MovieFaves.Details",selectedFaves);
        startActivity(intentToStartDetailActivity);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //  Within onCreateOptionsMenu, use getMenuInflater().inflate to inflate the menu
        getMenuInflater().inflate(R.menu.main, menu);
        // Return true to display your menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_most_popular) {
            mMovieMenuSort = "popular";
            populateUI(mMovieMenuSort);
            return true;
        }else if (itemThatWasClickedId == R.id.action_highest_rated) {
            mMovieMenuSort = "top_rated";
            populateUI(mMovieMenuSort);
            return true;
        }else if (itemThatWasClickedId == R.id.action_favorites) {
            mMovieMenuSort = "favorites";
            populateUI(mMovieMenuSort);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
