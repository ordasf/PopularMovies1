package com.project.movies.popular.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class MovieFavouritesContentProvider extends ContentProvider {

    private static final int FAVOURITES = 1000;
    private static final int FAVOURITES_ID = 1001;

    private MovieFavouritesDbHelper dbHelper;
    private static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        matcher.addURI(MovieFavouritesContract.BASE_CONTENT_URI.toString(),
                MovieFavouritesContract.MovieFavouriteEntry.FAVOURITES_PATH, FAVOURITES);
        matcher.addURI(MovieFavouritesContract.BASE_CONTENT_URI.toString(),
                MovieFavouritesContract.MovieFavouriteEntry.FAVOURITES_PATH + "/#", FAVOURITES_ID);
    };

    @Override
    public boolean onCreate() {
        dbHelper = new MovieFavouritesDbHelper(this.getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int code = matcher.match(uri);
        Cursor cursor;
        switch (code) {
            case FAVOURITES:
                cursor = db.query(MovieFavouritesContract.MovieFavouriteEntry.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        MovieFavouritesContract.MovieFavouriteEntry.COLUMN_TITLE);
                break;
            case FAVOURITES_ID:
                long id = ContentUris.parseId(uri);
                String[] params = {Long.toString(id)};
                cursor = db.query(MovieFavouritesContract.MovieFavouriteEntry.TABLE_NAME,
                        null,
                        MovieFavouritesContract.MovieFavouriteEntry.COLUMN_MOVIE_ID,
                        params,
                        null,
                        null,
                        null);
                break;
            default: throw new UnsupportedOperationException("The uri: " + uri.toString() + "did not match");
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int code = matcher.match(uri);

        Uri returnUri;
        switch (code) {
            case FAVOURITES:
                long id = db.insert(MovieFavouritesContract.MovieFavouriteEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(MovieFavouritesContract.MovieFavouriteEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Data was not correctly inserted");
                }
                break;
            default: throw new UnsupportedOperationException();
        }

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int code = matcher.match(uri);

        int elemsDeleted = 0;
        switch (code) {
            case FAVOURITES:
                elemsDeleted = db.delete(MovieFavouritesContract.MovieFavouriteEntry.TABLE_NAME, "1=1", null);
                break;
            case FAVOURITES_ID:
                long id = ContentUris.parseId(uri);
                String[] params = {Long.toString(id)};
                elemsDeleted = db.delete(MovieFavouritesContract.MovieFavouriteEntry.TABLE_NAME,
                        MovieFavouritesContract.MovieFavouriteEntry.COLUMN_MOVIE_ID, params);
                break;
            default: throw new UnsupportedOperationException();
        }

        return elemsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Update not supported");
    }
}
