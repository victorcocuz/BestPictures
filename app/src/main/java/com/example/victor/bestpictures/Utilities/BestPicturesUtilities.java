package com.example.victor.bestpictures.Utilities;

import android.content.Context;

import com.example.victor.bestpictures.R;

import java.util.Random;

/**
 * Created by Victor on 3/21/2018.
 */

public class BestPicturesUtilities {

    public static String randomizeAwards(Context context) {
        Random random = new Random();
        String[] awardsWinsArray = context.getResources().getStringArray(R.array.awards_wins);
        String[] awardsEventsArray = context.getResources().getStringArray(R.array.awards_events);

        String awardsWins = awardsWinsArray[new Random().nextInt(awardsWinsArray.length)];
        String awardsEvents = awardsEventsArray[new Random().nextInt(awardsEventsArray.length)];
        String awardsCount = String.valueOf(random.nextInt(11 - 2));
        String otherWinsCount = String.valueOf(random.nextInt(30 - 5));
        String otherNominationsCount = String.valueOf(random.nextInt(100 - 10));

        String awards = awardsWins + " "
                + awardsCount + " "
                + awardsEvents + " "
                + context.getResources().getString(R.string.awards_another) + " "
                + otherWinsCount + " "
                + context.getResources().getString(R.string.awards_another_wins) + " "
                + context.getResources().getString(R.string.awards_another_and) + " "
                + otherNominationsCount + " "
                + context.getResources().getString(R.string.awards_another_nominations);

        return awards;
    }
}
