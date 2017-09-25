package com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers;

import android.os.Parcel;
import com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.bases.NFL;
import com.calebtrevino.tallystacker.models.enums.ScoreType;

/**
 * @author Ritesh Shakya
 */

public class NFL_Total extends NFL {
    public static final Creator<NFL_Total> CREATOR = new Creator<NFL_Total>() {
        @Override public NFL_Total createFromParcel(Parcel in) {
            return new NFL_Total(in);
        }

        @Override public NFL_Total[] newArray(int size) {
            return new NFL_Total[size];
        }
    };
    private final ScoreType BID_SCORE_TYPE = ScoreType.TOTAL;

    public NFL_Total() {
    }

    @SuppressWarnings("UnusedParameters") private NFL_Total(Parcel in) {
        // Empty Block
    }

    @Override public ScoreType getScoreType() {
        return BID_SCORE_TYPE;
    }

    @Override public String getContraryPackageName() {
        return NFL_Spread.class.getName();
    }
}
