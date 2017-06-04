package com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers;

import android.os.Parcel;

import com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.bases.CFL;
import com.calebtrevino.tallystacker.models.enums.ScoreType;


/**
 * @author Ritesh Shakya
 */

public class CFL_Spread extends CFL {
    public static final Creator<CFL_Spread> CREATOR = new Creator<CFL_Spread>() {
        @Override
        public CFL_Spread createFromParcel(Parcel in) {
            return new CFL_Spread(in);
        }

        @Override
        public CFL_Spread[] newArray(int size) {
            return new CFL_Spread[size];
        }
    };
    private final ScoreType BID_SCORE_TYPE = ScoreType.SPREAD;

    public CFL_Spread() {
    }

    @SuppressWarnings("UnusedParameters")
    private CFL_Spread(Parcel in) {
    }

    @Override
    public ScoreType getScoreType() {
        return BID_SCORE_TYPE;
    }

}
