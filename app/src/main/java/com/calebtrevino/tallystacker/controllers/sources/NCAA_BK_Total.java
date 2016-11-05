package com.calebtrevino.tallystacker.controllers.sources;

import android.os.Parcel;

import com.calebtrevino.tallystacker.controllers.sources.bases.NCAA_BK;
import com.calebtrevino.tallystacker.controllers.sources.bases.NCAA_FB;
import com.calebtrevino.tallystacker.models.enums.ScoreType;


/**
 * @author Ritesh Shakya
 */

public class NCAA_BK_Total extends NCAA_BK {
    private ScoreType BID_SCORE_TYPE = ScoreType.TOTAL;

    public NCAA_BK_Total() {
    }

    private NCAA_BK_Total(Parcel in) {
    }

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

    @Override
    public ScoreType getScoreType() {
        return BID_SCORE_TYPE;
    }


}
