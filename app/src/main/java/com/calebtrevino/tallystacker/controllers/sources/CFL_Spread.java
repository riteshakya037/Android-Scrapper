package com.calebtrevino.tallystacker.controllers.sources;

import android.os.Parcel;

import com.calebtrevino.tallystacker.controllers.factories.DefaultFactory;
import com.calebtrevino.tallystacker.controllers.sources.bases.CFL;
import com.calebtrevino.tallystacker.models.Bid;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.enums.BidCondition;
import com.calebtrevino.tallystacker.models.enums.ScoreType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author Ritesh Shakya
 */

public class CFL_Spread extends CFL {
    private ScoreType BID_SCORE_TYPE = ScoreType.SPREAD;

    public CFL_Spread() {
    }

    private CFL_Spread(Parcel in) {
    }

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

    @Override
    public ScoreType getScoreType() {
        return BID_SCORE_TYPE;
    }

}
