package com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.bases;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.controllers.sources.ScoreBoardParser;
import com.calebtrevino.tallystacker.controllers.sources.ScoreParser;
import com.calebtrevino.tallystacker.controllers.sources.espn_scrappers.exceptions.ExpectedElementNotFound;
import com.calebtrevino.tallystacker.controllers.sources.sofascore_scrappers.SofaGameScoreParser;
import com.calebtrevino.tallystacker.controllers.sources.sofascore_scrappers.SofaScoreboardParser;
import com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.WNBA_Total;

/**
 * @author Ritesh Shakya
 */
public abstract class WNBA extends LeagueBase {
    @SuppressWarnings("unused") private static final String TAG = WNBA_Total.class.getSimpleName();
    private static final String ESPN_URL = "http://www.sofascore.com/basketball//";

    private final static String NAME = "Women's National Basketball Association";
    private final static String BASE_URL = "http://www.vegasinsider.com/wnba/odds/las-vegas/";
    private final static String ACRONYM = "WNBA";
    private final static String CSS_QUERY =
            "table.frodds-data-tbl > tbody>tr:has(td:not(.game-notes))";

    protected WNBA() {
    }

    @Override public String getName() {
        return NAME;
    }

    @Override public String getAcronym() {
        return ACRONYM;
    }

    @Override public String getBaseUrl() {
        return BASE_URL;
    }

    @Override public String getCSSQuery() {
        return CSS_QUERY;
    }

    @Override public String getPackageName() {
        return getClass().getName();
    }

    @Override public String getBaseScoreUrl() {
        return ESPN_URL;
    }

    @Override public int getAvgTime() {
        return 90;
    }

    @Override public int getTeamResource() {
        return R.raw.wnba_teams;
    }

    @Override public boolean hasSecondPhase() {
        return true;
    }

    @Override public ScoreBoardParser getScoreBoardParser() throws ExpectedElementNotFound {
        return SofaScoreboardParser.getInstance(this);
    }

    @Override public ScoreParser getParser() {
        return SofaGameScoreParser.getInstance();
    }
}
