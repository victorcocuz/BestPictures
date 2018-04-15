package com.example.android.bestpictures.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bestpictures.R;
import com.example.android.bestpictures.objects.ReviewItem;

import java.util.List;

/**
 * Created by Victor on 3/17/2018.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private static final String LOG_TAG = ReviewAdapter.class.getSimpleName();
    private Context context;
    private List<ReviewItem> reviews;

    public ReviewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ReviewAdapter.ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_review, parent, false);
        view.setFocusable(true);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.ReviewViewHolder holder, int position) {
        holder.vhAuthorView.setText(reviews.get(position).getReviewAuthor());
        holder.vhContentView.setText(reviews.get(position).getReviewContent());
    }

    @Override
    public int getItemCount() {
        if (reviews != null) {
            return reviews.size();
        }
        return 0;
    }

    public void addAll(List<ReviewItem> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        private TextView vhAuthorView;
        private TextView vhContentView;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            vhAuthorView = itemView.findViewById(R.id.card_review_author_view);
            vhContentView = itemView.findViewById(R.id.card_review_content_view);
        }
    }
}
