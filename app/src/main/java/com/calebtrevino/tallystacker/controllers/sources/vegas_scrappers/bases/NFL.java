package com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.bases;

import android.os.Parcel;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.WNBA_Total;

/**
 * @author Ritesh Shakya
 */
@SuppressWarnings("FieldCanBeLocal")
public abstract class NFL extends LeagueBase {
    @SuppressWarnings("unused")
    private static final String TAG = WNBA_Total.class.getSimpleName();

    private final String ESPN_URL = "http://www.espn.com/nfl";
    private final String NAME = "National Football League";
    private final String BASE_URL = "http://www.vegasinsider.com/nfl/odds/las-vegas/";
    private final String ACRONYM = "NFL";
    private final String CSS_QUERY = "table.frodds-data-tbl > tbody>tr:has(td:not(.game-notes))";

    protected NFL() {
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
    public String getEspnUrl() {
        return ESPN_URL;
    }

    @Override
    public int getAvgTime() {
        return 90;
    }

    @Override
    public int getTeamResource() {
        return R.raw.nfl_teams;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
