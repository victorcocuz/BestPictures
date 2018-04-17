package com.example.android.bestpictures.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/******
 * Created by Victor on 4/7/2018.
 ******/

public class BestPicturesContract {
    public static final String BP_CONTENT_AUTHORITY = "com.example.android.bestpictures";
    public final static String BP_PATH_MOVIES = "movies";
    public final static String BP_PATH_CAST = "cast";
    public final static String BP_PATH_REVIEWS = "reviews";
    public static final String BP_CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + BP_CONTENT_AUTHORITY + "/" + BP_PATH_MOVIES;
    public static final String BP_CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + BP_CONTENT_AUTHORITY + "/" + BP_PATH_MOVIES;
    private static final String BP_SCHEME = "content://";
    private static final Uri BP_BASE_CONTENT_URI = Uri.parse(BP_SCHEME + BP_CONTENT_AUTHORITY);

    public static final class MoviesEntry implements BaseColumns {
        public static final Uri MOVIES_CONTENT_URI = BP_BASE_CONTENT_URI.buildUpon()
                .appendPath(BP_PATH_MOVIES)
                .build();

        public static final String MOVIES_TABLE_NAME = "random";
        public static final String COLUMN_MOVIE_ID = "movieId";
        public static final String COLUMN_MOVIE_BACKDROP = "backdrop";
        public static final String COLUMN_MOVIE_BUDGET = "budget";
        public static final String COLUMN_MOVIE_GENRES = "genres";
        public static final String COLUMN_MOVIE_OVERVIEW = "overview";
        public static final String COLUMN_MOVIE_POPULARITY = "popularity";
        public static final String COLUMN_MOVIE_POSTER = "poster";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_MOVIE_REVENUE = "revenue";
        public static final String COLUMN_MOVIE_RUNTIME = "runtime";
        public static final String COLUMN_MOVIE_TITLE = "title";
        public static final String COLUMN_MOVIE_VOTE_AVERAGE = "voteAverage";
        public static final String COLUMN_MOVIE_DIRECTOR = "director";
        public static final String COLUMN_MOVIE_AWARDS = "awards";
        public static final String COLUMN_MOVIE_VIDEO_URL = "videoKey";
    }

    public static final class CastEntry implements BaseColumns {
        public static final Uri CAST_AND_CREW_CONTENT_URI = BP_BASE_CONTENT_URI.buildUpon()
                .appendPath(BP_PATH_CAST)
                .build();

        public static final String CAST_TABLE_NAME = "cast";
        public static final String COLUMN_CAST_MOVIE_ID = "movieId";
        public static final String COLUMN_CAST_TYPE = "type";
        public static final String COLUMN_CAST_NAME = "name";
        public static final String COLUMN_CAST_SUBTITLE = "subtitle";
        public static final String COLUMN_CAST_PROFILE = "profile";
    }

    public static final class ReviewsEntry implements BaseColumns {
        public static final Uri REVIEWS_CONTENT_URI = BP_BASE_CONTENT_URI.buildUpon()
                .appendPath(BP_PATH_REVIEWS)
                .build();

        public static final String REVIEWS_TABLE_NAME = "reviews";
        public static final String COLUMN_REVIEW_MOVIE_ID = "movieId";
        public static final String COLUMN_REVIEW_AUTHOR = "author";
        public static final String COLUMN_REVIEW_CONTENT = "content";
    }
}
