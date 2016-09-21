package com.calebtrevino.tallystacker.controllers.sources;

import android.os.Parcel;

import com.calebtrevino.tallystacker.controllers.factories.DefaultFactory;
import com.calebtrevino.tallystacker.controllers.sources.bases.LeagueBase;
import com.calebtrevino.tallystacker.models.Bid;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.Team;
import com.calebtrevino.tallystacker.models.enums.BidCondition;
import com.calebtrevino.tallystacker.models.enums.ScoreType;
import com.calebtrevino.tallystacker.utils.ParseUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author Ritesh Shakya
 */

public class WNBA_Total extends LeagueBase {
    private static final String TAG = WNBA_Total.class.getSimpleName();

    private ScoreType BID_SCORE_TYPE = ScoreType.TOTAL;
    private String NAME = "Women's National Basketball Association";
    private String BASE_URL = "http://www.vegasinsider.com/wnba/odds/las-vegas/";
    private String ACRONYM = "WNBA";
    private String CSS_QUERY = "table.frodds-data-tbl > tbody>tr:has(td:not(.game-notes))";

    public WNBA_Total() {
    }

    private WNBA_Total(Parcel in) {
        BID_SCORE_TYPE = in.readParcelable(ScoreType.class.getClassLoader());
        NAME = in.readString();
        BASE_URL = in.readString();
        ACRONYM = in.readString();
        CSS_QUERY = in.readString();
    }

    public static final Creator<WNBA_Total> CREATOR = new Creator<WNBA_Total>() {
        @Override
        public WNBA_Total createFromParcel(Parcel in) {
            return new WNBA_Total(in);
        }

        @Override
        public WNBA_Total[] newArray(int size) {
            return new WNBA_Total[size];
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
    protected void createGameInfo(String bodyText, Game gameFromHtmlBlock) {
        // Header: 09/08 8:30 PM 451 Carolina 452 Denver
        Pattern pattern = Pattern.compile("([0-9]{2}/[0-9]{2})" + // Date of match
                "\\s+" + "([0-9]{1,2}:[0-9]{2}" + "\\s+" + "[A|P]M)" + // Time of match
                "\\s+" + "([0-9]{3})" + // First team code
                ".?(\\w.*)" + // First team city
                "([0-9]{3})" + // Second team code
                ".?(\\w.*)"); // Second team city
        Matcher m = pattern.matcher(bodyText);
        if (m.matches()) {
            // Initialize gameFromHtmlBlock
            gameFromHtmlBlock.setGameDateTime(ParseUtils.parseDate(m.group(1), m.group(2), "MM/dd", "hh:mm aa"));
            gameFromHtmlBlock.setGameAddDate();

            Team firstTeam = DefaultFactory.Team.constructDefault();
            firstTeam.setLeagueType(this);
            firstTeam.set_teamId(Long.valueOf(m.group(3)));
            firstTeam.setCity(m.group(4));
            firstTeam.createID();
            gameFromHtmlBlock.setFirstTeam(firstTeam);

            Team secondTeam = DefaultFactory.Team.constructDefault();
            secondTeam.setLeagueType(this);
            secondTeam.set_teamId(Long.valueOf(m.group(5)));
            secondTeam.setCity(m.group(6));
            secondTeam.createID();
            gameFromHtmlBlock.setSecondTeam(secondTeam);
        }
    }

    @Override
    protected void createBidInfo(String text, Game gameFromHtmlBlock) {
        // 3 -25 41½u-10
        String[] bidBlocks = text.split("\n");
        for (String individualBlock : bidBlocks) {
            Pattern pattern = Pattern.compile("(\\d+" + //digit before o/u
                    "[\\p{N}]?" +  // if char like ½ exists
                    ")(" +
                    "[uUoO]" + // condition to check
                    ").*");
            Matcher m = pattern.matcher(individualBlock.trim());
            if (m.matches()) {
                Bid bid = DefaultFactory.Bid.constructDefault();
                bid.setBidAmount(m.group(1));
                bid.setCondition(BidCondition.match(m.group(2)));
                bid.createID();
                gameFromHtmlBlock.getBidList().add(bid);
            }
        }
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
}
