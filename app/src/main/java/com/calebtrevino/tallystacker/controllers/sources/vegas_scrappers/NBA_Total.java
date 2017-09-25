package com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers;

import android.os.Parcel;
import com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.bases.NBA;
import com.calebtrevino.tallystacker.models.enums.ScoreType;

/**
 * @author Ritesh Shakya
 */

public class NBA_Total extends NBA {
    public static final Creator<NBA_Total> CREATOR = new Creator<NBA_Total>() {
        @Override public NBA_Total createFromParcel(Parcel in) {
            return new NBA_Total(in);
        }

        @Override public NBA_Total[] newArray(int size) {
            return new NBA_Total[size];
        }
    };
    private final ScoreType BID_SCORE_TYPE = ScoreType.TOTAL;

    public NBA_Total() {
    }

    @SuppressWarnings("UnusedParameters") private NBA_Total(Parcel in) {
        // Empty Block
    }

    @Override public ScoreType getScoreType() {
        return BID_SCORE_TYPE;
    }

    @Override public String getContraryPackageName() {
        return NBA_Spread.class.getName();
    }
}
