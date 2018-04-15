package com.example.android.bestpictures.utilities;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.android.bestpictures.BuildConfig;
import com.example.android.bestpictures.R;
import com.example.android.bestpictures.data.BestPicturesPreferences;

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

    public final static String TMDB_SIZE_POSTER_MEDIUM = "w500";
    public final static String TMDB_SIZE_POSTER_LARGE = "w780";
    public final static String TMDB_PARAM_ID = "id";
    //Fake Parameters
    public final static String TMDB_PARAM_AWARDS = "awards";

//    public final static int URL_REQUEST_DISCOVER = 0;
//    public final static int URL_REQUEST_IMAGE = 1;
//    public final static int URL_REQUEST_MOVIE = 2;
public final static int TMDB_PAGE_SIZE_VALUE = 20;
    // API Paths - Image Sizes
    final static String TMDB_SIZE_BACKDROP = "w1280";
    final static String TMDB_SIZE_POSTER_SMALL = "w342";
    final static String TMDB_SIZE_PROFILE = "w185";
    private final static String LOG_TAG = NetworkUtils.class.getSimpleName();
    private final static String TMDB_API_KEY = BuildConfig.TMDB_API_KEY;
    // API URL Base
    private final static String HTTPS_SCHEME = "https";
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
    private final static String TMDB_PATH_VIDEOS = "videos";
    // API Parameters
    private final static String TMDB_PARAM_API_KEY = "api_key";
    private final static String TMDB_PARAM_APPEND_TO_RESPONSE = "append_to_response";
    private final static String TMDB_PARAM_PAGE = "page";
    private final static String TMDB_PARAM_VOTE_COUNT_GTE = "vote_count.gte";
    private final static String TMDB_PARAM_SORT_BY = "sort_by";
    // API Values
    private final static String TMDB_SORT_BY_POPULARITY_DESC = "popularity.desc";
    private final static String TMDB_SORT_BY_RATING_DESC = "vote_average.desc";
    private final static String TMDB_VOTE_COUNT_VALUE = "2500";
    //Youtube Parameters
    private final static String YOUTUBE_AUTHORITY = "www.youtube.com";
    private final static String YOUTUBE_PARAM_WATCH = "watch";
    private final static String YOUTUBE_PARAM_VIDEO = "v";
    String credits = "https://api.themoviedb.org/3/movie/269149/credits?api_key=5c17b6965b6565524fd0e9dd6fe2a76c";
    String movie = "https://api.themoviedb.org/3/movie/269149?api_key=5c17b6965b6565524fd0e9dd6fe2a76c&language=en-US";

    private NetworkUtils() {
        //Empty constructor
    }

    //Different method of retrieving url with sorting, based on vote count
    public static URL getUrlForDiscover(Context context, int page) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(HTTPS_SCHEME)
                        .authority(TMDB_AUTHORITY_DATA)
                        .appendPath(TMDB_VERSION)
                        .appendPath(TMDB_PATH_DISCOVER)
                        .appendPath(TMDB_PATH_MOVIE)
                        .appendQueryParameter(TMDB_PARAM_API_KEY, TMDB_API_KEY)
                        .appendQueryParameter(TMDB_PARAM_PAGE, String.valueOf(page))
                        .appendQueryParameter(context.getString(R.string.prev_vote_count_key), BestPicturesPreferences.getVoteCountPreference(context))
                        .appendQueryParameter(context.getString(R.string.pref_sort_by_key), BestPicturesPreferences.getSortByPreference(context))
                        .build();
        return buildUrl(builder);
    }

    public static URL getUrlForSorting(Context context, int page) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(HTTPS_SCHEME)
                .authority(TMDB_AUTHORITY_DATA)
                .appendPath(TMDB_VERSION)
                .appendPath(TMDB_PATH_MOVIE)
                .appendPath(BestPicturesPreferences.getSortByPreference(context))
                .appendQueryParameter(TMDB_PARAM_API_KEY, TMDB_API_KEY)
                .appendQueryParameter(TMDB_PARAM_PAGE, String.valueOf(page))
                .build();
        return buildUrl(builder);
    }

    public static URL getUrlForMovie(int id) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(HTTPS_SCHEME)
                .authority(TMDB_AUTHORITY_DATA)
                .appendPath(TMDB_VERSION)
                .appendPath(TMDB_PATH_MOVIE)
                .appendPath(String.valueOf(id))
                .appendQueryParameter(TMDB_PARAM_API_KEY, TMDB_API_KEY)
                .appendQueryParameter(TMDB_PARAM_APPEND_TO_RESPONSE, TMDB_PATH_CREDITS + "," + TMDB_PATH_REVIEWS)
                .build();
        return buildUrl(builder);
    }

    public static URL getUrlForVideo(int id) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(HTTPS_SCHEME)
                .authority(TMDB_AUTHORITY_DATA)
                .appendPath(TMDB_VERSION)
                .appendPath(TMDB_PATH_MOVIE)
                .appendPath(String.valueOf(id))
                .appendPath(TMDB_PATH_VIDEOS)
                .appendQueryParameter(TMDB_PARAM_API_KEY, TMDB_API_KEY)
                .build();
        return buildUrl(builder);
    }

    public static String getYoutubeURL(String key) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(HTTPS_SCHEME)
                .authority(YOUTUBE_AUTHORITY)
                .appendPath(YOUTUBE_PARAM_WATCH)
                .appendQueryParameter(YOUTUBE_PARAM_VIDEO, key)
                .build();
        return builder.toString();
    }

    public static URL buildUrl(Uri.Builder uri) {
        try {
            URL tmdbQueryUrl = new URL(uri.toString());
            return tmdbQueryUrl;
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Could not create URL from String ", e);
        }
        return null;
    }

    public static String getStringForImageUrl(String imagePath, String imageSize) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(HTTPS_SCHEME)
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
