package com.example.victor.bestpictures;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.example.victor.bestpictures.Model.MovieItem;
import com.example.victor.bestpictures.Utilities.NetworkUtils;
import com.example.victor.bestpictures.Utilities.TmdbJasonUtils;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Victor on 3/11/2018.
 */

public class MovieLoaderDetail extends AsyncTaskLoader<MovieItem> {

    private final static String LOG_TAG = MovieLoaderDetail.class.getSimpleName();

    private int id;

    public MovieLoaderDetail(Context context, int id) {
        super(context);
        this.id = id;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public MovieItem loadInBackground() {
        URL url = NetworkUtils.getUrlForMovie(id);

        try {
            String TmdbJsonResponse = NetworkUtils.makeHttpRequest(url);
            MovieItem movieItem = TmdbJasonUtils.extractDataFromTmdbJsonMovie(TmdbJsonResponse);
            return movieItem;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Could not make Http Request", e);
            return null;
        }
    }
}
