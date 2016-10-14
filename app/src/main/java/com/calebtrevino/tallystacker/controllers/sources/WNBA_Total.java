package com.calebtrevino.tallystacker.controllers.sources;

import android.os.Parcel;

import com.calebtrevino.tallystacker.controllers.sources.bases.WNBA;
import com.calebtrevino.tallystacker.models.enums.ScoreType;


/**
 * @author Ritesh Shakya
 */

public class WNBA_Total extends WNBA {
    private ScoreType BID_SCORE_TYPE = ScoreType.TOTAL;

    public WNBA_Total() {
    }

    private WNBA_Total(Parcel in) {
    }

    public static final Creator<WNBA_Total> CREATOR = new Creator<WNBA_Total>() {
        @Override
        public WNBA_Total createFromParcel(Parcel in) {
            return new WNBA_Total(in);
        }

        @Override
        public WNBA_Total[] newArray(int size) {
            return new WNBA_Total[size];
        }
    };

    @Override
    public ScoreType getScoreType() {
        return BID_SCORE_TYPE;
    }

}
