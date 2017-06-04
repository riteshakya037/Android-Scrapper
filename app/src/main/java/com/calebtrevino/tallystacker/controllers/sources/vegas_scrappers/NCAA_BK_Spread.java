package com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers;

import com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.bases.NCAA_BK;
import com.calebtrevino.tallystacker.models.enums.ScoreType;


/**
 * @author Ritesh Shakya
 */

public class NCAA_BK_Spread extends NCAA_BK {
    private final ScoreType BID_SCORE_TYPE = ScoreType.SPREAD;

    @Override
    public ScoreType getScoreType() {
        return BID_SCORE_TYPE;
    }
}
