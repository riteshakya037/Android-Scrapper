package com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers;

import android.os.Parcel;

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

    private ScoreType BID_SCORE_TYPE = ScoreType.TOTAL;
    private String NAME = "Major League Baseball";
    private String ESPN_URL = "http://www.espn.com/mlb";
    private String BASE_URL = "http://www.vegasinsider.com/mlb/odds/las-vegas/";
    private String ACRONYM = "MLB";
    private String CSS_QUERY = "table.frodds-data-tbl > tbody>tr:has(td:not(.game-notes))";

    public MLB_Total() {
    }

    private MLB_Total(Parcel in) {
        BID_SCORE_TYPE = in.readParcelable(ScoreType.class.getClassLoader());
        NAME = in.readString();
        BASE_URL = in.readString();
        ACRONYM = in.readString();
        CSS_QUERY = in.readString();
    }

    public static final Creator<MLB_Total> CREATOR = new Creator<MLB_Total>() {
        @Override
        public MLB_Total createFromParcel(Parcel in) {
            return new MLB_Total(in);
        }

        @Override
        public MLB_Total[] newArray(int size) {
            return new MLB_Total[size];
        }
    };

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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(BID_SCORE_TYPE, i);
        parcel.writeString(NAME);
        parcel.writeString(BASE_URL);
        parcel.writeString(ACRONYM);
        parcel.writeString(CSS_QUERY);
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
