package com.calebtrevino.tallystacker.controllers.sources;

import android.os.Parcel;

import com.calebtrevino.tallystacker.controllers.factories.DefaultFactory;
import com.calebtrevino.tallystacker.controllers.sources.bases.WNBA;
import com.calebtrevino.tallystacker.models.Bid;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.enums.BidCondition;
import com.calebtrevino.tallystacker.models.enums.ScoreType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


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

    @Override
    protected void createBidInfo(String text, Game gameFromHtmlBlock) {
        // 3 -25 41½u-10
        String[] bidBlocks = text.split("\n");
        for (String individualBlock : bidBlocks) {
            Pattern pattern = Pattern.compile("(\\d+" + //digit before o/u
                    "[\\p{N}]?" +  // if char like ½ exists
                    ")(" +
                    "[uUoO]" + // condition to check
                    ").*");
            Matcher m = pattern.matcher(individualBlock.trim());
            if (m.matches()) {
                Bid bid = DefaultFactory.Bid.constructDefault();
                bid.setBidAmount(m.group(1));
                bid.setCondition(BidCondition.match(m.group(2)));
                bid.createID();
                gameFromHtmlBlock.getBidList().add(bid);
            }
        }
    }

}
