package com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers;

import com.calebtrevino.tallystacker.controllers.factories.DefaultFactory;
import com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.bases.Soccer;
import com.calebtrevino.tallystacker.models.Bid;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.enums.BidCondition;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author Ritesh Shakya
 */

public class Soccer_Spread extends Soccer {
    private static final String BASE_URL = "http://www.vegasinsider.com/soccer/odds/las-vegas/spread/";

    @Override
    public String getBaseUrl() {
        return BASE_URL;
    }

    public void createBidInfo(String text, Game gameFromHtmlBlock, boolean isVI_column) {
        // 3 -25 41½u-10
        String[] bidBlocks = text.split("br2n");
        int position = 0;
        Bid bid1 = DefaultFactory.Bid.constructDefault();
        Bid bid2 = DefaultFactory.Bid.constructDefault();
        for (String individualBlock : bidBlocks) {
            Pattern pattern = Pattern.compile("([-+]?(\\d+|" + //digit
                    "[\\p{N}]|" +  // if char like ½ exists
                    "\\d+[\\p{N}])" +  // if char like 1½ exists
                    ")" +
                    " " + // condition to check
                    "([-+]?(\\d+|EV))");
            Matcher m = pattern.matcher(individualBlock.trim());
            if (m.matches()) {
                if (position == 1) {
                    bid1.setBidAmount(m.group(1));
                    bid1.setVIColumn(isVI_column);
                    bid1.setCondition(BidCondition.SPREAD);
                    bid1.setVigAmount(m.group(3));
                } else if (position == 2) {
                    bid2.setBidAmount(m.group(1));
                    bid2.setVIColumn(isVI_column);
                    bid2.setCondition(BidCondition.SPREAD);
                    bid2.setVigAmount(m.group(3));
                    if (bid1.getVigAmount() < bid2.getVigAmount()) {
                        bid1.setBidAmount(bid2.getBidAmount());
                        gameFromHtmlBlock.getBidList().add(bid1);
                    } else {
                        gameFromHtmlBlock.getBidList().add(bid2);
                    }
                }
            }
            position++;
        }
    }
}
