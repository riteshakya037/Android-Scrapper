package com.calebtrevino.tallystacker.controllers.sources.bases;

import android.os.Parcel;

import com.calebtrevino.tallystacker.controllers.sources.WNBA_Total;

/**
 * @author Ritesh Shakya
 */
public abstract class AFL extends LeagueBase {
    private static final String TAG = WNBA_Total.class.getSimpleName();

    private String NAME = "Arena Football League";
    private String BASE_URL = "http://www.vegasinsider.com/afl/odds/las-vegas/";
    private String ACRONYM = "AFL";
    private String CSS_QUERY = "table.frodds-data-tbl > tbody>tr:has(td:not(.game-notes))";

    public AFL() {
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
