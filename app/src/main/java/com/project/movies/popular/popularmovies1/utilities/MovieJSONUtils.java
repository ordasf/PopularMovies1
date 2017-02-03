package com.project.movies.popular.popularmovies1.utilities;

import com.project.movies.popular.popularmovies1.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to work with JSONs
 */
public final class MovieJSONUtils {

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

        /*
        * TODO Check status code or status_message
        * */

        JSONArray movieArray = jsonObject.getJSONArray("results");

        for (int i = 0; i < movieArray.length(); i++) {
            JSONObject movieJson = movieArray.getJSONObject(i);
            long id = movieJson.getLong("id");
            String title = movieJson.getString("title");
            String posterPath = movieJson.getString("poster_path");
            Movie movie = new Movie(id, title, posterPath);
            movieList.add(movie);
        }

        return movieList;

    }

}
