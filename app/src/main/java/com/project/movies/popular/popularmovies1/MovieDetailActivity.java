package com.project.movies.popular.popularmovies1;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.project.movies.popular.popularmovies1.utilities.MovieJSONUtils;
import com.project.movies.popular.popularmovies1.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String TAG = MovieDetailActivity.class.getSimpleName();

    private TextView mMovieTitleTextView;

    private ImageView mMoviePosterImageView;

    private TextView mSynopsisTextView;

    private TextView mMovieReleaseDateTextView;

    private TextView mMovieRatingTextView;

    private ProgressBar mLoadingDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent intent = getIntent();

        long movieId = 0L;
        if (intent.getExtras().containsKey("movieId")) {
            movieId = intent.getExtras().getLong("movieId");
        }

        mMovieTitleTextView = (TextView) findViewById(R.id.tv_detail_title);
        if (intent.getExtras().containsKey("movieTitle")) {
            String text = intent.getExtras().getString("movieTitle");
            mMovieTitleTextView.setText(text);
        }

        mMoviePosterImageView = (ImageView) findViewById(R.id.iv_detail_poster);
        if (intent.getExtras().containsKey("moviePoster")) {
            String posterPath = intent.getExtras().getString("moviePoster");
            URL imageURL = NetworkUtils.buildImageUrl(posterPath);
            Picasso.with(mMoviePosterImageView.getContext()).load(imageURL.toString()).into(mMoviePosterImageView);
        }

        mSynopsisTextView = (TextView) findViewById(R.id.tv_detail_synopsis);

        mMovieReleaseDateTextView = (TextView) findViewById(R.id.tv_detail_release_date);

        mMovieRatingTextView = (TextView) findViewById(R.id.tv_detail_rating);

        mLoadingDetail = (ProgressBar) findViewById(R.id.pb_loading_detail);

        new MovieDetailReleaseDateAsyncTask().execute(movieId);

    }

    public class MovieDetailReleaseDateAsyncTask extends AsyncTask<Long, Void, Movie> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoadingIndicator();
        }

        @Override
        protected Movie doInBackground(Long... params) {

            long movieId = 0L;
            if (params != null) {
                movieId = params[0];
            }

            Movie movie = new Movie();
            if (!isOnline()) {
                // Check if the device is connected to the network, in case it's not don't bother
                // to try to make de API calls, these are going to fail
                return movie;
            }

            URL movieUrl = NetworkUtils.buildMovieDetailUrl(movieId);

            String response = null;
            try {
                response = NetworkUtils.getResponseFromHttpUrl(movieUrl);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "There is a problem parsing the JSON");
                // TODO Handle exception properly
            }

            try {
                movie = MovieJSONUtils.getMovieDetailsFromJson(response);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "There is a problem parsing the JSON");
                // TODO Handle exception properly
            }

            return movie;
        }

        @Override
        protected void onPostExecute(Movie response) {
            super.onPostExecute(response);
            hideLoadingIndicator();
            mSynopsisTextView.setText(response.getOverview());
            mMovieReleaseDateTextView.append(response.getReleaseDate());
            mMovieRatingTextView.append(Float.toString(response.getRating()));
        }

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

}
