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

    @Override
    protected void createBidInfo(String text, Game gameFromHtmlBlock) {
        // 3 -25 41½u-10
        String[] bidBlocks = text.split("\n");
        for (String individualBlock : bidBlocks) {
            Pattern pattern = Pattern.compile("([-]?\\d+" + //digit before o/u
                    "[\\p{N}]?" +  // if char like ½ exists
                    ")" +
                    " -" + // condition to check
                    ".*");
            Matcher m = pattern.matcher(individualBlock.trim());
            if (m.matches()) {
                Bid bid = DefaultFactory.Bid.constructDefault();
                bid.setBidAmount(m.group(1));
                bid.setCondition(BidCondition.SPREAD);
                bid.createID();
                gameFromHtmlBlock.getBidList().add(bid);
            }
        }
    }

}
