package com.example.android.bestpictures.utilities;

import android.content.Context;
import android.util.Log;

import com.example.android.bestpictures.objects.CastMember;
import com.example.android.bestpictures.objects.MovieItem;
import com.example.android.bestpictures.objects.ReviewItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor on 3/10/2018.
 */

public class TmdbJsonUtils {

    private final static String LOG_TAG = TmdbJsonUtils.class.getSimpleName();

    private final static String TMDB_RESULTS = "results";
    private final static String TMDB_ID = "id";
    //General
    private final static String TMDB_BACKDROP_PATH = "backdrop_path";
    private final static String TMDB_BUDGET = "budget";
    private final static String TMDB_GENRES = "genres";
    private final static String TMDB_GENRES_NAME = "name";
    private final static String TMDB_OVERVIEW = "overview";
    private final static String TMDB_POPULARITY = "popularity";
    private final static String TMDB_POSTER_PATH = "poster_path";
    private final static String TMDB_RELEASE_DATE = "release_date";
    private final static String TMDB_REVENUE = "revenue";
    private final static String TMDB_RUNTIME = "runtime";
    private final static String TMDB_TITLE = "title";
    private final static String TMDB_VOTE_AVERAGE = "vote_average";
    private final static String TMDB_TOTAL_PAGES = "total_pages";
    //Cast
    private final static String TMDB_CREDITS = "credits";
    private final static String TMDB_CAST_ARRAY = "cast";
    private final static String TMDB_CAST_NAME = "name";
    private final static String TMDB_CAST_CHARACTER = "character";
    private final static String TMDB_CAST_PROFILE_PATH = "profile_path";
    //Crew
    private final static String TMDB_CREW_ARRAY = "crew";
    private final static String TMDB_CREW_NAME = "name";
    private final static String TMDB_CREW_JOB = "job";
    private final static String TMDB_CREW_JOB_DIRECTOR = "Director";
    private final static String TMDB_CREW_JOB_WRITER = "Screenplay";
    private final static String TMDB_CREW_JOB_PRODUCER = "Producer";
    private final static String TMDB_CREW_PROFILE_PATH = "profile_path";
    //Reviews
    private final static String TMDB_REVIEWS = "reviews";
    private final static String TMDB_REVIEWS_AUTHOR = "author";
    private final static String TMDB_REVIEWS_CONTENT = "content";
    //Videos
    private final static String TMDB_VIDEOS_KEY = "key";
    public static int jsonTotalPages = 0;

    private TmdbJsonUtils() {
        //Empty constructor
    }

    public static List<MovieItem> extractDataFromTmdbJsonDiscover(Context context, String tmdbJsonResponse) {
        List<MovieItem> movieList = new ArrayList<>();

        try {
            JSONObject jsonResponse = new JSONObject(tmdbJsonResponse);
            JSONArray jsonResults = jsonResponse.getJSONArray(TMDB_RESULTS);

            for (int i = 0; i < jsonResults.length(); i++) {
                JSONObject jsonMovie = jsonResults.getJSONObject(i);

                int jsonID = jsonMovie.optInt(TMDB_ID);

                //01 Poster
                String jsonPoster = null;
                String jsonPosterPath = jsonMovie.optString(TMDB_POSTER_PATH);
                if (jsonPosterPath != null) {
                    jsonPoster = NetworkUtils.getStringForImageUrl(jsonPosterPath.substring(1), NetworkUtils.TMDB_SIZE_POSTER_SMALL);
                }

                //02 Title
                String jsonTitle = jsonMovie.optString(TMDB_TITLE);

                //03 Vote Average
                String jsonVoteAverage = jsonMovie.optString(TMDB_VOTE_AVERAGE);

                //04 Awards
                String awards = BestPicturesUtilities.randomizeAwards(context);

                movieList.add(new MovieItem(jsonID,
                        null,
                        0,
                        null,
                        null,
                        null,
                        jsonPoster,
                        null,
                        0,
                        0,
                        jsonTitle,
                        jsonVoteAverage,
                        null,
                        null,
                        null,
                        null,
                        awards,
                        null
                ));
            }

            //Total Pages
            jsonTotalPages = jsonResponse.optInt(TMDB_TOTAL_PAGES);

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error parsing JSON results", e);
        }
        return movieList;
    }

