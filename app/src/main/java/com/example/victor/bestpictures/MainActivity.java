package com.example.victor.bestpictures;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.Preference;
import android.support.v7.widget.GridLayoutManager;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.victor.bestpictures.Adapters.MovieAdapter;
import com.example.victor.bestpictures.Data.BestPicturesPreferences;
import com.example.victor.bestpictures.Model.MovieItem;
import com.example.victor.bestpictures.Model.RecyclerViewSpacing;
import com.example.victor.bestpictures.Utilities.NetworkUtils;
import com.example.victor.bestpictures.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<MovieItem>>,
        MovieAdapter.MovieAdapterOnClickHandler {

    private final static String LOG_TAG = MainActivity.class.getSimpleName();
    private final static int ID_MOVIE_LOADER = 0;
    private final static int GRID_VIEW_SPAN = 2;
    RecyclerView discoverRV = null;
    int spanCount, spacing;

    private MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        discoverRV = binding.discoverRv;
        GridLayoutManager layoutManager = new GridLayoutManager(this, GRID_VIEW_SPAN);
        discoverRV.setLayoutManager(layoutManager);
        discoverRV.setHasFixedSize(true);
        spacing = (int) getResources().getDimension(R.dimen.margin_small);
        spanCount = layoutManager.getSpanCount();
        discoverRV.addItemDecoration(new RecyclerViewSpacing(spanCount, spacing));

        movieAdapter = new MovieAdapter(this, this);
        discoverRV.setAdapter(movieAdapter);

        getSupportLoaderManager().initLoader(ID_MOVIE_LOADER, null, this);

    }

    @Override
    public Loader<List<MovieItem>> onCreateLoader(int id, Bundle args) {
        return new MovieLoaderList(this);
    }

    @Override
    public void onLoadFinished(Loader<List<MovieItem>> loader, List<MovieItem> data) {
        movieAdapter.addAll(data);
    }

    @Override
    public void onLoaderReset(Loader<List<MovieItem>> loader) {
        movieAdapter.addAll(null);
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
        }
        return super.onOptionsItemSelected(item);
    }
}
