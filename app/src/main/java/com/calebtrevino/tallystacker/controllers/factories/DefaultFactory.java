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
        public static final com.calebtrevino.tallystacker.models.Team FIRST_TEAM = Team.constructDefault();
        public static final com.calebtrevino.tallystacker.models.Team SECOND_TEAM = Team.constructDefault();
        public static final com.calebtrevino.tallystacker.controllers.sources.League LEAGUE_TYPE = League.constructDefault();
        public static final long GAME_DATE_TIME = 0;
        public static final ScoreType SCORE_TYPE = ScoreType.DEFAULT;
        public static final List<com.calebtrevino.tallystacker.models.Bid> BID_LIST = new ArrayList<>();

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

            return newInstance;
        }
    }

    public static final class UpdatePageMarker {
        public static final String DEFAULT_NEXT_PAGE_URL = "No Next Page Url";
        public static final int DEFAULT_LAST_GAME_POSITION = 0;

        private UpdatePageMarker() {
            throw new AssertionError();
        }
    }

    public static final class Team {
        public static final Long ID = 0L;
        public static final String CITY = "No City";
        public static final String NAME = "No Name";
        public static final String ACRONYM = "No Acronym";
        public static final com.calebtrevino.tallystacker.controllers.sources.League LEAGUE_TYPE = League.constructDefault();

        private Team() {
            throw new AssertionError();
        }

        public static com.calebtrevino.tallystacker.models.Team constructDefault() {
            com.calebtrevino.tallystacker.models.Team newInstance = new com.calebtrevino.tallystacker.models.Team();
            newInstance.set_id(ID);
            newInstance.setCity(CITY);
            newInstance.setName(NAME);
            newInstance.setAcronym(ACRONYM);
            newInstance.setLeagueType(LEAGUE_TYPE);
            return newInstance;
        }
    }

    public static final class League {

        public static final ScoreType SCORE_TYPE = ScoreType.DEFAULT;
        public static final String NAME = "No Name";
        public static final String ACRONYM = "No Acronym";
        public static final String BASE_URL = "No Base Url";
        public static final String CSS_QUERY = "No CSS QUERY";


        private League() {
            throw new AssertionError();
        }

        public static com.calebtrevino.tallystacker.controllers.sources.League constructDefault() {
            com.calebtrevino.tallystacker.controllers.sources.League newInstance = new com.calebtrevino.tallystacker.controllers.sources.bases.LeagueBase() {
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
                protected void createGameInfo(String text, com.calebtrevino.tallystacker.models.Game gameFromHtmlBlock) {
                }

                @Override
                protected void createBidInfo(String text, com.calebtrevino.tallystacker.models.Game gameFromHtmlBlock) {
                }

            };
            return newInstance;
        }
    }

    public static final class Bid {
        public static final long ID = 0L;
        public static final ScoreType SCORE_TYPE = ScoreType.DEFAULT;
        public static final float BID_AMOUNT = 0F;
        public static final BidCondition CONDITION = BidCondition.DEFAULT;
        public static final BidResult RESULT = BidResult.NEUTRAL;


        private Bid() {
            throw new AssertionError();
        }


        public static com.calebtrevino.tallystacker.models.Bid constructDefault() {
            com.calebtrevino.tallystacker.models.Bid newInstance = new com.calebtrevino.tallystacker.models.Bid();
            newInstance.set_id(ID);
            newInstance.setScoreType(SCORE_TYPE);
            newInstance.setBidAmount(BID_AMOUNT);
            newInstance.setCondition(CONDITION);
            newInstance.setResult(RESULT);
            return newInstance;
        }
    }

    public static final class Grid {
        public static final long ID = 0;
        public static final String GRID_NAME = "No Name";
        public static final int ROW_NO = 15;
        public static final int COLUMN_NO = 65;
        public static final List<com.calebtrevino.tallystacker.models.Game> GAME_LIST = new ArrayList<>();
        public static final boolean KEEP_UPDATES = true;
        public static final boolean FORCE_ADD = false;
        public static final List<com.calebtrevino.tallystacker.models.GridLeagues> GRID_LEAGUES = new ArrayList<>();

        private Grid() {
            throw new AssertionError();
        }

        public static com.calebtrevino.tallystacker.models.Grid constructDefault() {
            com.calebtrevino.tallystacker.models.Grid newInstance = new com.calebtrevino.tallystacker.models.Grid();
            newInstance.set_id(ID);
            newInstance.setGridName(GRID_NAME);
            newInstance.setRowNo(ROW_NO);
            newInstance.setColumnNo(COLUMN_NO);
            newInstance.setGameList(GAME_LIST);
            newInstance.setKeepUpdates(KEEP_UPDATES);
            newInstance.setForceAdd(FORCE_ADD);
            newInstance.setGridLeagues(GRID_LEAGUES);
            return newInstance;
        }
    }

    public static final class GridLeagues {
        public static final com.calebtrevino.tallystacker.controllers.sources.League LEAGUE = League.constructDefault();
        public static final int START_NO = 0;
        public static final int END_NUMBER = 0;

        private GridLeagues() {
            throw new AssertionError();
        }

        public static com.calebtrevino.tallystacker.models.GridLeagues constructDefault() {
            com.calebtrevino.tallystacker.models.GridLeagues newInstance = new com.calebtrevino.tallystacker.models.GridLeagues();
            newInstance.setLeague(LEAGUE);
            newInstance.setStartNo(START_NO);
            newInstance.setEndNumber(END_NUMBER);
            return newInstance;
        }
    }
}
