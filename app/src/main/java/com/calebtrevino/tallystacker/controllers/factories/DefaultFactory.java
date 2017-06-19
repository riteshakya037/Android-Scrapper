package com.calebtrevino.tallystacker.controllers.factories;

import android.os.Parcel;
import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.models.enums.BidCondition;
import com.calebtrevino.tallystacker.models.enums.BidResult;
import com.calebtrevino.tallystacker.models.enums.GameStatus;
import com.calebtrevino.tallystacker.models.enums.GridMode;
import com.calebtrevino.tallystacker.models.enums.ScoreType;
import java.util.LinkedList;
import java.util.List;

import static com.calebtrevino.tallystacker.utils.Constants.VALUES.SOCCER_MIN_VALUE;

/**
 * A default class which create factory objects of various models used in the application.
 *
 * @author Ritesh Shakya
 */
@SuppressWarnings("WeakerAccess") public class DefaultFactory {
    private DefaultFactory() {
        throw new AssertionError();
    }

    public static final class Game {
        public static final com.calebtrevino.tallystacker.models.Team FIRST_TEAM =
                Team.constructDefault();
        public static final com.calebtrevino.tallystacker.models.Team SECOND_TEAM =
                Team.constructDefault();
        public static final com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.bases.League
                LEAGUE_TYPE = League.constructDefault();
        public static final long GAME_DATE_TIME = 0;
        public static final ScoreType SCORE_TYPE = ScoreType.DEFAULT;
        public static final BidResult RESULT = BidResult.NEUTRAL;
        public static final GameStatus GAME_STATUS = GameStatus.NEUTRAL;
        public static final long FIRST_TEAM_SCORE = 0L;
        public static final long SECOND_TEAM_SCORE = 0L;
        public static final String GAME_URL = "";

        private Game() {
            throw new AssertionError();
        }

        public static com.calebtrevino.tallystacker.models.Game constructDefault() {
            com.calebtrevino.tallystacker.models.Game newInstance =
                    new com.calebtrevino.tallystacker.models.Game();
            newInstance.setFirstTeam(FIRST_TEAM);
            newInstance.setSecondTeam(SECOND_TEAM);
            newInstance.setLeagueType(LEAGUE_TYPE);
            newInstance.setGameDateTime(GAME_DATE_TIME);
            newInstance.setScoreType(SCORE_TYPE);
            newInstance.setBidList(new LinkedList<com.calebtrevino.tallystacker.models.Bid>());
            newInstance.setBidResult(RESULT);
            newInstance.setFirstTeamScore(FIRST_TEAM_SCORE);
            newInstance.setSecondTeamScore(SECOND_TEAM_SCORE);
            newInstance.setGameUrl(GAME_URL);
            newInstance.setGameStatus(GAME_STATUS);
            newInstance.setVIBid();
            newInstance.createID();
            return newInstance;
        }
    }

    public static final class Team {
        public static final String NAME = "No Name";
        public static final String ACRONYM = "No Acronym";
        public static final String CITY = "No City";
        public static final com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.bases.League
                LEAGUE_TYPE = League.constructDefault();

        private Team() {
            throw new AssertionError();
        }

        public static com.calebtrevino.tallystacker.models.Team constructDefault() {
            com.calebtrevino.tallystacker.models.Team newInstance =
                    new com.calebtrevino.tallystacker.models.Team();
            newInstance.setCity(CITY);
            newInstance.setName(NAME);
            newInstance.setAcronym(ACRONYM);
            newInstance.setLeagueType(LEAGUE_TYPE);
            newInstance.createID();
            return newInstance;
        }
    }

    public static final class League {

        public static final long REFRESH_INTERVAL = 15;
        public static final ScoreType SCORE_TYPE = ScoreType.DEFAULT;
        public static final String NAME = "No Name";
        public static final String ACRONYM = "No Acronym";
        public static final String BASE_URL = "No Base Url";
        public static final String CSS_QUERY = "No CSS QUERY";

        private League() {
            throw new AssertionError();
        }

