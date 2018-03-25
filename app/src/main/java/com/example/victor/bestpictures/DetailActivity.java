package com.example.victor.bestpictures;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.LoaderManager;
import android.databinding.DataBindingUtil;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.victor.bestpictures.Adapters.CastAdapter;
import com.example.victor.bestpictures.Adapters.ReviewAdapter;
import com.example.victor.bestpictures.Model.MovieItem;
import com.example.victor.bestpictures.Utilities.NetworkUtils;
import com.example.victor.bestpictures.databinding.ActivityDetailBinding;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<MovieItem> {

    private ActivityDetailBinding binding;

    private CastAdapter castAdapter, crewAdapter;
    private ReviewAdapter reviewAdapter;

    private final static int ID_MOVIE_LOADER = 1;
    private int movieId;
    private String movieAwards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            movieId = extras.getInt(NetworkUtils.TMDB_PARAM_ID);
            movieAwards = extras.getString(NetworkUtils.TMDB_PARAM_AWARDS);
        }

        RecyclerView castRV = binding.detailActivityCastRecyclerView;
        LinearLayoutManager layoutManagerCast = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        castRV.setLayoutManager(layoutManagerCast);
        castRV.setHasFixedSize(true);
        castAdapter = new CastAdapter(this);
        castRV.setAdapter(castAdapter);

        RecyclerView crewRV = binding.detailActivityCrewRecyclerView;
        LinearLayoutManager layoutManagerCrew = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        crewRV.setLayoutManager(layoutManagerCrew);
        crewRV.setHasFixedSize(true);
        crewAdapter = new CastAdapter(this);
        crewRV.setAdapter(crewAdapter);

        RecyclerView reviewRV = binding.detailActivityReviewRecyclerView;
        LinearLayoutManager layoutManagerReview = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        reviewRV.setLayoutManager(layoutManagerReview);
        reviewRV.setHasFixedSize(false);
        reviewAdapter = new ReviewAdapter(this);
        reviewRV.setAdapter(reviewAdapter);


        getSupportLoaderManager().initLoader(ID_MOVIE_LOADER, null, this);
    }

    @Override
    public android.support.v4.content.Loader<MovieItem> onCreateLoader(int id, Bundle args) {
        return new MovieLoaderDetail(this, movieId);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<MovieItem> loader, MovieItem data) {
        populateDetailActivity(loader.getContext(), data);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<MovieItem> loader) {
    }

    private void populateDetailActivity(Context context, MovieItem movie) {
        Picasso.with(context)
                .load(movie.getMovieBackdrop())
                .into(binding.detailActivityBackdrop);

        binding.detailActivityTitle.setText(movie.getMovieTitle());
        binding.detailActivityDirector.setText(context.getResources().getString(R.string.by) + " " + movie.getMovieDirector());
        binding.detailActivityVoteAverage.setText(movie.getMovieVoteAverage());

        binding.detailActivityReleaseDate.setText(movie.getMovieReleaseDate());
        binding.detailActivityRuntime.setText(String.valueOf(movie.getMovieRuntime()));
        StringBuilder genresBuilder = new StringBuilder();
        if (movie.getMovieGenres() != null) {
            for (String s : movie.getMovieGenres()) {
                genresBuilder.append(s + ", ");
            }
            String genres = genresBuilder.toString();
            genres = genres.substring(0, genres.length() - 2);
            binding.detailActivityGenres.setText(genres);
        } else {
            binding.detailActivityGenres.setVisibility(View.GONE);
        }

        binding.detailActivityAwards.setText(movieAwards);

        Picasso.with(context)
                .load(movie.getMoviePoster())
                .into(binding.detailActivityPoster);
        binding.detailActivityOverview.setText(movie.getMovieOverview());

        binding.detailActivityBudget.setText(String.valueOf(movie.getMovieBudget()));
        binding.detailActivityRevenue.setText(String.valueOf(movie.getMovieRevenue()));
        binding.detailActivityPopularity.setText(String.valueOf(Math.round(Float.parseFloat(movie.getMoviePopularity()))));

        if (movie.getMovieCastMembers() != null) {
            castAdapter.addAll(movie.getMovieCastMembers());
        } else {
            binding.detailActivityCastRecyclerView.setVisibility(View.GONE);
        }

        if (movie.getMovieCrewMembers() != null) {
            crewAdapter.addAll(movie.getMovieCrewMembers());
        } else {
            binding.detailActivityCrewRecyclerView.setVisibility(View.GONE);
        }

        if (movie.getMovieReviewItems() != null) {
            reviewAdapter.whateverAll(movie.getMovieReviewItems());
        } else {
            binding.detailActivityReviewRecyclerView.setVisibility(View.GONE);
        }
    }
}
