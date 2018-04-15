package com.example.android.bestpictures;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.android.bestpictures.adapters.CastAdapter;
import com.example.android.bestpictures.adapters.ReviewAdapter;
import com.example.android.bestpictures.data.BestPicturesContract.CastEntry;
import com.example.android.bestpictures.data.BestPicturesContract.MoviesEntry;
import com.example.android.bestpictures.data.BestPicturesContract.ReviewsEntry;
import com.example.android.bestpictures.databinding.ActivityDetailBinding;
import com.example.android.bestpictures.loaders.MovieDetailLoader;
import com.example.android.bestpictures.objects.CastMember;
import com.example.android.bestpictures.objects.MovieItem;
import com.example.android.bestpictures.objects.ReviewItem;
import com.example.android.bestpictures.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {

    //Static constants
    public static final int ID_MOVIE_CURSOR_LOADER = 1;
    public static final int ID_CAST_CURSOR_LOADER = 2;
    public static final int ID_REVIEW_CURSOR_LOADER = 3;
    public static final int ID_CREW_CURSOR_LOADER = 4;
    public static final int ID_MOVIE_LOADER = 1000;
    public static final int ID_VIDEO_LOADER = 1001;
    public static final int ID_CAST_ADAPTER = 100;
    public static final int ID_CREW_ADAPTER = 100;
    private static final String LOG_TAG = DetailActivity.class.getSimpleName();
    private static String url;
    //DetailActivity fields
    String movieBackdrop = null;
    int movieBudget = 0;
    String movieGenres = null;
    List<String> movieGenresList = null;
    String movieOverview = null;
    String moviePopularity = null;
    String moviePoster = null;
    String movieReleaseDate = null;
    int movieRevenue = 0;
    int movieRuntime = 0;
    String movieTitle = null;
    String movieVoteAverage = null;
    String movieDirector = null;
    List<CastMember> castMembers = null;
    List<CastMember> crewMembers = null;
    List<ReviewItem> reviewItems = null;
    private ActivityDetailBinding binding;
    private CastAdapter castAdapter, crewAdapter;
    private ReviewAdapter reviewAdapter;
    private boolean IS_FAVORITE = false;
    //Received from intent
    private int movieId;
    private String movieAwards;
    //Movie projection
    private String[] movieProjection = new String[]{
            MoviesEntry.COLUMN_MOVIE_ID,
            MoviesEntry.COLUMN_MOVIE_BACKDROP,
            MoviesEntry.COLUMN_MOVIE_BUDGET,
            MoviesEntry.COLUMN_MOVIE_GENRES,
            MoviesEntry.COLUMN_MOVIE_OVERVIEW,
            MoviesEntry.COLUMN_MOVIE_POPULARITY,
            MoviesEntry.COLUMN_MOVIE_POSTER,
            MoviesEntry.COLUMN_MOVIE_RELEASE_DATE,
            MoviesEntry.COLUMN_MOVIE_REVENUE,
            MoviesEntry.COLUMN_MOVIE_RUNTIME,
            MoviesEntry.COLUMN_MOVIE_TITLE,
            MoviesEntry.COLUMN_MOVIE_VOTE_AVERAGE,
            MoviesEntry.COLUMN_MOVIE_DIRECTOR,
            MoviesEntry.COLUMN_MOVIE_AWARDS,
            MoviesEntry.COLUMN_MOVIE_VIDEO_URL};

    //Cast projection
    private String[] castProjection = new String[]{
            CastEntry.COLUMN_CAST_MOVIE_ID,
            CastEntry.COLUMN_CAST_TYPE,
            CastEntry.COLUMN_CAST_NAME,
            CastEntry.COLUMN_CAST_SUBTITLE,
            CastEntry.COLUMN_CAST_PROFILE};

    //Crew projection
    private String[] reviewsProjection = new String[]{
            ReviewsEntry.COLUMN_REVIEW_MOVIE_ID,
            ReviewsEntry.COLUMN_REVIEW_AUTHOR,
            ReviewsEntry.COLUMN_REVIEW_CONTENT};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        setSupportActionBar(binding.detailActivityToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(null);
        }
        binding.detailActivityCollapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.colorText));
        binding.detailActivityCollapsingToolbar.setCollapsedTitleTextColor(getResources().getColor(R.color.colorText));

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            movieId = extras.getInt(NetworkUtils.TMDB_PARAM_ID);
            movieAwards = extras.getString(NetworkUtils.TMDB_PARAM_AWARDS);
        }
        binding.detailActivityFrameGradient.setVisibility(View.INVISIBLE);

        getSupportLoaderManager().initLoader(ID_MOVIE_CURSOR_LOADER, null, this);

        //Recycler views for Cast, Crew and Reviews
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
        NetworkUtils.getUrlForVideo(movieId);

        //Adding or removing favorite movies
        binding.detailActivityFavoriteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!IS_FAVORITE) {
                    addFavorite();
                    addToDatabase(ID_MOVIE_CURSOR_LOADER);
                    addToDatabase(ID_CAST_CURSOR_LOADER);
                    addToDatabase(ID_REVIEW_CURSOR_LOADER);
                } else {
                    removeFavorite();
                    String selection = MoviesEntry.COLUMN_MOVIE_ID + "=?";
                    String selectionArgs[] = new String[]{String.valueOf(movieId)};
                    getContentResolver().delete(MoviesEntry.MOVIES_CONTENT_URI, selection, selectionArgs);
                    getContentResolver().delete(CastEntry.CAST_AND_CREW_CONTENT_URI, selection, selectionArgs);
                    getContentResolver().delete(ReviewsEntry.REVIEWS_CONTENT_URI, selection, selectionArgs);
                }
            }
        });
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        switch (id) {
            case ID_MOVIE_CURSOR_LOADER:
                String movieSelection = MoviesEntry.COLUMN_MOVIE_ID + "=?";
                String[] movieSelectionArgs = new String[]{String.valueOf(movieId)};
                return new CursorLoader(this, MoviesEntry.MOVIES_CONTENT_URI, movieProjection, movieSelection, movieSelectionArgs, null);
            case ID_CAST_CURSOR_LOADER:
                String castSelection = CastEntry.COLUMN_CAST_MOVIE_ID + "=?";
                String[] castSelectionArgs = new String[]{String.valueOf(movieId)};
                return new CursorLoader(this, CastEntry.CAST_AND_CREW_CONTENT_URI, castProjection, castSelection, castSelectionArgs, null);
            case ID_REVIEW_CURSOR_LOADER:
                String reviewSelection = ReviewsEntry.COLUMN_REVIEW_MOVIE_ID + "=?";
                String[] reviewSelectionArgs = new String[]{String.valueOf(movieId)};
                return new CursorLoader(this, ReviewsEntry.REVIEWS_CONTENT_URI, reviewsProjection, reviewSelection, reviewSelectionArgs, null);
            case ID_MOVIE_LOADER:
                return new MovieDetailLoader(this, movieId, ID_MOVIE_LOADER);
            case ID_VIDEO_LOADER:
                return new MovieDetailLoader(this, movieId, ID_VIDEO_LOADER);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        switch (loader.getId()) {
            case ID_MOVIE_CURSOR_LOADER:
                Cursor movieCursor = (Cursor) data;
                if (movieCursor.getCount() > 0) {
                    movieCursor.moveToFirst();
                    int currentMovieId;
                    currentMovieId = movieCursor.getInt(movieCursor.getColumnIndex(MoviesEntry.COLUMN_MOVIE_ID));
                    if (currentMovieId == movieId) {
                        addFavorite();
                        populateMovieValues(loader.getContext(), movieCursor, ID_MOVIE_CURSOR_LOADER);
                        getSupportLoaderManager().initLoader(ID_CAST_CURSOR_LOADER, null, this);
                        getSupportLoaderManager().initLoader(ID_REVIEW_CURSOR_LOADER, null, this);
                    }
                } else {
                    removeFavorite();
                    getSupportLoaderManager().initLoader(ID_MOVIE_LOADER, null, this);
                    getSupportLoaderManager().initLoader(ID_VIDEO_LOADER, null, this);
                }
                break;
            case ID_CAST_CURSOR_LOADER:
                Cursor castCursor = (Cursor) data;
                populateCastValues(castCursor, ID_CAST_CURSOR_LOADER);
                break;
            case ID_REVIEW_CURSOR_LOADER:
                Cursor reviewCursor = (Cursor) data;
                populateReviewValues(reviewCursor, ID_REVIEW_CURSOR_LOADER);
                break;
            case ID_MOVIE_LOADER:
                populateMovieValues(loader.getContext(), data, ID_MOVIE_LOADER);
                populateCastValues(data, ID_MOVIE_LOADER);
                populateReviewValues(data, ID_MOVIE_LOADER);
                break;
            case ID_VIDEO_LOADER:
                MovieItem movieItem = (MovieItem) data;
                url = NetworkUtils.getYoutubeURL(movieItem.getMovieVideoKey());
                break;
        }

    }

    @Override
    public void onLoaderReset(Loader loader) {
    }

    private void populateMovieValues(Context context, Object object, int type) {
        switch (type) {
            case ID_MOVIE_CURSOR_LOADER:
                Cursor movieCursor = (Cursor) object;
                movieBackdrop = movieCursor.getString(movieCursor.getColumnIndex(MoviesEntry.COLUMN_MOVIE_BACKDROP));
                movieBudget = movieCursor.getInt(movieCursor.getColumnIndex(MoviesEntry.COLUMN_MOVIE_BUDGET));
                movieGenres = movieCursor.getString(movieCursor.getColumnIndex(MoviesEntry.COLUMN_MOVIE_GENRES));
                movieOverview = movieCursor.getString(movieCursor.getColumnIndex(MoviesEntry.COLUMN_MOVIE_OVERVIEW));
                moviePopularity = movieCursor.getString(movieCursor.getColumnIndex(MoviesEntry.COLUMN_MOVIE_POPULARITY));
                moviePoster = movieCursor.getString(movieCursor.getColumnIndex(MoviesEntry.COLUMN_MOVIE_POSTER));
                movieReleaseDate = movieCursor.getString(movieCursor.getColumnIndex(MoviesEntry.COLUMN_MOVIE_RELEASE_DATE));
                movieRevenue = movieCursor.getInt(movieCursor.getColumnIndex(MoviesEntry.COLUMN_MOVIE_REVENUE));
                movieRuntime = movieCursor.getInt(movieCursor.getColumnIndex(MoviesEntry.COLUMN_MOVIE_RUNTIME));
                movieTitle = movieCursor.getString(movieCursor.getColumnIndex(MoviesEntry.COLUMN_MOVIE_TITLE));
                movieVoteAverage = movieCursor.getString(movieCursor.getColumnIndex(MoviesEntry.COLUMN_MOVIE_VOTE_AVERAGE));
                movieDirector = movieCursor.getString(movieCursor.getColumnIndex(MoviesEntry.COLUMN_MOVIE_DIRECTOR));
                break;
            case ID_MOVIE_LOADER:
                MovieItem movieItem = (MovieItem) object;
                movieBackdrop = movieItem.getMovieBackdrop();
                movieBudget = movieItem.getMovieBudget();
                movieGenresList = movieItem.getMovieGenres();
                StringBuilder genresBuilder = new StringBuilder();
                if (movieGenresList != null) {
                    for (String s : movieGenresList) {
                        genresBuilder.append(s).append(", ");
                    }
                    movieGenres = genresBuilder.toString();
                    movieGenres = movieGenres.substring(0, movieGenres.length() - 2);
                }
                movieOverview = movieItem.getMovieOverview();
                moviePopularity = String.valueOf(Math.round(Float.parseFloat(movieItem.getMoviePopularity())));
                moviePoster = movieItem.getMoviePoster();
                movieReleaseDate = movieItem.getMovieReleaseDate();
                movieRevenue = movieItem.getMovieRevenue();
                movieRuntime = movieItem.getMovieRuntime();
                movieTitle = movieItem.getMovieTitle();
                movieVoteAverage = movieItem.getMovieVoteAverage();
                movieDirector = String.format("%s %s", context.getResources().getString(R.string.by), movieItem.getMovieDirector());
                break;
        }

        //02 Backdrop
        Picasso.with(context)
                .load(movieBackdrop)
                .into(binding.detailActivityBackdrop);
        binding.detailActivityFrameGradient.setVisibility(View.VISIBLE);

        binding.detailActivityPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(youtubeIntent);
            }
        });
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(movieTitle);
        }

        //03 Budget
        binding.detailActivityBudget.setText(String.valueOf(movieBudget));

        //04 Genres
        if (movieGenres != null) {
            binding.detailActivityGenres.setText(movieGenres);
        } else {
            binding.detailActivityGenres.setVisibility(View.GONE);
        }

        //05 Overview
        binding.detailActivityOverview.setText(movieOverview);

        //06 Popularity
        binding.detailActivityPopularity.setText(moviePopularity);

        //07 Poster
        Picasso.with(context)
                .load(moviePoster)
                .into(binding.detailActivityPoster);

        //08 Release Date
        binding.detailActivityReleaseDate.setText(movieReleaseDate);

        //09 Revenue
        binding.detailActivityRevenue.setText(String.valueOf(movieRevenue));

        //10 Runtime
        StringBuilder builder = new StringBuilder();
        builder.append(movieRuntime)
                .append(" ")
                .append(getResources().getString(R.string.mins));
        binding.detailActivityRuntime.setText(builder.toString());

        //11 Title
        binding.detailActivityCollapsingToolbar.setTitle(movieTitle);

        //12 Vote Average
        binding.detailActivityVoteAverage.setText(movieVoteAverage);

        //15 Director
        binding.detailActivityDirector.setText(movieDirector);

        //17 Awards
        binding.detailActivityAwards.setText(movieAwards);

    }

    private void populateCastValues(Object object, int type) {
        switch (type) {
            case ID_CAST_CURSOR_LOADER:
                Cursor castCursor = (Cursor) object;
                int castType;
                String castName;
                String castSubtitle;
                String castProfile;
                castMembers = new ArrayList<>();
                crewMembers = new ArrayList<>();
                for (int i = 0; i < castCursor.getCount(); i++) {
                    castCursor.moveToPosition(i);
                    castType = castCursor.getInt(castCursor.getColumnIndex(CastEntry.COLUMN_CAST_TYPE));
                    castName = castCursor.getString(castCursor.getColumnIndex(CastEntry.COLUMN_CAST_NAME));
                    castSubtitle = castCursor.getString(castCursor.getColumnIndex(CastEntry.COLUMN_CAST_SUBTITLE));
                    castProfile = castCursor.getString(castCursor.getColumnIndex(CastEntry.COLUMN_CAST_PROFILE));
                    switch (castType) {
                        case ID_CAST_CURSOR_LOADER:
                            castMembers.add(new CastMember(castName, castSubtitle, castProfile));
                            break;
                        case ID_CREW_CURSOR_LOADER:
                            crewMembers.add(new CastMember(castName, castSubtitle, castProfile));
                            break;
                    }
                }
                break;
            case ID_MOVIE_LOADER:
                MovieItem movieItem = (MovieItem) object;
                castMembers = movieItem.getMovieCastMembers();
                crewMembers = movieItem.getMovieCrewMembers();
                break;
        }

        //13 Cast
        if (castMembers != null) {
            castAdapter.addAll(castMembers);
        } else {
            binding.detailActivityCastRecyclerView.setVisibility(View.GONE);
        }

        //14 Crew
        if (crewMembers != null) {
            crewAdapter.addAll(crewMembers);
        } else {
            binding.detailActivityCrewRecyclerView.setVisibility(View.GONE);
        }
    }

    private void populateReviewValues(Object object, int type) {
        switch (type) {
            case ID_REVIEW_CURSOR_LOADER:
                Cursor reviewCursor = (Cursor) object;
                String reviewAuthor;
                String reviewContent;
                reviewItems = new ArrayList<>();
                for (int i = 0; i < reviewCursor.getCount(); i++) {
                    reviewCursor.moveToPosition(i);
                    reviewAuthor = reviewCursor.getString(reviewCursor.getColumnIndex(ReviewsEntry.COLUMN_REVIEW_AUTHOR));
                    reviewContent = reviewCursor.getString(reviewCursor.getColumnIndex(ReviewsEntry.COLUMN_REVIEW_CONTENT));
                    reviewItems.add(new ReviewItem(reviewAuthor, reviewContent));
                }
                break;
            case ID_MOVIE_LOADER:
                MovieItem movieItem = (MovieItem) object;
                reviewItems = movieItem.getMovieReviewItems();
                break;
        }

        //16 Reviews
        if (reviewItems != null) {
            reviewAdapter.addAll(reviewItems);
        } else {
            binding.detailActivityReviewRecyclerView.setVisibility(View.GONE);
        }
    }

    private void addToDatabase(int type) {
        ContentValues contentValues = new ContentValues();
        switch (type) {
            case ID_MOVIE_CURSOR_LOADER:
                contentValues.put(MoviesEntry.COLUMN_MOVIE_ID, movieId);
                contentValues.put(MoviesEntry.COLUMN_MOVIE_BACKDROP, movieBackdrop);
                contentValues.put(MoviesEntry.COLUMN_MOVIE_BUDGET, movieBudget);
                contentValues.put(MoviesEntry.COLUMN_MOVIE_GENRES, movieGenres);
                contentValues.put(MoviesEntry.COLUMN_MOVIE_OVERVIEW, movieOverview);
                contentValues.put(MoviesEntry.COLUMN_MOVIE_POPULARITY, moviePopularity);
                contentValues.put(MoviesEntry.COLUMN_MOVIE_POSTER, moviePoster);
                contentValues.put(MoviesEntry.COLUMN_MOVIE_RELEASE_DATE, movieReleaseDate);
                contentValues.put(MoviesEntry.COLUMN_MOVIE_REVENUE, movieRevenue);
                contentValues.put(MoviesEntry.COLUMN_MOVIE_RUNTIME, movieRuntime);
                contentValues.put(MoviesEntry.COLUMN_MOVIE_TITLE, movieTitle);
                contentValues.put(MoviesEntry.COLUMN_MOVIE_VOTE_AVERAGE, movieVoteAverage);
                contentValues.put(MoviesEntry.COLUMN_MOVIE_DIRECTOR, movieDirector);
                contentValues.put(MoviesEntry.COLUMN_MOVIE_AWARDS, movieAwards);
                contentValues.put(MoviesEntry.COLUMN_MOVIE_VIDEO_URL, url);
                getContentResolver().insert(MoviesEntry.MOVIES_CONTENT_URI, contentValues);
                break;

            case ID_CAST_CURSOR_LOADER:
                String castName;
                String castSubtitle;
                String castProfile;
                if (castMembers != null) {
                    for (int i = 0; i < castMembers.size(); i++) {
                        castName = castMembers.get(i).getCastName();
                        castSubtitle = castMembers.get(i).getCastSubtitle();
                        castProfile = castMembers.get(i).getCastProfile();
                        contentValues.put(CastEntry.COLUMN_CAST_MOVIE_ID, movieId);
                        contentValues.put(CastEntry.COLUMN_CAST_TYPE, ID_CAST_CURSOR_LOADER);
                        contentValues.put(CastEntry.COLUMN_CAST_NAME, castName);
                        contentValues.put(CastEntry.COLUMN_CAST_SUBTITLE, castSubtitle);
                        contentValues.put(CastEntry.COLUMN_CAST_PROFILE, castProfile);
                        getContentResolver().insert(CastEntry.CAST_AND_CREW_CONTENT_URI, contentValues);
                    }
                }
                if (crewMembers != null) {
                    for (int j = 0; j < crewMembers.size(); j++) {
                        castName = crewMembers.get(j).getCastName();
                        castSubtitle = crewMembers.get(j).getCastSubtitle();
                        castProfile = crewMembers.get(j).getCastProfile();
                        contentValues.put(CastEntry.COLUMN_CAST_MOVIE_ID, movieId);
                        contentValues.put(CastEntry.COLUMN_CAST_TYPE, ID_CREW_CURSOR_LOADER);
                        contentValues.put(CastEntry.COLUMN_CAST_NAME, castName);
                        contentValues.put(CastEntry.COLUMN_CAST_SUBTITLE, castSubtitle);
                        contentValues.put(CastEntry.COLUMN_CAST_PROFILE, castProfile);
                        getContentResolver().insert(CastEntry.CAST_AND_CREW_CONTENT_URI, contentValues);
                    }
                }
                break;

            case ID_REVIEW_CURSOR_LOADER:
                String reviewAuthor;
                String reviewContent;
                if (reviewItems != null) {
                    for (int k = 1; k < reviewItems.size(); k++) {
                        reviewAuthor = reviewItems.get(k).getReviewAuthor();
                        reviewContent = reviewItems.get(k).getReviewContent();
                        contentValues.put(ReviewsEntry.COLUMN_REVIEW_MOVIE_ID, movieId);
                        contentValues.put(ReviewsEntry.COLUMN_REVIEW_AUTHOR, reviewAuthor);
                        contentValues.put(ReviewsEntry.COLUMN_REVIEW_CONTENT, reviewContent);
                        getContentResolver().insert(ReviewsEntry.REVIEWS_CONTENT_URI, contentValues);
                    }
                }
                break;
        }
    }

    private void addFavorite() {
        binding.detailActivityFavoriteView.setImageResource(R.drawable.ic_favorite_full);
        IS_FAVORITE = true;
    }

    private void removeFavorite() {
        binding.detailActivityFavoriteView.setImageResource(R.drawable.ic_favorite_empty);
        IS_FAVORITE = false;
    }
}
