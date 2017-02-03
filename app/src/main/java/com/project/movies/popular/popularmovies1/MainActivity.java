package com.project.movies.popular.popularmovies1;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.project.movies.popular.popularmovies1.utilities.MovieJSONUtils;
import com.project.movies.popular.popularmovies1.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView mMovieTextView;

    private ProgressBar mLoadingMain;

    private FetchMovieTask movieTask = new FetchMovieTask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMovieTextView = (TextView) findViewById(R.id.tv_movie_data);

        mLoadingMain = (ProgressBar)  findViewById(R.id.pb_loading_main);

        movieTask.execute("popular");

    }

    public class FetchMovieTask extends AsyncTask<String, Void, List<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingMain.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Movie> doInBackground(String... params) {

            String option = "";
            if (params != null) {
                option = params[0];
            }

            URL movieUrl = NetworkUtils.buildUrl(option);

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
                // TODO Handle Json exeption
//                Toast.makeText(, e.getMessage(), Toast.LENGTH_LONG).show();
            }

            return movieList;
        }

        @Override
        protected void onPostExecute(List<Movie> result) {
            super.onPostExecute(result);

//            try {
//                sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            mLoadingMain.setVisibility(View.INVISIBLE);

            mMovieTextView.setText("");
            for (Movie movie : result) {
                mMovieTextView.append(movie.toString() + "\n\n\n");
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int idSelected = item.getGroupId();
        if (idSelected == R.id.action_order_popular) {
            movieTask.execute("popular");
            return true;
        } else if (idSelected == R.id.action_order_top_rated) {
            movieTask.execute("top_rated");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
