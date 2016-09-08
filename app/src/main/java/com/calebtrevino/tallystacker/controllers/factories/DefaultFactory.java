package com.calebtrevino.tallystacker.controllers.factories;

import android.os.Parcel;

import com.calebtrevino.tallystacker.models.enums.BidCondition;
import com.calebtrevino.tallystacker.models.enums.BidResult;
import com.calebtrevino.tallystacker.models.enums.ScoreType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fatal on 9/3/2016.
 */

public class DefaultFactory {
    private DefaultFactory() {
        throw new AssertionError();
    }

    public static final class Game {
        static final com.calebtrevino.tallystacker.models.Team FIRST_TEAM = Team.constructDefault();
        static final com.calebtrevino.tallystacker.models.Team SECOND_TEAM = Team.constructDefault();
        static final com.calebtrevino.tallystacker.controllers.sources.League LEAGUE_TYPE = League.constructDefault();
        static final long GAME_DATE_TIME = 0;
        static final ScoreType SCORE_TYPE = ScoreType.DEFAULT;
        static final List<com.calebtrevino.tallystacker.models.Bid> BID_LIST = new ArrayList<>();
        static final BidResult RESULT = BidResult.NEUTRAL;
        static final long FIRST_TEAM_SCORE = 0L;
        static final long SECOND_TEAM_SCORE = 0L;

        private Game() {
            throw new AssertionError();
        }

        public static com.calebtrevino.tallystacker.models.Game constructDefault() {
            com.calebtrevino.tallystacker.models.Game newInstance = new com.calebtrevino.tallystacker.models.Game();
            newInstance.setFirstTeam(FIRST_TEAM);
            newInstance.setSecondTeam(SECOND_TEAM);
            newInstance.setLeagueType(LEAGUE_TYPE);
            newInstance.setGameDateTime(GAME_DATE_TIME);
            newInstance.setScoreType(SCORE_TYPE);
            newInstance.setBidList(BID_LIST);
            newInstance.setBidResult(RESULT);
            newInstance.setFirstTeamScore(FIRST_TEAM_SCORE);
            newInstance.setSecondTeamScore(SECOND_TEAM_SCORE);
            newInstance.createID();
            return newInstance;
        }
    }

    public static final class Team {
        static final String CITY = "No City";
        static final String NAME = "No Name";
        static final String ACRONYM = "No Acronym";
        static final com.calebtrevino.tallystacker.controllers.sources.League LEAGUE_TYPE = League.constructDefault();

        private Team() {
            throw new AssertionError();
        }

        public static com.calebtrevino.tallystacker.models.Team constructDefault() {
            com.calebtrevino.tallystacker.models.Team newInstance = new com.calebtrevino.tallystacker.models.Team();
            newInstance.setCity(CITY);
            newInstance.setName(NAME);
            newInstance.setAcronym(ACRONYM);
            newInstance.setLeagueType(LEAGUE_TYPE);
            newInstance.createID();
            return newInstance;
        }
    }

    public static final class League {

        static final ScoreType SCORE_TYPE = ScoreType.DEFAULT;
        static final String NAME = "No Name";
        static final String ACRONYM = "No Acronym";
        static final String BASE_URL = "No Base Url";
        static final String CSS_QUERY = "No CSS QUERY";


        private League() {
            throw new AssertionError();
        }

        public static com.calebtrevino.tallystacker.controllers.sources.League constructDefault() {
            return new com.calebtrevino.tallystacker.controllers.sources.bases.LeagueBase() {
                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel parcel, int i) {
                    parcel.writeParcelable(SCORE_TYPE, i);
                    parcel.writeString(NAME);
                    parcel.writeString(BASE_URL);
                    parcel.writeString(ACRONYM);
                    parcel.writeString(CSS_QUERY);
                }

                @Override
                public ScoreType getScoreType() {
                    return SCORE_TYPE;
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
                protected void createGameInfo(String text, com.calebtrevino.tallystacker.models.Game gameFromHtmlBlock) {
                }

                @Override
                protected void createBidInfo(String text, com.calebtrevino.tallystacker.models.Game gameFromHtmlBlock) {
                }

            };
        }
    }

    public static final class Bid {
        static final float BID_AMOUNT = 0F;
        static final BidCondition CONDITION = BidCondition.DEFAULT;


        private Bid() {
            throw new AssertionError();
        }


        public static com.calebtrevino.tallystacker.models.Bid constructDefault() {
            com.calebtrevino.tallystacker.models.Bid newInstance = new com.calebtrevino.tallystacker.models.Bid();
            newInstance.setBidAmount(BID_AMOUNT);
            newInstance.setCondition(CONDITION);
            newInstance.createID();
            return newInstance;
        }
    }

    public static final class Grid {
        static final String GRID_NAME = "No Name";
        static final int ROW_NO = 15;
        static final int COLUMN_NO = 65;
        static final List<com.calebtrevino.tallystacker.models.Game> GAME_LIST = new ArrayList<>();
        static final boolean KEEP_UPDATES = true;
        static final boolean FORCE_ADD = false;
        static final List<com.calebtrevino.tallystacker.models.GridLeagues> GRID_LEAGUES = new ArrayList<>();

        private Grid() {
            throw new AssertionError();
        }

        public static com.calebtrevino.tallystacker.models.Grid constructDefault() {
            com.calebtrevino.tallystacker.models.Grid newInstance = new com.calebtrevino.tallystacker.models.Grid();
            newInstance.setGridName(GRID_NAME);
            newInstance.setRowNo(ROW_NO);
            newInstance.setColumnNo(COLUMN_NO);
            newInstance.setGameList(GAME_LIST);
            newInstance.setKeepUpdates(KEEP_UPDATES);
            newInstance.setForceAdd(FORCE_ADD);
            newInstance.setGridLeagues(GRID_LEAGUES);
            newInstance.createID();
            return newInstance;
        }
    }

    public static final class GridLeagues {
        static final com.calebtrevino.tallystacker.controllers.sources.League LEAGUE = League.constructDefault();
        static final int START_NO = 0;
        static final int END_NUMBER = 0;

        private GridLeagues() {
            throw new AssertionError();
        }

        public static com.calebtrevino.tallystacker.models.GridLeagues constructDefault() {
            com.calebtrevino.tallystacker.models.GridLeagues newInstance = new com.calebtrevino.tallystacker.models.GridLeagues();
            newInstance.setLeague(LEAGUE);
            newInstance.setStartNo(START_NO);
            newInstance.setEndNumber(END_NUMBER);
            newInstance.createID();
            return newInstance;
        }
    }
}
