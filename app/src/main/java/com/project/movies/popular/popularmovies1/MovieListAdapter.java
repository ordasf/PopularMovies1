package com.project.movies.popular.popularmovies1;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.movies.popular.popularmovies1.utilities.NetworkUtils;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for the MovieListRecyclerView
 */
public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> {

    private static final String TAG = MovieListAdapter.class.getSimpleName();

    private List<Movie> movieList = new ArrayList<>();

    public interface MovieListAdapterOnClickHandler {
        void onClick(Movie movie);
    }

    private MovieListAdapterOnClickHandler onClickHandler;

    public MovieListAdapter(MovieListAdapterOnClickHandler onClickHandler) {
        this.onClickHandler = onClickHandler;
    }

    /**
     * ViewHolder for the MovieListAdapter
     */
    public class MovieViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        private Movie movie;

        private ImageView mMoviePosterImageView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mMoviePosterImageView = (ImageView) itemView.findViewById(R.id.iv_movie_poster_list);

        }

        public void bindMovieData(Movie movie) {
            this.movie = movie;
            URL imageURL = NetworkUtils.buildImageUrl(movie.getPosterPath());
            Picasso.with(mMoviePosterImageView.getContext()).load(imageURL.toString()).into(mMoviePosterImageView);
        }

        @Override
        public void onClick(View v) {
            int index = getAdapterPosition();
            Movie movie = movieList.get(index);
            onClickHandler.onClick(movie);
        }

    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.movie_list, parent, false);
        return new MovieViewHolder(view);

    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bindMovieData(movieList.get(position));
    }

    @Override
    public int getItemCount() {
        if (movieList != null) {
            return movieList.size();
        } else {
            return 0;
        }
    }

    public void setMovieList(List<Movie> movieList) {
        if (movieList == null) {
            this.movieList = new ArrayList<>();
        } else {
            this.movieList = movieList;
            notifyDataSetChanged();
        }
    }

}
