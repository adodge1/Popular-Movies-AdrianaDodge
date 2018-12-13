package com.example.android.popular_movies_adrianadodge.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class FavoriteRepository {
    private FavoriteDao mFavDao;
    private LiveData<List<FavoriteEntry>> mAllFavorites;


    public FavoriteRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mFavDao = db.favoriteDao();
        mAllFavorites = mFavDao.loadAllFavorites();
    }

    //Add a wrapper for getAllFavorites(). Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<FavoriteEntry>> getAllFavorites() {
        return mAllFavorites;
    }


    public void deleteRepoFav(String fav_mov_id) {
        new deleteAsyncTask(mFavDao).execute(fav_mov_id);
    }

    private static class deleteAsyncTask extends AsyncTask<String, Void, Void> {

        private FavoriteDao mAsyncTaskDao2;

        deleteAsyncTask(FavoriteDao dao) {
            mAsyncTaskDao2 = dao;
        }

        @Override
        protected Void doInBackground(final String... params) {
            mAsyncTaskDao2.deleteFavorite(params[0]);
            return null;
        }
    }


    public void insert (FavoriteEntry fav) {
        new insertAsyncTask(mFavDao).execute(fav);
    }
    private static class insertAsyncTask extends AsyncTask<FavoriteEntry, Void, Void> {

        private FavoriteDao mAsyncTaskDao;

        insertAsyncTask(FavoriteDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final FavoriteEntry... params) {
            mAsyncTaskDao.insertFavorite(params[0]);
            return null;
        }
    }


}
