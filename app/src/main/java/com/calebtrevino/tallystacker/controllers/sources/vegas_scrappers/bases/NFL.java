package com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.bases;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.WNBA_Total;

/**
 * @author Ritesh Shakya
 */
public abstract class NFL extends LeagueBase {
    @SuppressWarnings("unused") private static final String TAG = WNBA_Total.class.getSimpleName();

    private final static String ESPN_URL = "http://www.espn.com/nfl";
    private final static String NAME = "National Football League";
    private final static String BASE_URL = "http://www.vegasinsider.com/nfl/odds/las-vegas/";
    private final static String ACRONYM = "NFL";
    private final static String CSS_QUERY =
            "table.frodds-data-tbl > tbody>tr:has(td:not(.game-notes))";

    protected NFL() {
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
        return R.raw.nfl_teams;
    }

    @Override public boolean hasSecondPhase() {
        return true;
    }
}
