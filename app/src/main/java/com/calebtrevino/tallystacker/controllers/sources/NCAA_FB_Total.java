package com.calebtrevino.tallystacker.controllers.sources;

import android.os.Parcel;
import android.os.Parcelable;

import com.calebtrevino.tallystacker.controllers.sources.bases.NCAA_FB;
import com.calebtrevino.tallystacker.models.enums.ScoreType;


/**
 * @author Ritesh Shakya
 */

public class NCAA_FB_Total extends NCAA_FB {
    private final ScoreType BID_SCORE_TYPE = ScoreType.TOTAL;

    public NCAA_FB_Total() {
    }

    @SuppressWarnings("UnusedParameters")
    private NCAA_FB_Total(Parcel in) {
    }

    public static final Parcelable.Creator<NCAA_FB_Total> CREATOR = new Parcelable.Creator<NCAA_FB_Total>() {
        @Override
        public NCAA_FB_Total createFromParcel(Parcel in) {
            return new NCAA_FB_Total(in);
        }

        @Override
        public NCAA_FB_Total[] newArray(int size) {
            return new NCAA_FB_Total[size];
        }
    };

    @Override
    public ScoreType getScoreType() {
        return BID_SCORE_TYPE;
    }


}
