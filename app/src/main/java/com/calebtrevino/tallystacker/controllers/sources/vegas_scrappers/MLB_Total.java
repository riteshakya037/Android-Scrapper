package com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers;

import android.os.Parcel;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.controllers.sources.espn_scrappers.EspnGameScoreParser;
import com.calebtrevino.tallystacker.controllers.sources.espn_scrappers.exceptions.ExpectedElementNotFound;
import com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.bases.LeagueBase;
import com.calebtrevino.tallystacker.models.enums.ScoreType;
import com.calebtrevino.tallystacker.utils.StringUtils;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


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
    public String getEspnUrl() {
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
    public String getScoreBoard() {
        return "/boxscore";
    }

    @Override
    public EspnGameScoreParser.IntermediateResult scrapeScoreBoard(Document document) throws Exception {
        // Scrape Game Url
        Elements titleElement = document.select("table.linescore>tbody>tr.periods>td");
        int incNo = 0, runRow = 0;
        for (Element element : titleElement) {
            if (element.text().equals("R")) {
                runRow = incNo;
            }
            incNo++;
        }
        Elements element = document.select("table.linescore>tbody>tr");
        EspnGameScoreParser.IntermediateResult result = new EspnGameScoreParser.IntermediateResult();
        for (int i = 0; i < element.size(); i++) {
            if (StringUtils.isNotNull(element.get(i).select("td.team").text())) {
                result.add(element.get(i).select("td.team").text(), element.get(i).select("td").get(runRow).text());
            }
        }
        if (result.isEmpty()) {
            throw new ExpectedElementNotFound("Couldn't find any games to download.");
        }
        result.setCompleted(false);
        return result;
    }
}
