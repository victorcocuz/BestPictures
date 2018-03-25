package com.example.victor.bestpictures.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.victor.bestpictures.Model.MovieItem;
import com.example.victor.bestpictures.R;
import com.example.victor.bestpictures.Utilities.BestPicturesUtilities;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor on 3/10/2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<MovieItem> movies = new ArrayList<>();
    MovieAdapterOnClickHandler onClickHandler;
    Context context;
    String awards;

    public interface MovieAdapterOnClickHandler {
        void onClick(int id, String awards);
    }

    public MovieAdapter(Context context, MovieAdapterOnClickHandler onClickHandler) {
        this.context = context;
        this.onClickHandler = onClickHandler;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_movie, parent, false);
        view.setFocusable(true);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.vhTitleView.setText(movies.get(position).getMovieTitle());

        awards = BestPicturesUtilities.randomizeAwards(context);
        holder.vhAwardsView.setText(awards);

        Picasso.with(context)
                .load(movies.get(position).getMoviePoster())
                .into(holder.vhPosterView);
    }

    @Override
    public int getItemCount() {
        if (movies != null) {
            return movies.size();
        }
        return 0;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView vhTitleView;
        private TextView vhAwardsView;
        private ImageView vhPosterView;

        MovieViewHolder(View itemView) {
            super(itemView);
            vhTitleView = itemView.findViewById(R.id.card_movie_title_view);
            vhAwardsView = itemView.findViewById(R.id.card_movie_awards_view);
            vhPosterView = itemView.findViewById(R.id.card_movie_poster_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int id = movies.get(getAdapterPosition()).getMovieId();
            onClickHandler.onClick(id, awards);
        }
    }

    public void addAll(List<MovieItem> movies) {
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }
    public void insertAll(List<MovieItem> movies) {
        this.movies = movies;
    }

}
