package com.example.android.bestpictures.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bestpictures.R;
import com.example.android.bestpictures.data.BestPicturesContract.MoviesEntry;
import com.example.android.bestpictures.objects.MovieItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor on 3/10/2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();
    private List<MovieItem> movies = new ArrayList<>();
    private MovieAdapterOnClickHandler onClickHandler;
    private Cursor cursor;
    private Context context;
    private boolean isCursor = false;

    private String awards;

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
        String title;
        String poster;
        String voteAverage;
        if (isCursor) {
            cursor.moveToPosition(position);
            title = cursor.getString(cursor.getColumnIndex(MoviesEntry.COLUMN_MOVIE_TITLE));
            awards = cursor.getString(cursor.getColumnIndex(MoviesEntry.COLUMN_MOVIE_AWARDS));
            poster = cursor.getString(cursor.getColumnIndex(MoviesEntry.COLUMN_MOVIE_POSTER));
            voteAverage = cursor.getString(cursor.getColumnIndex(MoviesEntry.COLUMN_MOVIE_VOTE_AVERAGE));
        } else {
            title = movies.get(position).getMovieTitle();
            awards = movies.get(position).getMovieAwards();
            poster = movies.get(position).getMoviePoster();
            voteAverage = movies.get(position).getMovieVoteAverage();
        }

        holder.vhTitleView.setText(title);
        holder.vhAwardsView.setText(awards);

        Picasso.with(context)
                .load(poster)
                .into(holder.vhPosterView);

        holder.vhRatingView.setText(voteAverage);
    }

    @Override
    public int getItemCount() {
        if (isCursor) {
            if (cursor != null) {
                return cursor.getCount();
            }
        } else {
            if (movies != null) {
                return movies.size();
            }
        }
        return 0;
    }

    public void appendAll(Object object) {
        if (object != null) {
            if (object instanceof Cursor) {
                isCursor = true;
                this.cursor = (Cursor) object;
            } else {
                isCursor = false;
                this.movies.addAll((List<MovieItem>) object);
            }
            notifyDataSetChanged();
        }
    }

    public void clearAll() {
        this.movies.clear();
        notifyDataSetChanged();
    }

    public interface MovieAdapterOnClickHandler {
        void onClick(int id, String awards);
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView vhTitleView;
        private TextView vhAwardsView;
        private ImageView vhPosterView;
        private TextView vhRatingView;

        MovieViewHolder(View itemView) {
            super(itemView);
            vhTitleView = itemView.findViewById(R.id.card_movie_title_view);
            vhAwardsView = itemView.findViewById(R.id.card_movie_awards_view);
            vhPosterView = itemView.findViewById(R.id.card_movie_poster_view);
            vhRatingView = itemView.findViewById(R.id.card_movie_rating_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int id;
            if (isCursor) {
                cursor.moveToPosition(getAdapterPosition());
                id = cursor.getInt(cursor.getColumnIndex(MoviesEntry.COLUMN_MOVIE_ID));
            } else {
                id = movies.get(getAdapterPosition()).getMovieId();
            }
            onClickHandler.onClick(id, awards);
        }
    }
}
