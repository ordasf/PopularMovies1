package com.project.movies.popular.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.project.movies.popular.popularmovies.beans.Movie;
import com.project.movies.popular.popularmovies.beans.Review;
import com.project.movies.popular.popularmovies.beans.Trailer;
import com.project.movies.popular.popularmovies.utilities.MovieJSONUtils;
import com.project.movies.popular.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MovieDetailActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Movie> {

    private static final String TAG = MovieDetailActivity.class.getSimpleName();

    private static final int MOVIE_LOADER_ID = 2000;

    private static final String MOVIE_ID_KEY = "movie_id_key";

    private TextView mMovieTitleTextView;

    private ImageView mMoviePosterImageView;

    private TextView mSynopsisTextView;

    private TextView mMovieReleaseDateTextView;

    private TextView mMovieRatingTextView;

    private ProgressBar mLoadingDetail;

    private LinearLayout mContainer;

    private TextView mErrorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mMovieTitleTextView = (TextView) findViewById(R.id.tv_detail_title);

        mMoviePosterImageView = (ImageView) findViewById(R.id.iv_detail_poster);

        mSynopsisTextView = (TextView) findViewById(R.id.tv_detail_synopsis);

        mMovieReleaseDateTextView = (TextView) findViewById(R.id.tv_detail_release_date);

        mMovieRatingTextView = (TextView) findViewById(R.id.tv_detail_rating);

        mLoadingDetail = (ProgressBar) findViewById(R.id.pb_loading_detail);

        mContainer = (LinearLayout) findViewById(R.id.ll_detail_container);

        mErrorTextView = (TextView) findViewById(R.id.tv_error_detail);

        Intent intent = getIntent();

        Movie movie = null;
        if (intent.getExtras().containsKey("MOVIE")) {
            movie = (Movie) intent.getExtras().get("MOVIE");
        }

        if (movie == null) {
            Log.e(TAG, "The movie object has not been correctly parceled");
            return;
        }

        long movieId = movie.getId();

        mMovieTitleTextView.setText(movie.getTitle());

        String posterPath = movie.getPosterPath();
        URL imageURL = NetworkUtils.buildImageUrl(posterPath);
        Picasso.with(mMoviePosterImageView.getContext()).load(imageURL.toString()).into(mMoviePosterImageView);

        Bundle loaderBundle = new Bundle();
        loaderBundle.putLong(MOVIE_ID_KEY, movieId);

        LoaderManager loaderManager = getSupportLoaderManager();
        if (loaderManager.getLoader(MOVIE_LOADER_ID) == null) {
            loaderManager.initLoader(MOVIE_LOADER_ID, loaderBundle, this);
        } else {
            loaderManager.restartLoader(MOVIE_LOADER_ID, loaderBundle, this);
        }

    }

    @Override
    public android.support.v4.content.Loader<Movie> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<Movie>(this) {

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
            public Movie loadInBackground() {

                long movieId = args.getLong(MOVIE_ID_KEY);

                Movie movie = new Movie();
                if (!isOnline()) {
                    // Check if the device is connected to the network, in case it's not don't bother
                    // to try to make de API calls, these are going to fail
                    Log.d(TAG, "No connectivity");
                    showErrorMessage();
                    return movie;
                }

                URL movieUrl = NetworkUtils.buildMovieDetailUrl(movieId);

                String response = null;
                try {
                    response = NetworkUtils.getResponseFromHttpUrl(movieUrl);
                    movie = MovieJSONUtils.getMovieDetailsFromJson(response);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "There is a problem retrieving the movie detail info");
                    showErrorMessage();
                }

                URL trailersUrl = NetworkUtils.buildTrailersUrl(movieId);

                String trailersResponse = null;
                List<Trailer> trailers = new ArrayList<>();
                try {
                    trailersResponse = NetworkUtils.getResponseFromHttpUrl(trailersUrl);
                    trailers = MovieJSONUtils.getTrailersFromJson(trailersResponse);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "There is a problem retrieving the trailers info");
                    showErrorMessage();
                }
                movie.setTrailers(trailers);

                URL reviewsUrl = NetworkUtils.buildReviewsUrl(movieId);

                String reviewsResponse = null;
                List<Review> reviews = new ArrayList<>();
                try {
                    reviewsResponse = NetworkUtils.getResponseFromHttpUrl(reviewsUrl);
                    reviews = MovieJSONUtils.getReviewsFromJson(reviewsResponse);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "There is a problem retrieving the reviews info");
                    showErrorMessage();
                }
                movie.setReviews(reviews);

                return movie;
            }
        };
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Movie> loader, Movie data) {
        hideLoadingIndicator();
        mSynopsisTextView.setText(data.getOverview());
        mMovieReleaseDateTextView.append(data.getReleaseDate());
        mMovieRatingTextView.append(Float.toString(data.getRating()));
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Movie> loader) {

    }

    private void showLoadingIndicator() {
        mMovieReleaseDateTextView.setVisibility(View.INVISIBLE);
        mMovieRatingTextView.setVisibility(View.INVISIBLE);
        mLoadingDetail.setVisibility(View.VISIBLE);
    }

    private void hideLoadingIndicator() {
        mLoadingDetail.setVisibility(View.INVISIBLE);
        mMovieReleaseDateTextView.setVisibility(View.VISIBLE);
        mMovieRatingTextView.setVisibility(View.VISIBLE);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();

    }

    private void showErrorMessage() {
        mContainer.setVisibility(View.INVISIBLE);
        mErrorTextView.setVisibility(View.VISIBLE);
    }

    private void hideErrorMessage() {
        mErrorTextView.setVisibility(View.INVISIBLE);
        mContainer.setVisibility(View.VISIBLE);
    }

}
