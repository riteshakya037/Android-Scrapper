package com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers;

import com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.bases.NFL;
import com.calebtrevino.tallystacker.models.enums.ScoreType;


/**
 * @author Ritesh Shakya
 */

public class NFL_Spread extends NFL {
    private final ScoreType BID_SCORE_TYPE = ScoreType.SPREAD;

    @Override
    public ScoreType getScoreType() {
        return BID_SCORE_TYPE;
    }
}
