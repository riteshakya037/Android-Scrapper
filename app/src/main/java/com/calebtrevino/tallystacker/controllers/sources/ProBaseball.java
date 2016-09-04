package com.calebtrevino.tallystacker.controllers.sources;

import com.calebtrevino.tallystacker.controllers.factories.DefaultFactory;
import com.calebtrevino.tallystacker.controllers.sources.bases.LeagueBase;
import com.calebtrevino.tallystacker.models.Bid;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.Team;
import com.calebtrevino.tallystacker.models.enums.ScoreType;
import com.calebtrevino.tallystacker.utils.ParseUtils;


/**
 * Created by fatal on 9/3/2016.
 */

public class ProBaseball extends LeagueBase {
    private static final String TAG = ProBaseball.class.getSimpleName();

    private static ScoreType BID_SCORE_TYPE = ScoreType.TOTAL;
    private static String NAME = "Pro Baseball";
    private static String BASE_URL = "http://www.vegasinsider.com/mlb/odds/las-vegas/";

    private static String ACRONYM = "MLB";

    private static String CSS_QUERY = "table.frodds-data-tbl > tbody>tr:has(td:not(.game-notes))";



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
    protected String getErrorMessage() {
        return "404 - File or directory not found";
    }


    @Override
    protected void createGame(String bodyText, Game gameFromHtmlBlock) {
        String[] bodyParts = bodyText.split("\\s");
        if (bodyParts.length == 7) { // Header: 09/08 8:30 PM 451 Carolina 452 Denver
            // initialize gameFromHtmlBlock
            gameFromHtmlBlock.setGameDateTime(ParseUtils.parseDate(bodyParts[0], bodyParts[1] + bodyParts[2], "MM/dd", "hh:mmaa"));

            Team firstTeam = DefaultFactory.Team.constructDefault();
            firstTeam.setLeagueType(this);
            firstTeam.set_id(Long.valueOf(bodyParts[3]));
            firstTeam.setCity(bodyParts[4]);
            gameFromHtmlBlock.setFirstTeam(firstTeam);


            Team secondTeam = DefaultFactory.Team.constructDefault();
            secondTeam.setLeagueType(this);
            secondTeam.set_id(Long.valueOf(bodyParts[5]));
            secondTeam.setCity(bodyParts[6]);
            gameFromHtmlBlock.setSecondTeam(secondTeam);

        }
    }

    @Override
    protected void createBid(String text, Game gameFromHtmlBlock) {
        Bid bid = DefaultFactory.Bid.constructDefault();
        //1 -10 44u-10, 3 -25 41½u-10, 3 -20 41½u-10, 3 -15 41½u-10
        gameFromHtmlBlock.getBidList().add(new Bid());
    }



}
