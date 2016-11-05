package com.calebtrevino.tallystacker.controllers.sources;

import android.os.Parcel;

import com.calebtrevino.tallystacker.controllers.sources.bases.AFL;
import com.calebtrevino.tallystacker.models.enums.ScoreType;


/**
 * @author Ritesh Shakya
 */

public class AFL_Total extends AFL {
    private ScoreType BID_SCORE_TYPE = ScoreType.TOTAL;

    public AFL_Total() {
    }

    private AFL_Total(Parcel in) {
    }

    public static final Creator<AFL_Total> CREATOR = new Creator<AFL_Total>() {
        @Override
        public AFL_Total createFromParcel(Parcel in) {
            return new AFL_Total(in);
        }

        @Override
        public AFL_Total[] newArray(int size) {
            return new AFL_Total[size];
        }
    };

    @Override
    public ScoreType getScoreType() {
        return BID_SCORE_TYPE;
    }


}
