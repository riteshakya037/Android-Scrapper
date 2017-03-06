package com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.bases;

import android.os.Parcel;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.WNBA_Total;

/**
 * @author Ritesh Shakya
 */
@SuppressWarnings("FieldCanBeLocal")
public abstract class NCAA_FB extends LeagueBase {
    @SuppressWarnings("unused")
    private static final String TAG = WNBA_Total.class.getSimpleName();

    private final String NAME = "College football";
    private final String BASE_URL = "http://www.vegasinsider.com/college-football/odds/las-vegas/";
    private final String ACRONYM = "NCAA FB";
    private final String CSS_QUERY = "table.frodds-data-tbl > tbody>tr:has(td:not(.game-notes))";

    protected NCAA_FB() {
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
        return "";
    }

    @Override
    public int getAvgTime() {
        return 90;
    }

    @Override
    public int getTeamResource() {
        return R.raw.ncaa_fb_teams;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
