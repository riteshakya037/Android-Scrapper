package com.calebtrevino.tallystacker.controllers.sources;

import com.calebtrevino.tallystacker.controllers.sources.espn_scrappers.EspnScoreboardParser;
import com.calebtrevino.tallystacker.controllers.sources.sofascore_scrappers.SofaScoreboardParser;
import com.calebtrevino.tallystacker.models.Game;

/**
 * @author Ritesh Shakya
 */
public abstract class ScoreBoardParser {
    public abstract void setGameUrl(Game game);

    public static void writeGames() {
        EspnScoreboardParser.writeGames();
        SofaScoreboardParser.writeGames();
    }
}
