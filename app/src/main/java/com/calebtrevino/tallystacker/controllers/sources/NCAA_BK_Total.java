package com.calebtrevino.tallystacker.controllers.sources;

import android.os.Parcel;

import com.calebtrevino.tallystacker.controllers.factories.DefaultFactory;
import com.calebtrevino.tallystacker.controllers.sources.bases.NCAA_BK;
import com.calebtrevino.tallystacker.models.Bid;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.enums.BidCondition;
import com.calebtrevino.tallystacker.models.enums.ScoreType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author Ritesh Shakya
 */

public class NCAA_BK_Total extends NCAA_BK {
    private final ScoreType BID_SCORE_TYPE = ScoreType.TOTAL;

    @SuppressWarnings("unused")
    public NCAA_BK_Total() {
    }

    @SuppressWarnings("UnusedParameters")
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

    @Override
    public void createBidTotal(String text, Game gameFromHtmlBlock, boolean isVI_column) {
        // 3 -25 41½u-10
        String[] bidBlocks = text.split("br2n");
        int position = 0;
        for (String individualBlock : bidBlocks) {
            Pattern pattern = Pattern.compile("(\\d+" + //digit before o/u
                    "[\\p{N}]?" +  // if char like ½ exists
                    ")(" +
                    "[uUoO]" + // condition to check
                    ").*");
            Pattern TempSpreadPattern = Pattern.compile("([-]?(\\d+|" + //digit before o/u
                    "[\\p{N}]|" +  // if char like ½ exists
                    "\\d+[\\p{N}])" +  // if char like ½ exists
                    ")" +
                    " " + // condition to check
                    ".*");
            Matcher mTotal = pattern.matcher(individualBlock.trim());
            Matcher mSpread = TempSpreadPattern.matcher(individualBlock.trim());
            if (mTotal.matches()) {
                Bid bid = DefaultFactory.Bid.constructDefault();
                bid.setBidAmount(mTotal.group(1));
                bid.setCondition(BidCondition.match(mTotal.group(2)));
                bid.setVI_column(isVI_column);
                gameFromHtmlBlock.getBidList().add(bid);
                break;
            } else if (mSpread.matches() && !isVI_column) {
                Bid bid = DefaultFactory.Bid.constructDefault();
                if (position == 1) {
                    bid.setBidAmount(mSpread.group(1), true);
                } else {
                    bid.setBidAmount(mSpread.group(1));
                }
                bid.setCondition(BidCondition.SPREAD);
                bid.setVI_column(false);
                gameFromHtmlBlock.getBidList().add(bid);
                break;
            }
            position++;
        }
    }
}
