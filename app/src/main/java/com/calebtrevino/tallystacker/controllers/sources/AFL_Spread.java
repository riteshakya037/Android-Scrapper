package com.calebtrevino.tallystacker.controllers.sources;

import android.os.Parcel;

import com.calebtrevino.tallystacker.controllers.sources.bases.AFL;
import com.calebtrevino.tallystacker.models.enums.ScoreType;


/**
 * @author Ritesh Shakya
 */

public class AFL_Spread extends AFL {
    private final ScoreType BID_SCORE_TYPE = ScoreType.SPREAD;

    public AFL_Spread() {
    }

    @SuppressWarnings("UnusedParameters")
    private AFL_Spread(Parcel in) {
    }

    public static final Creator<AFL_Spread> CREATOR = new Creator<AFL_Spread>() {
        @Override
        public AFL_Spread createFromParcel(Parcel in) {
            return new AFL_Spread(in);
        }

        @Override
        public AFL_Spread[] newArray(int size) {
            return new AFL_Spread[size];
        }
    };

    @Override
    public ScoreType getScoreType() {
        return BID_SCORE_TYPE;
    }


}
