package com.example.android.popular_movies_adrianadodge;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;


import com.example.android.popular_movies_adrianadodge.database.FavoriteEntry;
import com.example.android.popular_movies_adrianadodge.database.FavoriteRepository;

import java.util.List;

public class MoviesViewModel extends AndroidViewModel {


    //Add a private member variable to hold a reference to the repository.

    private FavoriteRepository mRepository;

    // Add a private LiveData member variable to cache the list of words.

    private LiveData<List<FavoriteEntry>> mAllFavoriteMovies;

    //   Add a constructor that gets a reference to the repository and gets the list of words from the repository.

    public MoviesViewModel (Application application) {
        super(application);
        mRepository = new FavoriteRepository(application);
        mAllFavoriteMovies = mRepository.getAllFavorites();
    }



    //Add a "getter" method for all the words. This completely hides the implementation from the UI.

    LiveData<List<FavoriteEntry>> getAllFavorites() { return mAllFavoriteMovies; }

    //Create a wrapper insert() method that calls the Repository's insert() method. In this way, the implementation of insert() is completely hidden from the UI.

    public void insert(FavoriteEntry fav) { mRepository.insert(fav); }

    public void deleteModelFav(String favoriteMovieId) { mRepository.deleteRepoFav(favoriteMovieId);}


}
