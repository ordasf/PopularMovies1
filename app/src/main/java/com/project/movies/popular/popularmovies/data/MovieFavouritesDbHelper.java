package com.project.movies.popular.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This class is a helper class for creating the database
 */
public final class MovieFavouritesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favouriteMovies.db";
    private static final int DATABASE_VERSION = 1;

    public MovieFavouritesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAVOURITES_TABLE =
                "CREATE TABLE " + MovieFavouritesContract.MovieFavouriteEntry.TABLE_NAME + "(" +
                        MovieFavouritesContract.MovieFavouriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        MovieFavouritesContract.MovieFavouriteEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL," +
                        MovieFavouritesContract.MovieFavouriteEntry.COLUMN_TITLE + " TEXT NOT NULL," +
                        MovieFavouritesContract.MovieFavouriteEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL" +
                        ");";
        db.execSQL(SQL_CREATE_FAVOURITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieFavouritesContract.MovieFavouriteEntry.TABLE_NAME);
        onCreate(db);
    }
}
