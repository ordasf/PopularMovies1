package com.project.movies.popular.popularmovies1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MovieDetailActivity extends AppCompatActivity {

    private TextView mMovieDetailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mMovieDetailTextView = (TextView) findViewById(R.id.tv_movie_detail);

        Intent intent = getIntent();
        if (intent.getExtras().containsKey(Intent.EXTRA_TEXT)) {
            String text = intent.getExtras().getString(Intent.EXTRA_TEXT);
            mMovieDetailTextView.setText(text);
        }


    }
}
