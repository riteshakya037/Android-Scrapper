package com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers;

import com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.bases.NCAA_FB;
import com.calebtrevino.tallystacker.models.enums.ScoreType;


/**
 * @author Ritesh Shakya
 */

public class NCAA_FB_Spread extends NCAA_FB {
    private final ScoreType BID_SCORE_TYPE = ScoreType.SPREAD;

    @Override
    public ScoreType getScoreType() {
        return BID_SCORE_TYPE;
    }
}
