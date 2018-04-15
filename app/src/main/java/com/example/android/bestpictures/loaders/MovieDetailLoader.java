package com.example.android.bestpictures.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.android.bestpictures.DetailActivity;
import com.example.android.bestpictures.utilities.NetworkUtils;
import com.example.android.bestpictures.utilities.TmdbJsonUtils;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Victor on 3/11/2018.
 */

public class MovieDetailLoader extends AsyncTaskLoader<Object> {

    private final static String LOG_TAG = MovieDetailLoader.class.getSimpleName();

    private int movieId;
    private int loaderId;

    public MovieDetailLoader(Context context, int id, int loaderId) {
        super(context);
        this.movieId = id;
        this.loaderId = loaderId;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public Object loadInBackground() {
        URL url;
        switch (loaderId) {
            case DetailActivity.ID_MOVIE_LOADER:
                url = NetworkUtils.getUrlForMovie(movieId);
                try {
                    String TmdbJsonResponse = NetworkUtils.makeHttpRequest(url);
                    return TmdbJsonUtils.extractDataFromTmdbJsonMovie(getContext(), TmdbJsonResponse);
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Could not make Http Request", e);
                    return null;
                }
            case DetailActivity.ID_VIDEO_LOADER:
                url = NetworkUtils.getUrlForVideo(movieId);
                try {
                    String TmdbJsonResponse = NetworkUtils.makeHttpRequest(url);
                    return TmdbJsonUtils.extractDataFromTmdbJsonVideo(TmdbJsonResponse);
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Could not make Http Request", e);
                    return null;
                }
            default:
                return null;
        }
    }
}
