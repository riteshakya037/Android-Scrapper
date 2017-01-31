package com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.bases;

import android.os.Parcel;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.WNBA_Total;

/**
 * @author Ritesh Shakya
 */
@SuppressWarnings("FieldCanBeLocal")
public abstract class NCAA_BK extends LeagueBase {
    @SuppressWarnings("unused")
    private static final String TAG = WNBA_Total.class.getSimpleName();
    private final String ESPN_URL = "http://www.espn.in/womens-college-basketball";
    private final String NAME = "College BasketBall";
    private final String BASE_URL = "http://www.vegasinsider.com/college-basketball/odds/las-vegas/";
    private final String ACRONYM = "NCAA BK";
    private final String CSS_QUERY = "table.frodds-data-tbl > tbody>tr:has(td:not(.game-notes))";

    protected NCAA_BK() {
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
    public int getTeamResource() {
        return R.raw.ncaa_bk__teams;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
