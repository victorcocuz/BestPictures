package com.example.victor.bestpictures.Utilities;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.victor.bestpictures.Data.BestPicturesPreferences;
import com.example.victor.bestpictures.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by Victor on 3/11/2018.
 */

public class NetworkUtils {

    private final static String LOG_TAG = NetworkUtils.class.getSimpleName();
    private final static String TMDB_API_KEY = "5c17b6965b6565524fd0e9dd6fe2a76c";
    String credits = "https://api.themoviedb.org/3/movie/269149/credits?api_key=5c17b6965b6565524fd0e9dd6fe2a76c";
    String movie = "https://api.themoviedb.org/3/movie/269149?api_key=5c17b6965b6565524fd0e9dd6fe2a76c&language=en-US";

//    public final static int URL_REQUEST_DISCOVER = 0;
//    public final static int URL_REQUEST_IMAGE = 1;
//    public final static int URL_REQUEST_MOVIE = 2;

    // API URL Base
    private final static String TMDB_SCHEME = "https";
    private final static String TMDB_AUTHORITY_DATA = "api.themoviedb.org";
    private final static String TMDB_AUTHORITY_IMAGE = "image.tmdb.org";

    // API Paths
    private final static String TMDB_VERSION = "3";
    private final static String TMDB_PATH_1 = "t";
    private final static String TMDB_PATH_2 = "p";
    private final static String TMDB_PATH_DISCOVER = "discover";
    private final static String TMDB_PATH_FIND = "find";
    private final static String TMDB_PATH_GENRE = "genre";
    private final static String TMDB_PATH_MOVIE = "movie";
    private final static String TMDB_PATH_TV = "tv";
    private final static String TMDB_PATH_CREDITS = "credits";
    private final static String TMDB_PATH_REVIEWS = "reviews";

    // API Paths - Image Sizes
    public final static String TMDB_SIZE_BACKDROP = "w1280";
    public final static String TMDB_SIZE_POSTER_SMALL = "w342";
    public final static String TMDB_SIZE_POSTER_MEDIUM = "w500";
    public final static String TMDB_SIZE_POSTER_LARGE = "w780";
    public final static String TMDB_SIZE_PROFILE = "w185";

    // API Parameters
    private final static String TMDB_PARAM_API_KEY = "api_key";
    private final static String TMDB_PARAM_APPEND_TO_RESPONSE = "append_to_response";
    private final static String TMDB_PARAM_PAGE = "page";
    private final static String TMDB_PARAM_VOTE_COUNT_GTE = "vote_count.gte";
    private final static String TMDB_PARAM_SORT_BY = "sort_by";
    public final static String TMDB_PARAM_ID = "id";

    //Fake Parameters
    public final static String TMDB_PARAM_AWARDS = "awards";

    private final static int page = 1;
    private final static String TMDB_SORT_BY_POPULARITY_DESC = "popularity.desc";
    private final static String TMDB_SORT_BY_RATING_DESC = "vote_average.desc";
    private final static String TMDB_VOTE_COUNT_VALUE = "2500";

    public static URL getUrlForDiscover(Context context) {
        Uri.Builder builder = new Uri.Builder();
                builder.scheme(TMDB_SCHEME)
                        .authority(TMDB_AUTHORITY_DATA)
                        .appendPath(TMDB_VERSION)
                        .appendPath(TMDB_PATH_DISCOVER)
                        .appendPath(TMDB_PATH_MOVIE)
                        .appendQueryParameter(TMDB_PARAM_API_KEY, TMDB_API_KEY)
                        .appendQueryParameter(TMDB_PARAM_PAGE, String.valueOf(page))
                        .appendQueryParameter(context.getString(R.string.prev_vote_count_key), BestPicturesPreferences.getVoteCountPreference(context))
                        .appendQueryParameter(context.getString(R.string.pref_sort_by_key), BestPicturesPreferences.getSortByPreference(context))
                        .build();
        return buildUrl(builder.toString());
    }

    public static URL getUrlForMovie(int id) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(TMDB_SCHEME)
                .authority(TMDB_AUTHORITY_DATA)
                .appendPath(TMDB_VERSION)
                .appendPath(TMDB_PATH_MOVIE)
                .appendPath(String.valueOf(id))
                .appendQueryParameter(TMDB_PARAM_API_KEY, TMDB_API_KEY)
                .appendQueryParameter(TMDB_PARAM_APPEND_TO_RESPONSE, TMDB_PATH_CREDITS + "," + TMDB_PATH_REVIEWS)
                .build();
        Log.e(LOG_TAG, "hsddsalka " + builder.toString());
        return buildUrl(builder.toString());
    }

    public static URL buildUrl(String uri) {
        try {
            URL tmdbQueryUrl = new URL(uri);
            return tmdbQueryUrl;
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Could not create URL from String ", e);
        }
        return null;
    }

    public static String getStringForImageUrl(String imagePath, String imageSize) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(TMDB_SCHEME)
                .authority(TMDB_AUTHORITY_IMAGE)
                .appendPath(TMDB_PATH_1)
                .appendPath(TMDB_PATH_2)
                .appendPath(imageSize)
                .appendPath(imagePath)
                .build();
        return builder.toString();
    }

    public static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setRequestMethod("GET");

            if (httpURLConnection.getResponseCode() == 200) {
                inputStream = httpURLConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Request Code error " + httpURLConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Could not retrieve JSON results ", e);
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    public static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }
}
