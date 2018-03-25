package com.example.victor.bestpictures.Data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.victor.bestpictures.R;

/**
 * Created by Victor on 3/18/2018.
 */

public class BestPicturesPreferences {

    public static String getSortByPreference(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String sortByKey = context.getString(R.string.pref_sort_by_key);
        String defaultValue = context.getString(R.string.pref_sort_by_popularity_desc);
        return sharedPreferences.getString(sortByKey, defaultValue);
    }

    public static String getVoteCountPreference(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String voteCountKey = context.getString(R.string.prev_vote_count_key);
        String defaultValue = context.getString(R.string.pref_vote_count_default_value);
        return sharedPreferences.getString(voteCountKey, defaultValue);
    }

    public static void resetVoteCountPreference(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String voteCountKey = context.getString(R.string.prev_vote_count_key);
        String defaultValue = context.getString(R.string.pref_vote_count_default_value);
        editor.putString(voteCountKey, defaultValue);
        editor.apply();
    }
}
