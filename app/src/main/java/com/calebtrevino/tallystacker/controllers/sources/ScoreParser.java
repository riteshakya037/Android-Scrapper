package com.calebtrevino.tallystacker.controllers.sources;

import com.calebtrevino.tallystacker.controllers.sources.espn_scrappers.exceptions.ExpectedElementNotFound;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.IntermediateResult;

/**
 * Abstract class for referencing the scoreboard during intermediate fetches.
 *
 * @author Ritesh Shakya
 */
public abstract class ScoreParser {
    public abstract IntermediateResult getCurrentScore(Game game) throws ExpectedElementNotFound;

    public abstract IntermediateResult scrapeUsual() throws ExpectedElementNotFound;
}
