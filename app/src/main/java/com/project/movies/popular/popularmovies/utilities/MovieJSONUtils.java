package com.project.movies.popular.popularmovies.utilities;

import android.util.Log;

import com.project.movies.popular.popularmovies.beans.Movie;
import com.project.movies.popular.popularmovies.beans.Review;
import com.project.movies.popular.popularmovies.beans.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
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
    private static final String TRAILER_KEY = "key";
    private static final String TRAILER_NAME = "name";
    private static final String TRAILER_SITE = "site";
    private static final String TRAILER_SIZE = "size";
    private static final String REVIEW_AUTHOR = "author";
    private static final String REVIEW_CONTENT = "content";
    private static final String REVIEW_URL = "url";

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

    public static List<Trailer> getTrailersFromJson(String trailersJsonStr) throws JSONException {

        List<Trailer> trailerList = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(trailersJsonStr);

        if (jsonObject.has(STATUS_CODE)) {
            String statusMessage = jsonObject.getString(STATUS_MESSAGE);
            Log.d(TAG, statusMessage);
            throw new JSONException(STATUS_CODE + " " + STATUS_MESSAGE);
        }

        JSONArray trailersArray = jsonObject.getJSONArray(RESULTS);

        for (int i = 0; i < trailersArray.length(); i++) {

            JSONObject trailerJson = trailersArray.getJSONObject(i);

            String id = trailerJson.getString(ID);
            String key = trailerJson.getString(TRAILER_KEY);
            String name = trailerJson.getString(TRAILER_NAME);
            String site = trailerJson.getString(TRAILER_SITE);
            String size = trailerJson.getString(TRAILER_SIZE);

            Trailer trailer = new Trailer(id, key, name, site, size);
            trailerList.add(trailer);
        }

        return trailerList;

    }

    public static List<Review> getReviewsFromJson(String reviewsJsonStr) throws JSONException {

        List<Review> reviewsList = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(reviewsJsonStr);

        if (jsonObject.has(STATUS_CODE)) {
            String statusMessage = jsonObject.getString(STATUS_MESSAGE);
            Log.d(TAG, statusMessage);
            throw new JSONException(STATUS_CODE + " " + STATUS_MESSAGE);
        }

        JSONArray reviewsArray = jsonObject.getJSONArray(RESULTS);

        for (int i = 0; i < reviewsArray.length(); i++) {

            JSONObject reviewJson = reviewsArray.getJSONObject(i);

            String id = reviewJson.getString(ID);
            String author = reviewJson.getString(REVIEW_AUTHOR);
            String content = reviewJson.getString(REVIEW_CONTENT);
            String url = reviewJson.getString(REVIEW_URL);

            Review review = new Review(id, author, content, url);
            reviewsList.add(review);
        }

        return reviewsList;

    }

}
