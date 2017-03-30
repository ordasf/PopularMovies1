package com.project.movies.popular.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * This class define the contract for the MovieFavourites table which will store the favourite
 * movies of the user
 */
public final class MovieFavouritesContract {

    private static final String CONTENT_PREFIX = "content://";
    public static final String CONTENT_AUTHORITY = "com.project.movies.popular.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse(CONTENT_PREFIX + CONTENT_AUTHORITY);

    public static class MovieFavouriteEntry implements BaseColumns {

        public static final String FAVOURITES_PATH = "favourites";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(FAVOURITES_PATH).build();

        public static final String TABLE_NAME = "favourites";

        public static final String COLUMN_MOVIE_ID = "movieId";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER_PATH = "posterPath";

    }
}
