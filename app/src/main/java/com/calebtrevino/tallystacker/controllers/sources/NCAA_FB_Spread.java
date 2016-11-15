package com.calebtrevino.tallystacker.controllers.sources;

import android.os.Parcel;
import android.os.Parcelable;

import com.calebtrevino.tallystacker.controllers.sources.bases.NCAA_FB;
import com.calebtrevino.tallystacker.models.enums.ScoreType;


/**
 * @author Ritesh Shakya
 */

public class NCAA_FB_Spread extends NCAA_FB {
    private final ScoreType BID_SCORE_TYPE = ScoreType.SPREAD;

    public NCAA_FB_Spread() {
    }

    @SuppressWarnings("UnusedParameters")
    private NCAA_FB_Spread(Parcel in) {
    }

    public static final Parcelable.Creator<NCAA_FB_Spread> CREATOR = new Parcelable.Creator<NCAA_FB_Spread>() {
        @Override
        public NCAA_FB_Spread createFromParcel(Parcel in) {
            return new NCAA_FB_Spread(in);
        }

        @Override
        public NCAA_FB_Spread[] newArray(int size) {
            return new NCAA_FB_Spread[size];
        }
    };

    @Override
    public ScoreType getScoreType() {
        return BID_SCORE_TYPE;
    }

}
