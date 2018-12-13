package com.example.android.popular_movies_adrianadodge.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

@Database(entities = {FavoriteEntry.class}, version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {


    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "favorite_movies";
    private static AppDatabase sInstance;


    // singleton to prevent having multiple instances of the database opened at the same time.
    public static AppDatabase getDatabase(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        // call allowMainThreadQueries before building the instance
                        // Queries should be done in a separate thread to avoid locking the UI
                        // We will allow this ONLY TEMPORALLY to see that our DB is working
                        .allowMainThreadQueries()
                        .addCallback(sRoomDatabaseCallback)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }


    private static RoomDatabase.Callback sRoomDatabaseCallback =
    new RoomDatabase.Callback(){
        @Override
        public void onOpen (@NonNull SupportSQLiteDatabase db){
        super.onOpen(db);
        new PopulateDbAsync(sInstance).execute();
        }
    };


    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final FavoriteDao mDao;

        PopulateDbAsync(AppDatabase db) {
            mDao = db.favoriteDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {


            return null;
        }
    }

    public abstract FavoriteDao favoriteDao();
}
