package com.project.movies.popular.popularmovies.utilities;

import android.util.Log;

import com.project.movies.popular.popularmovies.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to work with JSONs
 */
public final class MovieJSONUtils {

    private final static String TAG = MovieJSONUtils.class.getSimpleName();

    private static final String RESULTS = "results";
    private static final String STATUS_CODE = "status_code";
    private static final String STATUS_MESSAGE = "status_message";
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String POSTER_PATH = "poster_path";

    /**
     * Method to convert a Json in string format in a #List of #Movie
     *
     * @param moviesJsonStr a Json stored in a String variable
     * @return the list of Movies
     * @throws JSONException if the Json is not of the expected structure
     */
    public static List<Movie> getMoviesFromJson(String moviesJsonStr) throws JSONException {

        List<Movie> movieList = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(moviesJsonStr);

        if (jsonObject.has(STATUS_CODE)) {
            String statusMessage = jsonObject.getString(STATUS_MESSAGE);
            Log.d(TAG, statusMessage);
            throw new JSONException(STATUS_CODE + " " + STATUS_MESSAGE);
        }

        JSONArray movieArray = jsonObject.getJSONArray(RESULTS);

        for (int i = 0; i < movieArray.length(); i++) {
            JSONObject movieJson = movieArray.getJSONObject(i);
            long id = movieJson.getLong(ID);
            String title = movieJson.getString(TITLE);
            String posterPath = movieJson.getString(POSTER_PATH);
            Movie movie = new Movie(id, title, posterPath);
            movieList.add(movie);
        }

        return movieList;

    }

    public static Movie getMovieDetailsFromJson(String movieDetailJson) throws JSONException {

        Movie movie = new Movie();

        JSONObject jsonObject = new JSONObject(movieDetailJson);

        if (jsonObject.has(STATUS_CODE)) {
            String statusMessage = jsonObject.getString(STATUS_MESSAGE);
            Log.d(TAG, statusMessage);
            throw new JSONException(STATUS_CODE + " " + STATUS_MESSAGE);
        }

        movie.setTitle(jsonObject.getString("original_title"));
        movie.setOverview(jsonObject.getString("overview"));
        movie.setPosterPath(jsonObject.getString(POSTER_PATH));
        movie.setReleaseDate(jsonObject.getString("release_date"));
        movie.setRating(jsonObject.getLong("vote_average"));

        return movie;

    }

}
