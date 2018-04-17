package com.example.android.bestpictures;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.android.bestpictures.adapters.MovieAdapter;
import com.example.android.bestpictures.data.BestPicturesContract.MoviesEntry;
import com.example.android.bestpictures.data.BestPicturesPreferences;
import com.example.android.bestpictures.databinding.ActivityMainBinding;
import com.example.android.bestpictures.loaders.MovieLoaderList;
import com.example.android.bestpictures.objects.MovieItem;
import com.example.android.bestpictures.utilities.NetworkUtils;
import com.example.android.bestpictures.utilities.RecyclerViewSpacing;
import com.example.android.bestpictures.utilities.TmdbJsonUtils;
import com.facebook.stetho.Stetho;

import java.util.List;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks,
        MovieAdapter.MovieAdapterOnClickHandler {

    private final static String LOG_TAG = MainActivity.class.getSimpleName();
    private final static int ID_MOVIE_LIST_LOADER = 0;
    private static final String LIFECYCLE_CALLBACKS_LAYOUT_MANAGER_KEY = "KeyForLayoutManagerState";
    private static final String LIFECYCLE_CALLBACKS_PAGE_COUNT = "KeyForPageCountState";
    public static int pageCount = 1;
    private final String[] projection = new String[]{
            MoviesEntry.COLUMN_MOVIE_ID,
            MoviesEntry.COLUMN_MOVIE_TITLE,
            MoviesEntry.COLUMN_MOVIE_AWARDS,
            MoviesEntry.COLUMN_MOVIE_POSTER,
            MoviesEntry.COLUMN_MOVIE_VOTE_AVERAGE};
    Parcelable savedLayoutManagerState;
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;
    private RecyclerView discoverRV = null;
    private ImageView discoverLogo = null;
    private ActionBar discoverActionBarLayout = null;
    private ProgressBar discoverRvProgressBar;
    private boolean isLoading = false;
    private MovieAdapter movieAdapter;
    //    private int lastFirstVisiblePosition;
    private GridLayoutManager layoutManager;

    public static void resetPageCount() {
        pageCount = 1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        final ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        discoverRvProgressBar = binding.discoverRvProgressBar;
        discoverRvProgressBar.setVisibility(View.GONE);
        discoverActionBarLayout = getSupportActionBar();
        if (discoverActionBarLayout != null) {
            discoverActionBarLayout.hide();
        }
        discoverActionBarLayout.setTitle(BestPicturesPreferences.getSortByPreference(getApplicationContext()));
        discoverLogo = binding.discoverLogo;

        //RecyclerView
        discoverRV = binding.discoverRv;
        discoverRV.setVisibility(View.INVISIBLE);
        final int columns = getResources().getInteger(R.integer.movies_columns);
        layoutManager = new GridLayoutManager(this, columns);
        discoverRV.setLayoutManager(layoutManager);
        discoverRV.setHasFixedSize(true);
        int spacing = (int) getResources().getDimension(R.dimen.margin_small);
        int spanCount = layoutManager.getSpanCount();
        discoverRV.addItemDecoration(new RecyclerViewSpacing(spanCount, spacing));
        discoverRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading && pageCount < TmdbJsonUtils.jsonTotalPages) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= NetworkUtils.TMDB_PAGE_SIZE_VALUE) {
                        isLoading = true;
                        discoverRvProgressBar.setVisibility(View.VISIBLE);
                        getSupportLoaderManager().restartLoader(ID_MOVIE_LIST_LOADER, null, MainActivity.this);
                        pageCount++;
                    }
                }
            }
        });
        movieAdapter = new MovieAdapter(this, this);
        discoverRV.setAdapter(movieAdapter);
        binding.discoverSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSupportLoaderManager().restartLoader(ID_MOVIE_LIST_LOADER, null, MainActivity.this);
                binding.discoverSwipeRefreshLayout.setRefreshing(false);
            }
        });

        //Shared Preferences Listener
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                movieAdapter.clearAll();
                discoverActionBarLayout.setTitle(BestPicturesPreferences.getSortByPreference(getApplicationContext()));
                getSupportLoaderManager().restartLoader(ID_MOVIE_LIST_LOADER, null, MainActivity.this);
            }
        };
        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener);

        //Loader Initialised
        getSupportLoaderManager().initLoader(ID_MOVIE_LIST_LOADER, null, this);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        lastFirstVisiblePosition = ((GridLayoutManager) discoverRV.getLayoutManager()).findFirstVisibleItemPosition();
        outState.putInt(LIFECYCLE_CALLBACKS_PAGE_COUNT, pageCount);
        outState.putParcelable(LIFECYCLE_CALLBACKS_LAYOUT_MANAGER_KEY, layoutManager.onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
//            (discoverRV.getLayoutManager()).scrollToPosition(lastFirstVisiblePosition);
            pageCount = savedInstanceState.getInt(LIFECYCLE_CALLBACKS_PAGE_COUNT);
            savedLayoutManagerState = savedInstanceState.getParcelable(LIFECYCLE_CALLBACKS_LAYOUT_MANAGER_KEY);
            discoverRV.getLayoutManager().onRestoreInstanceState(savedLayoutManagerState);
        }
    }

    @NonNull
    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        if (BestPicturesPreferences.getSortByPreference(this).equals(getString(R.string.pref_sort_by_favorite))) {
            return new CursorLoader(this, MoviesEntry.MOVIES_CONTENT_URI, projection, null, null, null);
        } else {
            return new MovieLoaderList(this);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader loader, Object data) {
        if (data instanceof Cursor) {
            Cursor cursor = (Cursor) data;
            movieAdapter.appendAll(cursor);
        } else {
            //noinspection unchecked
            List<MovieItem> movieItems = (List<MovieItem>) data;
            movieAdapter.appendAll(movieItems);
            discoverRV.getLayoutManager().onRestoreInstanceState(savedLayoutManagerState);
        }
        discoverRvProgressBar.setVisibility(View.GONE);
        isLoading = false;
        discoverLogo.setVisibility(View.GONE);
        discoverActionBarLayout.show();
        discoverRV.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {
        movieAdapter.appendAll(null);
        discoverRvProgressBar.setVisibility(View.GONE);
        resetPageCount();
        isLoading = false;
    }

    @Override
    public void onClick(int id, String awards) {
        Intent goToDetailActivity = new Intent(MainActivity.this, DetailActivity.class);
        goToDetailActivity.putExtra(NetworkUtils.TMDB_PARAM_ID, id);
        goToDetailActivity.putExtra(NetworkUtils.TMDB_PARAM_AWARDS, awards);
        startActivity(goToDetailActivity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_general, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_general_settings:
                Intent goToSettingsActivity = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(goToSettingsActivity);
                break;
            case R.id.action_general_reset_vote_count:
                BestPicturesPreferences.resetVoteCountPreference(this);
                getSupportLoaderManager().restartLoader(ID_MOVIE_LIST_LOADER, null, this);
                break;
            case R.id.action_general_refresh:
                getSupportLoaderManager().restartLoader(ID_MOVIE_LIST_LOADER, null, this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
