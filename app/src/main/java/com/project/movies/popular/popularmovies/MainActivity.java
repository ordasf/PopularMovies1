package com.project.movies.popular.popularmovies;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
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

import com.project.movies.popular.popularmovies.adapters.MovieListAdapter;
import com.project.movies.popular.popularmovies.beans.Movie;
import com.project.movies.popular.popularmovies.data.MovieFavouritesContract;
import com.project.movies.popular.popularmovies.utilities.MovieJSONUtils;
import com.project.movies.popular.popularmovies.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        MovieListAdapter.MovieListAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<List<Movie>> {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String INSTANCE_STATE_BUNDLE_ORDER_KEY = "order_key";

    private static final int LOADER_MOVIE_LIST_ID = 1000;
    private static final int LOADER_MOVIE_FAVOURITES_LIST_ID = 2000;
    private static final String LOADER_ORDER_TYPE_KEY = "loader_order_type_key";

    private MovieOrderType movieOrderType = MovieOrderType.POPULAR;

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

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(INSTANCE_STATE_BUNDLE_ORDER_KEY)) {
                List<Movie> movieList = savedInstanceState.getParcelableArrayList(INSTANCE_STATE_BUNDLE_ORDER_KEY);
                movieListAdapter.setMovieList(movieList);
            }
        } else {
            movieOrderType = MovieOrderType.POPULAR;
            startLoader();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<Movie> movieList = new ArrayList<>(movieListAdapter.getMovieList());
        outState.putParcelableArrayList(INSTANCE_STATE_BUNDLE_ORDER_KEY, movieList);
    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra("MOVIE", movie);
        startActivity(intent);
    }

    private void startLoader() {

        int loader_id;
        Bundle loaderBundle = new Bundle();
        if (MovieOrderType.FAVOURITE == movieOrderType) {
            loader_id = LOADER_MOVIE_FAVOURITES_LIST_ID;
        } else {
            loader_id = LOADER_MOVIE_LIST_ID;
            loaderBundle.putString(LOADER_ORDER_TYPE_KEY, movieOrderType.getValue());
        }

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<List<Movie>> movieListLoader = loaderManager.getLoader(loader_id);
        if (movieListLoader == null) {
            loaderManager.initLoader(loader_id, loaderBundle, this);
        } else {
            loaderManager.restartLoader(loader_id, loaderBundle, this);
        }

    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, final Bundle args) {

        switch(id) {
            case LOADER_MOVIE_LIST_ID:
                return new AsyncTaskLoader<List<Movie>>(this) {

                    @Override
                    protected void onStartLoading() {
                        super.onStartLoading();
                        if (args == null) {
                            return;
                        }
                        hideErrorMessage();
                        showLoadingIndicator();
                        forceLoad();
                    }

                    @Override
                    public List<Movie> loadInBackground() {

                        MovieOrderType option = MovieOrderType.getFromString(args.getString(LOADER_ORDER_TYPE_KEY));

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

                };
            case LOADER_MOVIE_FAVOURITES_LIST_ID:
                return new AsyncTaskLoader<List<Movie>>(this) {

                    @Override
                    protected void onStartLoading() {
                        super.onStartLoading();
                        if (args == null) {
                            return;
                        }
                        hideErrorMessage();
                        showLoadingIndicator();
                        forceLoad();
                    }

                    @Override
                    public List<Movie> loadInBackground() {

                        ContentResolver resolver = getContentResolver();
                        Uri uri = MovieFavouritesContract.MovieFavouriteEntry.CONTENT_URI;
                        Cursor cursor = resolver.query(uri, null, null, null, null);
                        List<Movie> movieList = new ArrayList<>();
                        if (cursor != null) {
                            while (cursor.moveToNext()) {
                                long movieId = cursor.getLong(cursor.getColumnIndex(MovieFavouritesContract.MovieFavouriteEntry.COLUMN_MOVIE_ID));
                                String movieTitle = cursor.getString(cursor.getColumnIndex(MovieFavouritesContract.MovieFavouriteEntry.COLUMN_TITLE));
                                String posterPath = cursor.getString(cursor.getColumnIndex(MovieFavouritesContract.MovieFavouriteEntry.COLUMN_POSTER_PATH));
                                movieList.add(new Movie(movieId, movieTitle, posterPath));
                            }

                            cursor.close();
                        }

                        return movieList;
                    }

                };
            default:
                throw new UnsupportedOperationException("Loader not recognized");
        }
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> result) {
        hideLoadingIndicator();
        movieListAdapter.setMovieList(result);
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {

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
        switch (idSelected) {
            case R.id.action_order_popular:
                movieOrderType = MovieOrderType.POPULAR;
                startLoader();
                return true;
            case R.id.action_order_top_rated:
                movieOrderType = MovieOrderType.TOP_RATED;
                startLoader();
                return true;
            case R.id.action_favourites:
                movieOrderType = MovieOrderType.FAVOURITE;
                startLoader();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
