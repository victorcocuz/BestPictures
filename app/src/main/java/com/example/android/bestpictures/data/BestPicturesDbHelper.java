package com.example.android.bestpictures.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.bestpictures.data.BestPicturesContract.CastEntry;
import com.example.android.bestpictures.data.BestPicturesContract.MoviesEntry;
import com.example.android.bestpictures.data.BestPicturesContract.ReviewsEntry;

/**
 * Created by Victor on 4/7/2018.
 */

public class BestPicturesDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;

    public BestPicturesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIES_TABLE =
                "CREATE TABLE " + MoviesEntry.MOVIES_TABLE_NAME + " (" +
                        MoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        MoviesEntry.COLUMN_MOVIE_ID + " INTEGER, " +
                        MoviesEntry.COLUMN_MOVIE_BACKDROP + " TEXT, " +
                        MoviesEntry.COLUMN_MOVIE_BUDGET + " INTEGER, " +
                        MoviesEntry.COLUMN_MOVIE_GENRES + " TEXT, " +
                        MoviesEntry.COLUMN_MOVIE_OVERVIEW + " TEXT, " +
                        MoviesEntry.COLUMN_MOVIE_POPULARITY + " TEXT, " +
                        MoviesEntry.COLUMN_MOVIE_POSTER + " TEXT, " +
                        MoviesEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT, " +
                        MoviesEntry.COLUMN_MOVIE_REVENUE + " INTEGER, " +
                        MoviesEntry.COLUMN_MOVIE_RUNTIME + " INTEGER, " +
                        MoviesEntry.COLUMN_MOVIE_TITLE + " TEXT, " +
                        MoviesEntry.COLUMN_MOVIE_VOTE_AVERAGE + " TEXT, " +
                        MoviesEntry.COLUMN_MOVIE_DIRECTOR + " TEXT, " +
                        MoviesEntry.COLUMN_MOVIE_AWARDS + " TEXT, " +
                        MoviesEntry.COLUMN_MOVIE_VIDEO_URL + " TEXT);";
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);

        final String SQL_CREATE_CAST_TABLE =
                "CREATE TABLE " + CastEntry.CAST_TABLE_NAME + " (" +
                        CastEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        CastEntry.COLUMN_CAST_MOVIE_ID + " INTEGER, " +
                        CastEntry.COLUMN_CAST_TYPE + " INTEGER, " +
                        CastEntry.COLUMN_CAST_NAME + " TEXT, " +
                        CastEntry.COLUMN_CAST_SUBTITLE + " TEXT, " +
                        CastEntry.COLUMN_CAST_PROFILE + " TEXT);";
        sqLiteDatabase.execSQL(SQL_CREATE_CAST_TABLE);

        final String SQL_CREATE_REVIEWS_TABLE =
                "CREATE TABLE " + ReviewsEntry.REVIEWS_TABLE_NAME + " (" +
                        ReviewsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        ReviewsEntry.COLUMN_REVIEW_MOVIE_ID + " INTEGER, " +
                        ReviewsEntry.COLUMN_REVIEW_AUTHOR + " TEXT, " +
                        ReviewsEntry.COLUMN_REVIEW_CONTENT + " TEXT);";
        sqLiteDatabase.execSQL(SQL_CREATE_REVIEWS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BestPicturesContract.MoviesEntry.MOVIES_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
