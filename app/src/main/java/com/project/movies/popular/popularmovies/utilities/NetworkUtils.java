package com.project.movies.popular.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;

import com.project.movies.popular.popularmovies.BuildConfig;
import com.project.movies.popular.popularmovies.MovieOrderType;
import com.project.movies.popular.popularmovies.MoviePosterSize;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the movie db servers. The original NetworkUtils
 * class were using in the exercises of the Fast Track Android Development course and I have adapted
 * the class to perform the communication with the The Movie Database API
 */
public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String BASE_MOVIES_URL = "http://api.themoviedb.org/3/movie/";

    private static final String IMAGE_MOVIE_BASE_URL = " http://image.tmdb.org/t/p/";

    private static final MoviePosterSize IMAGE_SIZE_PATH = MoviePosterSize.W500;

    private static final String API_KEY_QUERY = "api_key";

    final static String KEY = BuildConfig.API_KEY;

    /**
     * Builds the URL used to talk to the movie db server using an order type.
     *
     * @param orderType The type or order to append to the query
     * @return The URL to use to query the movie db server.
     */
    public static URL buildUrl(MovieOrderType orderType) {
        Uri.Builder uriBuilder = Uri.parse(BASE_MOVIES_URL).buildUpon();

        uriBuilder.appendEncodedPath(orderType.getValue());

        /*if (orderType == MovieOrderType.POPULAR) {
            uriBuilder.appendPath(POPULAR_ORDER);
        } else if (orderType == MovieOrderType.TOP_RATED) {
            uriBuilder.appendPath(TOP_RATED_ORDER);
        }*/

        Uri builtUri = uriBuilder.appendQueryParameter(API_KEY_QUERY, KEY).build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(TAG, "Movie URL could not be built");
        }

        Log.v(TAG, "Built URL " + url);

        return url;
    }

    /**
     * This method will construct the URL to the poster movie
     *
     * @param imagePath relative path of the poster image
     * @return and URL with the complete URL
     */
    public static URL buildImageUrl(String imagePath) {

        Uri uri = Uri.parse(IMAGE_MOVIE_BASE_URL).buildUpon()
                .appendEncodedPath(IMAGE_SIZE_PATH.getValue())
                .appendEncodedPath(imagePath).build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(TAG, "Movie image URL could not be built");
        }

        Log.v(TAG, "Built image URL " + url);

        return url;
    }

    /**
     * Builds the URL to get the information of a movie given its ID
     *
     * @param movieId id of the movie that will be used in the URL
     * @return URL built
     */
    public static URL buildMovieDetailUrl(long movieId) {

        Uri uri = Uri.parse(BASE_MOVIES_URL).buildUpon()
                .appendEncodedPath(Long.toString(movieId))
                .appendQueryParameter(API_KEY_QUERY, KEY)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(TAG, "Movie detail URL could not be built");
        }

        Log.v(TAG, "Built movie detail URL " + url);

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

}