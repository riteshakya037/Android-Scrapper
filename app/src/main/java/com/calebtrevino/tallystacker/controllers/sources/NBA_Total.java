package com.calebtrevino.tallystacker.controllers.sources;

import android.os.Parcel;

import com.calebtrevino.tallystacker.controllers.sources.bases.NBA;
import com.calebtrevino.tallystacker.models.enums.ScoreType;


/**
 * @author Ritesh Shakya
 */

public class NBA_Total extends NBA {
    private ScoreType BID_SCORE_TYPE = ScoreType.TOTAL;

    public NBA_Total() {
    }

    private NBA_Total(Parcel in) {
    }

    public static final Creator<NBA_Total> CREATOR = new Creator<NBA_Total>() {
        @Override
        public NBA_Total createFromParcel(Parcel in) {
            return new NBA_Total(in);
        }

        @Override
        public NBA_Total[] newArray(int size) {
            return new NBA_Total[size];
        }
    };

    @Override
    public ScoreType getScoreType() {
        return BID_SCORE_TYPE;
    }



}