    public static Object extractDataFromTmdbJsonMovie(Context context, String tmdbJsonResponse) {
        MovieItem movieItem;

        try {
            JSONObject jsonResponse = new JSONObject(tmdbJsonResponse);

            //01 ID
            int jsonID = jsonResponse.getInt(TMDB_ID);

            //02 Backdrop
            String jsonBackdrop = null;
            String jsonBackdropPath = jsonResponse.optString(TMDB_BACKDROP_PATH);
            if (jsonBackdropPath != null) {
                jsonBackdrop = NetworkUtils.getStringForImageUrl(jsonBackdropPath.substring(1), NetworkUtils.TMDB_SIZE_BACKDROP);
            }

            //03 Budget
            int jsonBudget = jsonResponse.optInt(TMDB_BUDGET);

            //04 Genres
            List<String> jsonGenres = new ArrayList<>();
            JSONArray jsonGenresArray = jsonResponse.optJSONArray(TMDB_GENRES);
            if (jsonGenresArray != null) {
                for (int i = 0; i < Math.min(jsonGenresArray.length(), 2); i++) {
                    JSONObject jsonGenre = jsonGenresArray.getJSONObject(i);
                    jsonGenres.add(jsonGenre.optString(TMDB_GENRES_NAME));
                }
            }

            //05 Overview
            String jsonOverview = jsonResponse.optString(TMDB_OVERVIEW);

            //06 Popularity
            String jsonPopularity = jsonResponse.optString(TMDB_POPULARITY);

            //07 Poster
            String jsonPoster = null;
            String jsonPosterPath = jsonResponse.optString(TMDB_POSTER_PATH);
            if (jsonPosterPath != null) {
                jsonPoster = NetworkUtils.getStringForImageUrl(jsonPosterPath.substring(1), NetworkUtils.TMDB_SIZE_POSTER_SMALL);
            }

            //08 Release Date
            String jsonReleaseDate = jsonResponse.optString(TMDB_RELEASE_DATE);

            //09 Revenue
            int jsonRevenue = jsonResponse.optInt(TMDB_REVENUE);

            //10 Runtime
            int jsonRuntime = jsonResponse.optInt(TMDB_RUNTIME);

            //11 Title
            String jsonTitle = jsonResponse.optString(TMDB_TITLE);

            //12 Vote Average
            String jsonVoteAverage = jsonResponse.optString(TMDB_VOTE_AVERAGE);

            //13 Cast
            List<CastMember> jsonCastMembers = new ArrayList<>();
            JSONObject jsonCredits = jsonResponse.optJSONObject(TMDB_CREDITS);
            JSONArray jsonCastArray = jsonCredits.optJSONArray(TMDB_CAST_ARRAY);
            if (jsonCastArray.length() > 0) {
                for (int i = 0; i < Math.min(jsonCastArray.length(), 10); i++) {
                    JSONObject jsonCast = jsonCastArray.optJSONObject(i);
                    String jsonCastName = jsonCast.optString(TMDB_CAST_NAME);
                    String jsonCastCharacter = jsonCast.optString(TMDB_CAST_CHARACTER);

                    String jsonCastProfilePath = jsonCast.optString(TMDB_CAST_PROFILE_PATH);
                    String jsonCastProfile = null;
                    if (jsonCastProfilePath != null) {
                        jsonCastProfile = NetworkUtils.getStringForImageUrl(jsonCastProfilePath.substring(1), NetworkUtils.TMDB_SIZE_PROFILE);
                    }
                    jsonCastMembers.add(new CastMember(jsonCastName, jsonCastCharacter, jsonCastProfile));
                }
            }

            //14 Crew
            List<CastMember> jsonCrewMembers = new ArrayList<>();
            JSONArray jsonCrewArray = jsonCredits.optJSONArray(TMDB_CREW_ARRAY);
            String jsonCrewDirector = null;
            if (jsonCrewArray.length() > 0) {
                for (int i = 0; i < jsonCrewArray.length(); i++) {
                    JSONObject jsonCrew = jsonCrewArray.optJSONObject(i);
                    String jsonCrewJob = jsonCrew.optString(TMDB_CREW_JOB);
                    if (!(jsonCrewJob.equals(TMDB_CREW_JOB_DIRECTOR) || jsonCrewJob.equals(TMDB_CREW_JOB_WRITER) || jsonCrewJob.equals(TMDB_CREW_JOB_PRODUCER)))
                        continue;
                    String jsonCrewName = jsonCrew.optString(TMDB_CREW_NAME);

                    //15 Director
                    if (jsonCrewJob.equals(TMDB_CREW_JOB_DIRECTOR)) {
                        jsonCrewDirector = jsonCrew.optString(TMDB_CREW_NAME);
                    }

                    String jsonCrewProfilePath = jsonCrew.optString(TMDB_CREW_PROFILE_PATH);
                    if (jsonCrewName == null || jsonCrewProfilePath == null)
                        continue;

                    String jsonCrewProfile = NetworkUtils.getStringForImageUrl(jsonCrewProfilePath.substring(1), NetworkUtils.TMDB_SIZE_PROFILE);

                    jsonCrewMembers.add(new CastMember(jsonCrewName, jsonCrewJob, jsonCrewProfile));
                }
            } else {
                jsonCrewMembers = null;
            }

            //16 Reviews
            List<ReviewItem> jsonReviewItems = new ArrayList<>();
            JSONObject jsonReviews = jsonResponse.optJSONObject(TMDB_REVIEWS);
            JSONArray jsonReviewsResultsArray = jsonReviews.optJSONArray(TMDB_RESULTS);
            if (jsonReviewsResultsArray.length() > 0) {
                for (int i = 0; i < Math.min(jsonReviewsResultsArray.length(), 5); i++) {
                    JSONObject jsonReview = jsonReviewsResultsArray.getJSONObject(i);
                    String jsonReviewAuthor = jsonReview.optString(TMDB_REVIEWS_AUTHOR);
                    String jsonReviewContent = jsonReview.optString(TMDB_REVIEWS_CONTENT);
                    jsonReviewItems.add(new ReviewItem(jsonReviewAuthor, jsonReviewContent));
                }
            } else {
                jsonReviewItems = null;
            }

            //17 Awards
            String awards = BestPicturesUtilities.randomizeAwards(context);

            movieItem = new MovieItem(jsonID,
                    jsonBackdrop,
                    jsonBudget,
                    jsonGenres,
                    jsonOverview,
                    jsonPopularity,
                    jsonPoster,
                    jsonReleaseDate,
                    jsonRevenue,
                    jsonRuntime,
                    jsonTitle,
                    jsonVoteAverage,
                    jsonCastMembers,
                    jsonCrewMembers,
                    jsonCrewDirector,
                    jsonReviewItems,
                    awards,
                    null);

            return movieItem;

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error parsing JSON results", e);
        }
        return null;
    }

    public static Object extractDataFromTmdbJsonVideo(String tmdbJsonResponse) {
        if (tmdbJsonResponse == null)
            return null;

        MovieItem movieItem;
        try {
            JSONObject jsonResponse = new JSONObject(tmdbJsonResponse);
            JSONArray jsonResults;
            String jsonKey = null;
            if (jsonResponse.getJSONArray(TMDB_RESULTS).length() > 0) {
                jsonResults = jsonResponse.getJSONArray(TMDB_RESULTS);
                JSONObject jsonObject = jsonResults.optJSONObject(0);
                jsonKey = jsonObject.optString(TMDB_VIDEOS_KEY);
            }

            movieItem = new MovieItem(0,
                    null,
                    0,
                    null,
                    null,
                    null,
                    null,
                    null,
                    0,
                    0,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    jsonKey);
            return movieItem;
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error parsing JSON results", e);
        }
        return null;
    }
}
