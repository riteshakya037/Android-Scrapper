package com.calebtrevino.tallystacker.controllers.sources;

import android.os.Parcel;

import com.calebtrevino.tallystacker.controllers.sources.bases.NFL;
import com.calebtrevino.tallystacker.models.enums.ScoreType;


/**
 * @author Ritesh Shakya
 */

public class NFL_Spread extends NFL {
    private ScoreType BID_SCORE_TYPE = ScoreType.SPREAD;

    public NFL_Spread() {
    }

    private NFL_Spread(Parcel in) {
    }

    public static final Creator<NFL_Spread> CREATOR = new Creator<NFL_Spread>() {
        @Override
        public NFL_Spread createFromParcel(Parcel in) {
            return new NFL_Spread(in);
        }

        @Override
        public NFL_Spread[] newArray(int size) {
            return new NFL_Spread[size];
        }
    };

    @Override
    public ScoreType getScoreType() {
        return BID_SCORE_TYPE;
    }


}
