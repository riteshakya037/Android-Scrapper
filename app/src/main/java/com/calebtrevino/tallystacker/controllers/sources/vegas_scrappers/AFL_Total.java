package com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers;

import android.os.Parcel;
import com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.bases.AFL;
import com.calebtrevino.tallystacker.models.enums.ScoreType;

/**
 * @author Ritesh Shakya
 */

public class AFL_Total extends AFL {
    public static final Creator<AFL_Total> CREATOR = new Creator<AFL_Total>() {
        @Override public AFL_Total createFromParcel(Parcel in) {
            return new AFL_Total(in);
        }

        @Override public AFL_Total[] newArray(int size) {
            return new AFL_Total[size];
        }
    };
    private final ScoreType BID_SCORE_TYPE = ScoreType.TOTAL;

    public AFL_Total() {
    }

    @SuppressWarnings("UnusedParameters") private AFL_Total(Parcel in) {
        // Empty Block
    }

    @Override public ScoreType getScoreType() {
        return BID_SCORE_TYPE;
    }

    @Override public String getContraryPackageName() {
        return AFL_Spread.class.getName();
    }
}
