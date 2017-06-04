package com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.controllers.sources.ScoreParser;
import com.calebtrevino.tallystacker.controllers.sources.espn_scrappers.EspnGameScoreParser;
import com.calebtrevino.tallystacker.controllers.sources.espn_scrappers.exceptions.ExpectedElementNotFound;
import com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.bases.LeagueBase;
import com.calebtrevino.tallystacker.models.IntermediateResult;
import com.calebtrevino.tallystacker.models.enums.ScoreType;


/**
 * @author Ritesh Shakya
 */

public class MLB_Total extends LeagueBase {
    @SuppressWarnings("unused")
    private static final String TAG = MLB_Total.class.getSimpleName();
    private static final ScoreType BID_SCORE_TYPE = ScoreType.TOTAL;
    private static final String NAME = "Major League Baseball";
    private static final String BASE_URL = "http://www.vegasinsider.com/mlb/odds/las-vegas/";
    private static final String ACRONYM = "MLB";
    private static String CSS_QUERY = "table.frodds-data-tbl > tbody>tr:has(td:not(.game-notes))";


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
    public String getBaseUrl() {
        return BASE_URL;
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
        String ESPN_URL = "http://www.espn.com/mlb";
        return ESPN_URL;
    }

    @Override
    public int getAvgTime() {
        return 90;
    }

    @Override
    public int getTeamResource() {
        return R.raw.mlb_teams;
    }

    @Override
    public String getScoreBoardURL() {
        return "/boxscore";
    }

    @Override
    public boolean hasSecondPhase() {
        return true;
    }

    @Override
    public IntermediateResult scrapeScoreBoard(ScoreParser scoreParser) throws ExpectedElementNotFound {
        return ((EspnGameScoreParser) scoreParser).scrapeMLB();
    }
}
