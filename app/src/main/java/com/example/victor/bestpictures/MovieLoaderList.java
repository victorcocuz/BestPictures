package com.example.victor.bestpictures;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.example.victor.bestpictures.Model.MovieItem;
import com.example.victor.bestpictures.Utilities.NetworkUtils;
import com.example.victor.bestpictures.Utilities.TmdbJasonUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Created by Victor on 3/10/2018.
 */

public class MovieLoaderList extends AsyncTaskLoader<List<MovieItem>> {

    private final static String LOG_TAG = MovieLoaderList.class.getSimpleName();
    private Context context;

    public MovieLoaderList(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<MovieItem> loadInBackground() {
        URL url = NetworkUtils.getUrlForDiscover(context, MainActivity.pageCount);
        try {
            String tmdbJsonResponse = NetworkUtils.makeHttpRequest(url);
            List<MovieItem> movies = TmdbJasonUtils.extractDataFromTmdbJsonDiscover(tmdbJsonResponse);
            return movies;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Could not make Http Request", e);
            return null;
        }
    }
}
