package com.project.movies.popular.popularmovies.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.movies.popular.popularmovies.R;
import com.project.movies.popular.popularmovies.beans.Trailer;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for the Trailer Recycler View shown in the MovieDetailActivity
 */
public class TrailerListAdapter extends RecyclerView.Adapter<TrailerListAdapter.TrailerViewHolder> {

    private static final String TAG = TrailerListAdapter.class.getSimpleName();

    private List<Trailer> trailerList = new ArrayList<Trailer>();

    public interface TrailerListAdapterOnClickHandler {
        void onClick(Trailer trailer);
    }

    private TrailerListAdapterOnClickHandler onClickHandler;

    public TrailerListAdapter(TrailerListAdapterOnClickHandler onClickHandler) {
        this.onClickHandler = onClickHandler;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.trailer_list, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        holder.bind(trailerList.get(position));
    }

    @Override
    public int getItemCount() {
        if (trailerList == null) {
            return 0;
        } else {
            return trailerList.size();
        }
    }

    public void setTrailerList(List<Trailer> trailerList) {
        if (trailerList == null) {
            this.trailerList = new ArrayList<>();
        } else {
            this.trailerList = trailerList;
        }
        notifyDataSetChanged();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTrailerNameTextView;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTrailerNameTextView = (TextView) itemView.findViewById(R.id.tv_trailer_list_name);
        }

        public void bind(Trailer trailer) {
            mTrailerNameTextView.setText(trailer.getName());
        }

        @Override
        public void onClick(View v) {
            int index = getAdapterPosition();
            Trailer trailer = trailerList.get(index);
            onClickHandler.onClick(trailer);
        }
    }
}
