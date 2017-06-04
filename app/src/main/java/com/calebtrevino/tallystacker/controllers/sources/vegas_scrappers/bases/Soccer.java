package com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.bases;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.controllers.sources.ScoreBoardParser;
import com.calebtrevino.tallystacker.controllers.sources.ScoreParser;
import com.calebtrevino.tallystacker.controllers.sources.espn_scrappers.exceptions.ExpectedElementNotFound;
import com.calebtrevino.tallystacker.controllers.sources.sofascore_scrappers.SofaScoreParser;
import com.calebtrevino.tallystacker.controllers.sources.sofascore_scrappers.SofaScoreboardParser;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.enums.ScoreType;

import java.util.regex.Pattern;

/**
 * @author Ritesh Shakya
 */
public abstract class Soccer extends LeagueBase {
    private static final String SOFA_SCORE_URL = "http://www.sofascore.com/football//";
    private ScoreType BID_SCORE_TYPE = ScoreType.SPREAD;
    private String NAME = "Soccer";
    private String ACRONYM = "Soccer";
    private String CSS_QUERY = "table.frodds-data-tbl > tbody>tr:has(td:not(.game-notes))";

    @Override
    public ScoreType getScoreType() {
        return BID_SCORE_TYPE;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getAcronym() {
        return ACRONYM;
    }

    @Override
    public String getCSSQuery() {
        return CSS_QUERY;
    }


    @Override
    public String getPackageName() {
        return getClass().getName();
    }

    @Override
    public String getBaseScoreUrl() {
        return SOFA_SCORE_URL;
    }

    @Override
    public int getAvgTime() {
        return 90;
    }

    @Override
    public int getTeamResource() {
        return R.raw.soccer_teams;
    }

    @Override
    public void createGameInfo(String bodyText, Game gameFromHtmlBlock) {
        // Header: 09/08 8:30 PM 451 Carolina 452 Denver
        Pattern pattern = Pattern.compile("([0-9]{2}/[0-9]{2}" + // Date of match
                "\\s" + "[0-9]{1,2}:[0-9]{2}" + "\\s" + "[A|P]M)" + // Time of match
                "br2n " + "([0-9]{6})" + // First team code
                ".?(\\w.*)br2n " + // First team city
                "([0-9]{6})" + // Second team code
                ".?(\\w.*)br2n " +// Second team city
                "([0-9]{6})" +
                ".?Drawbr2n " +
                "([0-9]{6})" +
                ".?Totalbr2n"
        );
        setGameInfo(bodyText, gameFromHtmlBlock, pattern);
    }

    @Override
    public boolean hasSecondPhase() {
        return true;
    }

    @Override
    public ScoreBoardParser getScoreBoardParser() throws ExpectedElementNotFound {
        return SofaScoreboardParser.getInstance(this);
    }

    @Override
    public ScoreParser getParser() {
        return SofaScoreParser.getInstance();
    }
}
