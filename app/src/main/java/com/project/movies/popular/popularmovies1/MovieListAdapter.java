package com.project.movies.popular.popularmovies1;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for the MovieListRecyclerView
 */
public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> {

    private List<Movie> movieList = new ArrayList<>();

    public MovieListAdapter() {

    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.movie_list, parent, false);
        return new MovieViewHolder(view);

    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bindMovieData(movieList.get(position).toString());
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
        this.movieList = movieList;
        notifyDataSetChanged();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        private TextView mDataListItemTexView;

        public MovieViewHolder(View itemView) {
            super(itemView);

            mDataListItemTexView = (TextView) itemView.findViewById(R.id.tv_movie_element);

        }

        public void bindMovieData(String movieData) {
            mDataListItemTexView.setText(movieData);
        }
    }
}
