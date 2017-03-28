package com.project.movies.popular.popularmovies.adapters;

import android.app.LauncherActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.movies.popular.popularmovies.R;
import com.project.movies.popular.popularmovies.beans.Review;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for the list of reviews shown in the Detail Activity
 */
public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ReviewViewHolder> {

    private List<Review> reviewList;

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.review_list, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        holder.bind(reviewList.get(position));
    }

    @Override
    public int getItemCount() {
        if (reviewList == null) {
            return 0;
        } else {
            return reviewList.size();
        }
    }

    public void setReviewList(List<Review> reviewList) {
        if (reviewList == null) {
            this.reviewList = new ArrayList<>();
        } else {
            this.reviewList = reviewList;
        }
        notifyDataSetChanged();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        private TextView mAuthorTextView;
        private TextView mReviewTextView;

        public ReviewViewHolder(View itemView) {
            super(itemView);

            mAuthorTextView = (TextView) itemView.findViewById(R.id.tv_review_list_author);
            mReviewTextView = (TextView) itemView.findViewById(R.id.tv_review_list_review);

        }

        public void bind(Review review) {
            mAuthorTextView.setText(review.getAuthor());
            mReviewTextView.setText(review.getContent());
        }
    }
}