        public static com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.bases.League constructDefault() {
            return new com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.bases.LeagueBase() {
                @Override public int describeContents() {
                    return 0;
                }

                @Override public void writeToParcel(Parcel parcel, int i) {
                    parcel.writeParcelable(SCORE_TYPE, i);
                    parcel.writeString(NAME);
                    parcel.writeString(BASE_URL);
                    parcel.writeString(ACRONYM);
                    parcel.writeString(CSS_QUERY);
                }

                @Override public ScoreType getScoreType() {
                    return SCORE_TYPE;
                }

                @Override public String getName() {
                    return NAME;
                }

                @Override public String getAcronym() {
                    return ACRONYM;
                }

                @Override public String getBaseUrl() {
                    return BASE_URL;
                }

                @Override public String getCSSQuery() {
                    return CSS_QUERY;
                }

                @Override public String getPackageName() {
                    return getClass().getName();
                }

                @Override public long getRefreshInterval() {
                    return REFRESH_INTERVAL;
                }

                @Override public void setRefreshInterval(long refreshInterval) {
                    System.out.println("refreshInterval = " + refreshInterval);
                }

                @Override public String getBaseScoreUrl() {
                    return "";
                }

                @Override public int getTeamResource() {
                    return R.raw.nfl_teams;
                }

                @Override public int getAvgTime() {
                    return 90;
                }

                @Override public boolean hasSecondPhase() {
                    return false;
                }

                @Override public String getScoreBoardURL() {
                    return "";
                }
            };
        }
    }

    public static final class Bid {
        public static final float BID_AMOUNT = 0F;
        public static final float VIG_AMOUNT = SOCCER_MIN_VALUE - 1F;
        public static final BidCondition CONDITION = BidCondition.DEFAULT;

        private Bid() {
            throw new AssertionError();
        }

        public static com.calebtrevino.tallystacker.models.Bid constructDefault() {
            com.calebtrevino.tallystacker.models.Bid newInstance =
                    new com.calebtrevino.tallystacker.models.Bid();
            newInstance.setBidAmount(BID_AMOUNT);
            newInstance.setCondition(CONDITION);
            newInstance.setVigAmount(VIG_AMOUNT);
            newInstance.createID();
            return newInstance;
        }
    }

    public static final class Grid {
        public static final int ROW_NO = 15;
        public static final int COLUMN_NO = 65;
        public static final String GRID_NAME = "No Name";
        public static final boolean KEEP_UPDATES = true;
        public static final List<com.calebtrevino.tallystacker.models.GridLeagues> GRID_LEAGUES =
                new LinkedList<>();
        public static final GridMode GRID_MODE = GridMode.TALLY_COUNT;
        public static final int MAX_TALLY_COUNT = 10;
        public static final int GRID_TOTAL_COUNT = 3;

        private Grid() {
            throw new AssertionError();
        }

        public static com.calebtrevino.tallystacker.models.Grid constructDefault() {
            com.calebtrevino.tallystacker.models.Grid newInstance =
                    new com.calebtrevino.tallystacker.models.Grid();
            newInstance.setGridName(GRID_NAME);
            newInstance.setRowNo(ROW_NO);
            newInstance.setColumnNo(COLUMN_NO);
            newInstance.setGameList(new LinkedList<com.calebtrevino.tallystacker.models.Game>());
            newInstance.setKeepUpdates(KEEP_UPDATES);
            newInstance.setGridLeagues(GRID_LEAGUES);
            newInstance.setGridMode(GRID_MODE);
            newInstance.setGridTotalCount(GRID_TOTAL_COUNT);
            newInstance.createID();
            return newInstance;
        }
    }

    public static final class GridLeagues {
        public static final com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.bases.League
                LEAGUE = League.constructDefault();
        public static final int START_NO = 0;
        public static final int END_NUMBER = 0;
        public static final boolean FORCE_ADD = false;

        private GridLeagues() {
            throw new AssertionError();
        }

        public static com.calebtrevino.tallystacker.models.GridLeagues constructDefault() {
            com.calebtrevino.tallystacker.models.GridLeagues newInstance =
                    new com.calebtrevino.tallystacker.models.GridLeagues();
            newInstance.setLeague(LEAGUE);
            newInstance.setStartNo(START_NO);
            newInstance.setEndNumber(END_NUMBER);
            newInstance.setForceAdd(FORCE_ADD);
            newInstance.createID();
            return newInstance;
        }
    }
}
