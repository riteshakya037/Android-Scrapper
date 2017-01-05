package com.calebtrevino.tallystacker.controllers.sources;

import android.os.Parcel;

import com.calebtrevino.tallystacker.controllers.factories.DefaultFactory;
import com.calebtrevino.tallystacker.controllers.sources.bases.Soccer;
import com.calebtrevino.tallystacker.models.Bid;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.enums.BidCondition;
import com.calebtrevino.tallystacker.models.enums.ScoreType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author Ritesh Shakya
 */

public class Soccer_Total extends Soccer {
    private final ScoreType BID_SCORE_TYPE = ScoreType.TOTAL;

    @SuppressWarnings("FieldCanBeLocal")
    private String BASE_URL = "http://www.vegasinsider.com/soccer/odds/las-vegas/";

    public Soccer_Total() {
    }

    @SuppressWarnings("UnusedParameters")
    private Soccer_Total(Parcel in) {
    }

    public static final Creator<Soccer_Total> CREATOR = new Creator<Soccer_Total>() {
        @Override
        public Soccer_Total createFromParcel(Parcel in) {
            return new Soccer_Total(in);
        }

        @Override
        public Soccer_Total[] newArray(int size) {
            return new Soccer_Total[size];
        }
    };

    @Override
    public ScoreType getScoreType() {
        return BID_SCORE_TYPE;
    }

    @Override
    public String getBaseUrl() {
        return BASE_URL;
    }


    @Override
    protected void createBidInfo(String text, Game gameFromHtmlBlock, boolean isVI_column) {
        String[] bidBlocks = text.split("br2n");
        for (String individualBlock : bidBlocks) {
            Pattern pattern = Pattern.compile("(\\d+" + //digit before o/u
                    "[\\p{N}]?" +  // if char like ½ exists
                    ")(" +
                    "[uUoO]" + // condition to check
                    ")" +
                    "([-+]?\\d+)");
            Matcher m = pattern.matcher(individualBlock.trim());
            if (m.matches()) {
                Bid bid = DefaultFactory.Bid.constructDefault();
                bid.setBidAmount(m.group(1));
                bid.setCondition(BidCondition.match(m.group(2)));
                bid.setVI_column(isVI_column);
                bid.setVigAmount(m.group(3));
                gameFromHtmlBlock.getBidList().add(bid);
            }
        }
    }
}
