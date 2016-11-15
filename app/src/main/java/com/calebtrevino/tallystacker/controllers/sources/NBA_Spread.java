package com.calebtrevino.tallystacker.controllers.sources;

import android.os.Parcel;

import com.calebtrevino.tallystacker.controllers.sources.bases.NBA;
import com.calebtrevino.tallystacker.models.enums.ScoreType;


/**
 * @author Ritesh Shakya
 */

public class NBA_Spread extends NBA {
    private final ScoreType BID_SCORE_TYPE = ScoreType.SPREAD;

    public NBA_Spread() {
    }

    @SuppressWarnings("UnusedParameters")
    private NBA_Spread(Parcel in) {
    }

    public static final Creator<NBA_Spread> CREATOR = new Creator<NBA_Spread>() {
        @Override
        public NBA_Spread createFromParcel(Parcel in) {
            return new NBA_Spread(in);
        }

        @Override
        public NBA_Spread[] newArray(int size) {
            return new NBA_Spread[size];
        }
    };

    @Override
    public ScoreType getScoreType() {
        return BID_SCORE_TYPE;
    }


}
