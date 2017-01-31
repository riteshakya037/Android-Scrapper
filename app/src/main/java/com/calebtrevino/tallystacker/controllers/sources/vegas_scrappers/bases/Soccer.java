package com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.bases;

import android.os.Parcel;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.controllers.factories.DefaultFactory;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.Team;
import com.calebtrevino.tallystacker.models.enums.ScoreType;
import com.calebtrevino.tallystacker.utils.ParseUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ritesh Shakya
 */
public abstract class Soccer extends LeagueBase {
    private ScoreType BID_SCORE_TYPE = ScoreType.SPREAD;
    private String NAME = "Soccer";
    private String ACRONYM = "Soccer";
    private String CSS_QUERY = "table.frodds-data-tbl > tbody>tr:has(td:not(.game-notes))";

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
        return R.raw.soccer_teams;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }

    @Override
    public void createGameInfo(String bodyText, Game gameFromHtmlBlock) {
        // Header: 09/08 8:30 PM 451 Carolina 452 Denver
        Pattern pattern = Pattern.compile("([0-9]{2}/[0-9]{2}" + // Date of match
                "\\s" + "[0-9]{1,2}:[0-9]{2}" + "\\s" + "[A|P]M)" + // Time of match
                "br2n " + "([0-9]{6})" + // First team code
                ".?(\\w.*)br2n " + // First team city
                "([0-9]{6})" + // Second team code
                ".?(\\w.*)br2n " +// Second team city
                "([0-9]{6})" +
                ".?Drawbr2n " +
                "([0-9]{6})" +
                ".?Totalbr2n"
        );
//        "([0-9]{2}/[0-9]{2})\\s([0-9]{1,2}:[0-9]{2}\\s[A|P]M)\\\\n ([0-9]{6}).?(\\w.*)\\\\n ([0-9]{6}).?(\\w.*)\\\\n [0-9].*"
        Matcher m = pattern.matcher(bodyText);
        if (m.matches()) {
            // Initialize gameFromHtmlBlock
            gameFromHtmlBlock.setGameDateTime(ParseUtils.parseDate(m.group(1)));
            gameFromHtmlBlock.setGameAddDate();

            Team firstTeam = DefaultFactory.Team.constructDefault();
            firstTeam.setLeagueType(this);
            firstTeam.set_teamId(Long.valueOf(m.group(2)));
            firstTeam.setCity(m.group(3));
            firstTeam.createID();
            gameFromHtmlBlock.setFirstTeam(firstTeam);

            Team secondTeam = DefaultFactory.Team.constructDefault();
            secondTeam.setLeagueType(this);
            secondTeam.set_teamId(Long.valueOf(m.group(4)));
            secondTeam.setCity(m.group(5));
            secondTeam.createID();
            gameFromHtmlBlock.setSecondTeam(secondTeam);
        }
    }
}
