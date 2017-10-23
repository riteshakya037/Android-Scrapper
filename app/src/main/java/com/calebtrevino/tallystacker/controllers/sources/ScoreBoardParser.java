package com.calebtrevino.tallystacker.controllers.sources;

import com.calebtrevino.tallystacker.controllers.sources.espn_scrappers.EspnScoreboardParser;
import com.calebtrevino.tallystacker.controllers.sources.espn_scrappers.exceptions.ExpectedElementNotFound;
import com.calebtrevino.tallystacker.controllers.sources.sofascore_scrappers.SofaScoreboardParser;
import com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.bases.League;
import com.calebtrevino.tallystacker.models.Game;

/**
 * Parser base for fetching game url during the initial phase i.e. game construction from
 * vegasinsider site.
 *
 * @author Ritesh Shakya
 */
public abstract class ScoreBoardParser {
    public static void writeGames() {
        EspnScoreboardParser.writeGames();
        SofaScoreboardParser.writeGames();
    }

    public static ScoreBoardParser getObject(League leagueType) throws ExpectedElementNotFound {
        if (leagueType.getScoreBoardParser() instanceof EspnScoreboardParser) {
            return new EspnScoreboardParser(leagueType);
        } else if (leagueType.getScoreBoardParser() instanceof SofaScoreboardParser) {
            return new SofaScoreboardParser(leagueType);
        } else {
            return new ScoreBoardParser() {
                @Override public void setGameUrl(Game game) {
                    System.out.println("Shouldn't reach");
                }
            };
        }
    }

    public abstract void setGameUrl(Game game);
}
