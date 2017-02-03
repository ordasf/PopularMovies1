package com.project.movies.popular.popularmovies1;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextClock;
import android.widget.TextView;

import com.project.movies.popular.popularmovies1.utilities.MovieJSONUtils;
import com.project.movies.popular.popularmovies1.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView mMovieTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMovieTextView = (TextView) findViewById(R.id.tv_movie_data);

        FetchMovieTask movieTask = new FetchMovieTask();
        movieTask.execute();

    }

    public class FetchMovieTask extends AsyncTask<Void, Void, List<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Movie> doInBackground(Void... params) {

            URL movieUrl = NetworkUtils.buildUrl("");

            String response = null;
            try {
                response = NetworkUtils.getResponseFromHttpUrl(movieUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }

            List<Movie> movieList = new ArrayList<>();
            try {
                movieList.addAll(MovieJSONUtils.getMoviesFromJson(response));
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d(TAG, "There is a problem parsing the JSON");
            }

            return movieList;
        }

        @Override
        protected void onPostExecute(List<Movie> result) {
            super.onPostExecute(result);
            mMovieTextView.setText("");
            for (Movie movie : result) {
                mMovieTextView.append(movie.toString() + "\n\n\n");
            }
        }

    }
}
