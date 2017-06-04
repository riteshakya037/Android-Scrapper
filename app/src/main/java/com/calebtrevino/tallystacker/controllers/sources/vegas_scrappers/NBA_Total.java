package com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers;

import com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.bases.NBA;
import com.calebtrevino.tallystacker.models.enums.ScoreType;


/**
 * @author Ritesh Shakya
 */

public class NBA_Total extends NBA {
    private final ScoreType BID_SCORE_TYPE = ScoreType.TOTAL;

    @Override
    public ScoreType getScoreType() {
        return BID_SCORE_TYPE;
    }
}
