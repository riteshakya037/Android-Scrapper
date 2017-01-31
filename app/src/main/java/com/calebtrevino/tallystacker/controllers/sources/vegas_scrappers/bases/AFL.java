package com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.bases;

import android.os.Parcel;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.WNBA_Total;

/**
 * @author Ritesh Shakya
 */
@SuppressWarnings("FieldCanBeLocal")
public abstract class AFL extends LeagueBase {
    @SuppressWarnings("unused")
    private static final String TAG = WNBA_Total.class.getSimpleName();

    private final String NAME = "Arena Football League";
    private final String BASE_URL = "http://www.vegasinsider.com/afl/odds/las-vegas/";
    private final String ACRONYM = "AFL";
    private final String CSS_QUERY = "table.frodds-data-tbl > tbody>tr:has(td:not(.game-notes))";

    protected AFL() {
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
        return "";
    }

    @Override
    public int getTeamResource() {
        return R.raw.afl_teams;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
