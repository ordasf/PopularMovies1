package com.project.movies.popular.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.project.movies.popular.popularmovies.utilities.MovieJSONUtils;
import com.project.movies.popular.popularmovies.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieListAdapter.MovieListAdapterOnClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ProgressBar mLoadingMain;

    private RecyclerView mMovieListRecyclerView;

    private MovieListAdapter movieListAdapter;

    private TextView mErrorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadingMain = (ProgressBar)  findViewById(R.id.pb_loading_main);

        mMovieListRecyclerView = (RecyclerView) findViewById(R.id.rv_movie_list);

        GridLayoutManager layoutManager = new GridLayoutManager(this, calculateNoOfColumns(getBaseContext()), GridLayoutManager.VERTICAL, false);
        mMovieListRecyclerView.setLayoutManager(layoutManager);

        mMovieListRecyclerView.setHasFixedSize(true);

        movieListAdapter = new MovieListAdapter(this);
        mMovieListRecyclerView.setAdapter(movieListAdapter);

        mErrorTextView = (TextView) findViewById(R.id.tv_error_main);

        new FetchMovieTask().execute(MovieOrderType.POPULAR);

    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra("MOVIE", movie);
        startActivity(intent);
    }

    public class FetchMovieTask extends AsyncTask<MovieOrderType, Void, List<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            hideErrorMessage();
            showLoadingIndicator();
        }

        @Override
        protected List<Movie> doInBackground(MovieOrderType... params) {

            MovieOrderType option = MovieOrderType.POPULAR;
            if (params != null) {
                option = params[0];
            }

            List<Movie> movieList = new ArrayList<>();
            if (!isOnline()) {
                Log.d(TAG, "No connectivity");
                showErrorMessage();
                return movieList;
            }

            URL movieUrl = NetworkUtils.buildUrl(option);

            String response = null;
            try {
                response = NetworkUtils.getResponseFromHttpUrl(movieUrl);
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "There is a problem getting the response");
                showErrorMessage();
            }


            try {
                movieList.addAll(MovieJSONUtils.getMoviesFromJson(response));
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d(TAG, "There is a problem parsing the JSON");
                showErrorMessage();
            }

            return movieList;
        }

        @Override
        protected void onPostExecute(List<Movie> result) {
            super.onPostExecute(result);
            hideLoadingIndicator();
            movieListAdapter.setMovieList(result);
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

        int idSelected = item.getItemId();
        if (idSelected == R.id.action_order_popular) {
            new FetchMovieTask().execute(MovieOrderType.POPULAR);
            return true;
        } else if (idSelected == R.id.action_order_top_rated) {
            new FetchMovieTask().execute(MovieOrderType.TOP_RATED);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();

    }

    private void showLoadingIndicator() {
        mMovieListRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingMain.setVisibility(View.VISIBLE);
    }

    private void hideLoadingIndicator() {
        mLoadingMain.setVisibility(View.INVISIBLE);
        mMovieListRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mMovieListRecyclerView.setVisibility(View.INVISIBLE);
        mErrorTextView.setVisibility(View.VISIBLE);
    }

    private void hideErrorMessage() {
        mErrorTextView.setVisibility(View.INVISIBLE);
        mMovieListRecyclerView.setVisibility(View.VISIBLE);
    }

    public int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns;
    }
}
