package com.example.victor.bestpictures;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.LoaderManager;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.victor.bestpictures.Adapters.MovieAdapter;
import com.example.victor.bestpictures.Data.BestPicturesPreferences;
import com.example.victor.bestpictures.Model.MovieItem;
import com.example.victor.bestpictures.Model.RecyclerViewSpacing;
import com.example.victor.bestpictures.Utilities.NetworkUtils;
import com.example.victor.bestpictures.Utilities.TmdbJasonUtils;
import com.example.victor.bestpictures.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<MovieItem>>,
        MovieAdapter.MovieAdapterOnClickHandler {

    private final static String LOG_TAG = MainActivity.class.getSimpleName();

    private final static int ID_MOVIE_LOADER = 0;
    private RecyclerView discoverRV = null;
    private ImageView discoverLogo = null;
    private AppBarLayout discoverAppBarLayout = null;
    private ProgressBar discoverRvProgressBar;
    private int spanCount, spacing;
    public static int pageCount = 1;
    private boolean isLoading = false;

    private MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        discoverRvProgressBar = binding.discoverRvProgressBar;
        discoverRvProgressBar.setVisibility(View.GONE);

        discoverAppBarLayout = binding.appBarLayout;
        discoverLogo = binding.discoverLogo;
        discoverRV = binding.discoverRv;

        discoverRV.setVisibility(View.INVISIBLE);
        discoverAppBarLayout.setVisibility(View.INVISIBLE);
        final int columns = getResources().getInteger(R.integer.movies_columns);
        final GridLayoutManager layoutManager = new GridLayoutManager(this, columns);

        discoverRV.setLayoutManager(layoutManager);
        discoverRV.setHasFixedSize(true);
        spacing = (int) getResources().getDimension(R.dimen.margin_small);
        spanCount = layoutManager.getSpanCount();
        discoverRV.addItemDecoration(new RecyclerViewSpacing(spanCount, spacing));
        discoverRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                Log.e(LOG_TAG, "visible item count " + visibleItemCount);
                Log.e(LOG_TAG, "visible total count " + totalItemCount);
                Log.e(LOG_TAG, "firstvisibleItemPosition " + firstVisibleItemPosition);

                if (!isLoading && pageCount < TmdbJasonUtils.jsonTotalPages) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= NetworkUtils.TMDB_PAGE_SIZE_VALUE) {
                        isLoading = true;
                        discoverRvProgressBar.setVisibility(View.VISIBLE);
                        getSupportLoaderManager().restartLoader(ID_MOVIE_LOADER, null, MainActivity.this);
                        pageCount++;
                        Log.e(LOG_TAG, "page count " + pageCount);
                    }
                }
            }
        });

        movieAdapter = new MovieAdapter(this, this);
        discoverRV.setAdapter(movieAdapter);

        binding.discoverSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSupportLoaderManager().restartLoader(ID_MOVIE_LOADER, null, MainActivity.this);
                binding.discoverSwipeRefreshLayout.setRefreshing(false);
            }
        });

        getSupportLoaderManager().initLoader(ID_MOVIE_LOADER, null, this);

    }

    @Override
    public Loader<List<MovieItem>> onCreateLoader(int id, Bundle args) {
        return new MovieLoaderList(this);
    }

    @Override
    public void onLoadFinished(Loader<List<MovieItem>> loader, List<MovieItem> data) {
        movieAdapter.addAll(data);
        discoverRvProgressBar.setVisibility(View.GONE);
        isLoading = false;
        discoverLogo.setVisibility(View.GONE);
        discoverAppBarLayout.setVisibility(View.VISIBLE);
        discoverRV.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<List<MovieItem>> loader) {
        movieAdapter.addAll(null);
        discoverRvProgressBar.setVisibility(View.GONE);
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
                getSupportLoaderManager().restartLoader(ID_MOVIE_LOADER, null, this);
                break;
            case R.id.action_general_refresh:
                getSupportLoaderManager().restartLoader(ID_MOVIE_LOADER, null, this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
