package com.calebtrevino.tallystacker.controllers.sources;

import android.os.Parcel;

import com.calebtrevino.tallystacker.controllers.sources.bases.NFL;
import com.calebtrevino.tallystacker.models.enums.ScoreType;


/**
 * @author Ritesh Shakya
 */

public class NFL_Total extends NFL {
    private ScoreType BID_SCORE_TYPE = ScoreType.TOTAL;

    public NFL_Total() {
    }

    private NFL_Total(Parcel in) {
    }

    public static final Creator<NFL_Total> CREATOR = new Creator<NFL_Total>() {
        @Override
        public NFL_Total createFromParcel(Parcel in) {
            return new NFL_Total(in);
        }

        @Override
        public NFL_Total[] newArray(int size) {
            return new NFL_Total[size];
        }
    };

    @Override
    public ScoreType getScoreType() {
        return BID_SCORE_TYPE;
    }


}
