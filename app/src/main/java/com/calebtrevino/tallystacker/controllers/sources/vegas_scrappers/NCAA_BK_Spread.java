package com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers;

import android.os.Parcel;

import com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.bases.NCAA_BK;
import com.calebtrevino.tallystacker.models.enums.ScoreType;


/**
 * @author Ritesh Shakya
 */

public class NCAA_BK_Spread extends NCAA_BK {
    public static final Creator<NCAA_BK_Spread> CREATOR = new Creator<NCAA_BK_Spread>() {
        @Override
        public NCAA_BK_Spread createFromParcel(Parcel in) {
            return new NCAA_BK_Spread(in);
        }

        @Override
        public NCAA_BK_Spread[] newArray(int size) {
            return new NCAA_BK_Spread[size];
        }
    };
    private final ScoreType BID_SCORE_TYPE = ScoreType.SPREAD;

    @SuppressWarnings("unused")
    public NCAA_BK_Spread() {
    }

    @SuppressWarnings("UnusedParameters")
    private NCAA_BK_Spread(Parcel in) {
        // Empty Block
    }

    @Override
    public ScoreType getScoreType() {
        return BID_SCORE_TYPE;
    }

}
