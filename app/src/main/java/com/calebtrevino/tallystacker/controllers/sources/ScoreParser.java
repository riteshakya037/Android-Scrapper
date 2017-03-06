package com.calebtrevino.tallystacker.controllers.sources;

import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.IntermediateResult;

/**
 * @author Ritesh Shakya
 */
public abstract class ScoreParser {
    public abstract IntermediateResult getCurrentScore(Game game) throws Exception;

    public abstract IntermediateResult scrapeUsual() throws Exception;
}
