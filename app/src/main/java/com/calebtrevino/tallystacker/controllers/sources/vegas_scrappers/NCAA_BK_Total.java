package com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers;

import android.os.Parcel;

import com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.bases.NCAA_BK;
import com.calebtrevino.tallystacker.models.enums.ScoreType;


/**
 * @author Ritesh Shakya
 */

public class NCAA_BK_Total extends NCAA_BK {
    public static final Creator<NCAA_BK_Total> CREATOR = new Creator<NCAA_BK_Total>() {
        @Override
        public NCAA_BK_Total createFromParcel(Parcel in) {
            return new NCAA_BK_Total(in);
        }

        @Override
        public NCAA_BK_Total[] newArray(int size) {
            return new NCAA_BK_Total[size];
        }
    };
    private final ScoreType BID_SCORE_TYPE = ScoreType.TOTAL;

    @SuppressWarnings("unused")
    public NCAA_BK_Total() {
    }

    @SuppressWarnings("UnusedParameters")
    private NCAA_BK_Total(Parcel in) {
        // Empty Block
    }

    @Override
    public ScoreType getScoreType() {
        return BID_SCORE_TYPE;
    }


}
