package com.example.android.bestpictures.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.android.bestpictures.MainActivity;
import com.example.android.bestpictures.R;
import com.example.android.bestpictures.data.BestPicturesPreferences;
import com.example.android.bestpictures.utilities.NetworkUtils;
import com.example.android.bestpictures.utilities.TmdbJsonUtils;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Victor on 3/10/2018.
 */

public class MovieLoaderList extends AsyncTaskLoader<Object> {

    private final static String LOG_TAG = MovieLoaderList.class.getSimpleName();

    public MovieLoaderList(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public Object loadInBackground() {
        URL url;
        if (!BestPicturesPreferences.getSortByPreference(getContext()).equals(getContext().getString(R.string.pref_sort_by_favorite))) {
            url = NetworkUtils.getUrlForSorting(getContext(), MainActivity.pageCount);
            try {
                String tmdbJsonResponse = NetworkUtils.makeHttpRequest(url);
                return TmdbJsonUtils.extractDataFromTmdbJsonDiscover(getContext(), tmdbJsonResponse);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Could not make Http Request", e);
                return null;
            }
        }
        return null;
    }
}
