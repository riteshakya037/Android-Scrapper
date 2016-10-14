package com.calebtrevino.tallystacker.controllers.sources.bases;

import android.os.Parcel;

import com.calebtrevino.tallystacker.controllers.sources.WNBA_Total;

/**
 * @author Ritesh Shakya
 */
public abstract class NCAA_FB extends LeagueBase {
    private static final String TAG = WNBA_Total.class.getSimpleName();

    private String NAME = "College football";
    private String BASE_URL = "http://www.vegasinsider.com/college-football/odds/las-vegas/";
    private String ACRONYM = "NCAA FB";
    private String CSS_QUERY = "table.frodds-data-tbl > tbody>tr:has(td:not(.game-notes))";

    public NCAA_FB() {
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
