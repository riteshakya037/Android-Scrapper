package com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.bases;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.WNBA_Total;

/**
 * @author Ritesh Shakya
 */
public abstract class CFL extends LeagueBase {
    @SuppressWarnings("unused") private static final String TAG = WNBA_Total.class.getSimpleName();

    private final static String NAME = "Canadian Football";
    private final static String BASE_URL = "http://www.vegasinsider.com/cfl/odds/las-vegas/";
    private final static String ACRONYM = "CFL";
    private final static String CSS_QUERY =
            "table.frodds-data-tbl > tbody>tr:has(td:not(.game-notes))";

    protected CFL() {
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
        return "";
    }

    @Override public int getAvgTime() {
        return 90;
    }

    @Override public int getTeamResource() {
        return R.raw.cfl_teams;
    }
}
