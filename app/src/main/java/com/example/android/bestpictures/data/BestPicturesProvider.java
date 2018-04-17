package com.example.android.bestpictures.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.bestpictures.data.BestPicturesContract.CastEntry;
import com.example.android.bestpictures.data.BestPicturesContract.MoviesEntry;
import com.example.android.bestpictures.data.BestPicturesContract.ReviewsEntry;

/******
 * Created by Victor on 4/8/2018.
 ******/

public class BestPicturesProvider extends ContentProvider {
    private static final int CODE_MOVIES = 100;
    private static final int CODE_MOVIES_ID = 101;
    private static final int CODE_CAST = 200;
    private static final int CODE_REVIEW = 300;
    private static final String LOG_TAG = BestPicturesProvider.class.getSimpleName();
    private static final UriMatcher uriMatcher = buildUriMatcher();
    private BestPicturesDbHelper dbHelper;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(BestPicturesContract.BP_CONTENT_AUTHORITY, BestPicturesContract.BP_PATH_MOVIES, CODE_MOVIES);
        uriMatcher.addURI(BestPicturesContract.BP_CONTENT_AUTHORITY, BestPicturesContract.BP_PATH_MOVIES + "/#", CODE_MOVIES_ID);
        uriMatcher.addURI(BestPicturesContract.BP_CONTENT_AUTHORITY, BestPicturesContract.BP_PATH_CAST, CODE_CAST);
        uriMatcher.addURI(BestPicturesContract.BP_CONTENT_AUTHORITY, BestPicturesContract.BP_PATH_REVIEWS, CODE_REVIEW);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new BestPicturesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case CODE_MOVIES:
                cursor = database.query(MoviesEntry.MOVIES_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_MOVIES_ID:
                selection = MoviesEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(MoviesEntry.MOVIES_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_CAST:
                cursor = database.query(CastEntry.CAST_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_REVIEW:
                cursor = database.query(ReviewsEntry.REVIEWS_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        if (getContext() != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = uriMatcher.match(uri);
        switch (match) {
            case CODE_MOVIES:
                return BestPicturesContract.BP_CONTENT_LIST_TYPE;
            case CODE_MOVIES_ID:
                return BestPicturesContract.BP_CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        long id;

        switch (uriMatcher.match(uri)) {
            case CODE_MOVIES:
                id = database.insert(MoviesEntry.MOVIES_TABLE_NAME, null, contentValues);
                if (id == -1) {
                    Log.e(LOG_TAG, "Failed to insert row for " + uri);
                    return null;
                }
                break;
            case CODE_CAST:
                id = database.insert(CastEntry.CAST_TABLE_NAME, null, contentValues);
                if (id == -1) {
                    Log.e(LOG_TAG, "Failed to insert row for " + uri);
                    return null;
                }
                break;
            case CODE_REVIEW:
                id = database.insert(ReviewsEntry.REVIEWS_TABLE_NAME, null, contentValues);
                if (id == -1) {
                    Log.e(LOG_TAG, "Failed to insert row for " + uri);
                    return null;
                }
                break;
            default:
                throw new IllegalArgumentException("Insert is not supported for " + uri);
        }
//        if (getContext() != null) {
//            getContext().getContentResolver().notifyChange(uri, null);
//        }
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int rowsDeleted;

        switch (uriMatcher.match(uri)) {
            case CODE_MOVIES:
                rowsDeleted = database.delete(MoviesEntry.MOVIES_TABLE_NAME, selection, selectionArgs);
                break;
            case CODE_MOVIES_ID:
                selection = MoviesEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(MoviesEntry.MOVIES_TABLE_NAME, selection, selectionArgs);
                break;
            case CODE_CAST:
                rowsDeleted = database.delete(CastEntry.CAST_TABLE_NAME, selection, selectionArgs);
                break;
            case CODE_REVIEW:
                rowsDeleted = database.delete(ReviewsEntry.REVIEWS_TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Delete is not supported for " + uri);
        }

        if (rowsDeleted != 0 && getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int rowsUpdated;

        if (contentValues == null || contentValues.size() == 0) {
            return 0;
        }

        switch (uriMatcher.match(uri)) {
            case CODE_MOVIES:
                rowsUpdated = database.update(MoviesEntry.MOVIES_TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            case CODE_MOVIES_ID:
                selection = MoviesEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsUpdated = database.update(MoviesEntry.MOVIES_TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }

        if (rowsUpdated != 0 && getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        dbHelper.close();
        super.shutdown();
    }

}
