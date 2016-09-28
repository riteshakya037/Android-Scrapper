package com.calebtrevino.tallystacker.models.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.calebtrevino.tallystacker.controllers.factories.DefaultFactory;
import com.calebtrevino.tallystacker.controllers.sources.League;
import com.calebtrevino.tallystacker.models.Bid;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.Grid;
import com.calebtrevino.tallystacker.models.GridLeagues;
import com.calebtrevino.tallystacker.models.Team;
import com.calebtrevino.tallystacker.models.enums.BidResult;
import com.calebtrevino.tallystacker.models.enums.ScoreType;
import com.calebtrevino.tallystacker.models.listeners.ChildGameEventListener;

import org.joda.time.DateTime;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Ritesh Shakya
 */

public class DatabaseContract {

    private static final String TEXT_TYPE = " TEXT";
    private static final String BOOLEAN_TYPE = " INTEGER";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String AND_SEP = " AND ";

    public DatabaseContract() {

    }

    static abstract class GameEntry implements BaseColumns {
        static final String TABLE_NAME = "game_table";

        static final String COLUMN_FIRST_TEAM = "first_team";            // Team
        static final String COLUMN_SECOND_TEAM = "second_team";          // Team
        static final String COLUMN_LEAGUE_TYPE = "league_type";          // League
        static final String COLUMN_GAME_DATE_TIME = "game_date_time";    // long
        static final String COLUMN_GAME_ADD_DATE = "game_added_time";  // long
        static final String COLUMN_SCORE_TYPE = "score_type";            // ScoreType
        static final String COLUMN_BID_LIST = "bid_list";                // BID_LIST
        static final String COLUMN_BID_RESULT = "bid_result";            // Result
        static final String COLUMN_FIRST_TEAM_SCORE = "first_team_score";      // Long
        static final String COLUMN_SECOND_TEAM_SCORE = "second_team_score";    // Long
        static final String COLUMN_UPDATED_ON = "updated_on";            // long

        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY," +
                        COLUMN_FIRST_TEAM + TEXT_TYPE + COMMA_SEP +
                        COLUMN_SECOND_TEAM + TEXT_TYPE + COMMA_SEP +
                        COLUMN_LEAGUE_TYPE + TEXT_TYPE + COMMA_SEP +
                        COLUMN_GAME_DATE_TIME + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_GAME_ADD_DATE + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_SCORE_TYPE + TEXT_TYPE + COMMA_SEP +
                        COLUMN_BID_LIST + TEXT_TYPE + COMMA_SEP +
                        COLUMN_BID_RESULT + TEXT_TYPE + COMMA_SEP +
                        COLUMN_FIRST_TEAM_SCORE + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_SECOND_TEAM_SCORE + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_UPDATED_ON + INTEGER_TYPE +
                        " )";

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    static abstract class TeamEntry implements BaseColumns {
        static final String TABLE_NAME = "team_table";

        static final String COLUMN_TEAM_ID = "team_id";                  // String
        static final String COLUMN_CITY = "city";                        // String
        static final String COLUMN_NAME = "name";                        // String
        static final String COLUMN_ACRONYM = "acronym";                  // String
        static final String COLUMN_LEAGUE_TYPE = "league_type";          // League

        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY," +
                        COLUMN_TEAM_ID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_CITY + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                        COLUMN_ACRONYM + TEXT_TYPE + COMMA_SEP +
                        COLUMN_LEAGUE_TYPE + TEXT_TYPE +
                        " )";

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    static abstract class LeagueEntry implements BaseColumns {
        static final String TABLE_NAME = "league_table";

        static final String COLUMN_CLASSPATH = "classpath";
        static final String REFRESH_INTERVAL = "refresh_interval";

        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY," +
                        COLUMN_CLASSPATH + TEXT_TYPE + COMMA_SEP +
                        REFRESH_INTERVAL + TEXT_TYPE +
                        " )";

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    static abstract class GridEntry implements BaseColumns {
        static final String TABLE_NAME = "grid_table";

        static final String COLUMN_GRID_NAME = "grid_name";              // String
        static final String COLUMN_ROW_NO = "row_no";                    // Int
        static final String COLUMN_COLUMN_NO = "column_no";              // Int
        static final String COLUMN_GAME_LIST = "game_list";              // Game List
        static final String COLUMN_KEEP_UPDATES = "keep_updates";        // Bool
        static final String COLUMN_GRID_LEAGUES = "grid_leagues";        // Grid Leagues List
        static final String COLUMN_UPDATED_ON = "updated_on";        // Long


        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY," +
                        COLUMN_GRID_NAME + TEXT_TYPE + COMMA_SEP +
                        COLUMN_ROW_NO + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_COLUMN_NO + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_GAME_LIST + TEXT_TYPE + COMMA_SEP +
                        COLUMN_KEEP_UPDATES + BOOLEAN_TYPE + COMMA_SEP +
                        COLUMN_GRID_LEAGUES + TEXT_TYPE + COMMA_SEP +
                        COLUMN_UPDATED_ON + INTEGER_TYPE +
                        " )";

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class DbHelper extends SQLiteOpenHelper {

        static final int DATABASE_VERSION = 1;
        static final String DATABASE_NAME = "tally_stacker.db";
        private static List<ChildGameEventListener> childGameEventListener;

        public DbHelper(Context activity) {
            super(activity.getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);
            if (childGameEventListener == null) {
                childGameEventListener = new LinkedList<>();
            }
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(GameEntry.SQL_CREATE_ENTRIES);
            sqLiteDatabase.execSQL(TeamEntry.SQL_CREATE_ENTRIES);
            sqLiteDatabase.execSQL(LeagueEntry.SQL_CREATE_ENTRIES);
            sqLiteDatabase.execSQL(GridEntry.SQL_CREATE_ENTRIES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

            sqLiteDatabase.execSQL(GameEntry.SQL_DELETE_ENTRIES);
            sqLiteDatabase.execSQL(TeamEntry.SQL_DELETE_ENTRIES);
            sqLiteDatabase.execSQL(LeagueEntry.SQL_DELETE_ENTRIES);
            sqLiteDatabase.execSQL(GridEntry.SQL_DELETE_ENTRIES);

            onCreate(sqLiteDatabase);
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }


        public void onInsertGame(List<Game> data) {
            boolean once = true;
            for (Game gameData : data) {
                if (once) {
                    onInsertLeague(gameData.getLeagueType());
                    once = false;
                }
                // check if available: if yes update
                long databaseId = checkForGame(gameData.getLeagueType(), gameData.getFirstTeam(), gameData.getSecondTeam(), gameData.getGameDateTime());
                if (databaseId == 0L) {
                    onInsetGame(gameData);
                } else {
                    onUpdateGame(databaseId, gameData);
                }
            }
        }

        public void addGamesToGrids() {
            long dateToday = new DateTime().withTimeAtStartOfDay().getMillis();
            List<Grid> gridList = getGrids();
            for (Grid grid : gridList) {
                if (grid.getUpdatedOn() != dateToday && grid.isKeepUpdates()) {
                    addGamesToGrid(grid);
                }
            }
        }

        public void addGamesToGrid(Grid grid) {
            List<GridLeagues> gridLeaguesList = grid.getGridLeagues();
            List<Game> gameList = grid.getGameList();
            for (GridLeagues gridLeague : gridLeaguesList) {
                onSelectGames(gridLeague, gameList);
            }
            grid.setGameList(gameList);
            grid.setUpdatedOn(new DateTime().withTimeAtStartOfDay().getMillis());
            long databaseId = checkForGrid(grid.get_id());
            if (databaseId == 0L) {
                onInsertGrid(grid);
            } else {
                onUpdateGrid(grid.get_id(), grid);
            }
        }

        private long checkForGrid(long gameId) {
            SQLiteDatabase db = getReadableDatabase();

            String[] projection = {
                    GridEntry._ID};

            String selection = GridEntry._ID + " = ?";

            String[] selectionArgs = {
                    String.valueOf(gameId)
            };
            Cursor res = db.query(
                    true,
                    GridEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null,
                    null
            );
            if (res.getCount() <= 0) {
                res.close();
                return 0L;
            }
            res.moveToFirst();
            long id = res.getLong(res.getColumnIndex(GameEntry._ID));
            res.close();
            return id;
        }

        private void onSelectGames(GridLeagues gridLeague, List<Game> gameList) {
            League league = gridLeague.getLeague();
            List<Game> addedGames = onSelectGame(league.getPackageName(), new DateTime().withTimeAtStartOfDay().getMillis());
            int i = gridLeague.getStartNo();
            for (Game game : addedGames) {
                if (!gameList.contains(game) && game.getBidList().size() > 2) {
                    gameList.add(game);
                    i++;
                }
                if (i > gridLeague.getEndNumber()) {
                    break;
                }
            }
        }

        private List<Game> onSelectGame(String leaguePackageName, long dateToday) {
            SQLiteDatabase db = getReadableDatabase();

            String[] projection = {
                    GameEntry._ID,
                    GameEntry.COLUMN_FIRST_TEAM,
                    GameEntry.COLUMN_SECOND_TEAM,
                    GameEntry.COLUMN_LEAGUE_TYPE,
                    GameEntry.COLUMN_GAME_DATE_TIME,
                    GameEntry.COLUMN_GAME_ADD_DATE,
                    GameEntry.COLUMN_SCORE_TYPE,
                    GameEntry.COLUMN_BID_LIST,
                    GameEntry.COLUMN_BID_RESULT,
                    GameEntry.COLUMN_FIRST_TEAM_SCORE,
                    GameEntry.COLUMN_SECOND_TEAM_SCORE,
            };
            String selection = GameEntry.COLUMN_LEAGUE_TYPE + " = ? " + AND_SEP +
                    GameEntry.COLUMN_GAME_ADD_DATE + " = ? ";
            String sortOrder =
                    GameEntry.COLUMN_UPDATED_ON + " ASC";

            String[] selectionArgs = {leaguePackageName, String.valueOf(dateToday)};

            Cursor res = db.query(
                    true,
                    GameEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder,
                    null
            );
            res.moveToFirst();
            List<Game> gameList = new LinkedList<>();
            while (!res.isAfterLast()) {
                try {
                    Game game = DefaultFactory.Game.constructDefault();
                    game.set_id(
                            res.getLong(res.getColumnIndex(
                                    GameEntry._ID)));
                    game.setFirstTeam(onSelectTeam(
                            res.getString(res.getColumnIndex(GameEntry.COLUMN_LEAGUE_TYPE)),
                            res.getString(res.getColumnIndex(GameEntry.COLUMN_FIRST_TEAM))
                    ));
                    game.setSecondTeam(onSelectTeam(
                            res.getString(res.getColumnIndex(GameEntry.COLUMN_LEAGUE_TYPE)),
                            res.getString(res.getColumnIndex(GameEntry.COLUMN_SECOND_TEAM))));

                    game.setLeagueType((League) Class.forName(
                            res.getString(res.getColumnIndex(
                                    GameEntry.COLUMN_LEAGUE_TYPE))).newInstance());
                    game.setGameDateTime(
                            res.getLong(res.getColumnIndex(
                                    GameEntry.COLUMN_GAME_DATE_TIME)));
                    game.setGameAddDate(
                            res.getLong(res.getColumnIndex(
                                    GameEntry.COLUMN_GAME_ADD_DATE)));
                    game.setScoreType(ScoreType.match(
                            res.getString(res.getColumnIndex(
                                    GameEntry.COLUMN_SCORE_TYPE))));
                    game.setBidList(Bid.createArrayFromJson(
                            res.getString(res.getColumnIndex(
                                    GameEntry.COLUMN_BID_LIST))));
                    game.setBidResult(BidResult.match(
                            res.getString(res.getColumnIndex(
                                    GameEntry.COLUMN_BID_RESULT))));
                    game.setFirstTeamScore(
                            res.getInt(res.getColumnIndex(
                                    GameEntry.COLUMN_FIRST_TEAM_SCORE)));
                    game.setSecondTeamScore
                            (res.getInt(res.getColumnIndex(
                                    GameEntry.COLUMN_SECOND_TEAM_SCORE)));
                    gameList.add(game);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                res.moveToNext();
            }
            res.close();
            return gameList;
        }

        private void onUpdateGame(long databaseId, final Game gameData) {
            SQLiteDatabase db = getReadableDatabase();

            ContentValues values = new ContentValues();
            values.put(GameEntry.COLUMN_FIRST_TEAM, gameData.getFirstTeam().get_teamID());
            values.put(GameEntry.COLUMN_SECOND_TEAM, gameData.getSecondTeam().get_teamID());
            values.put(GameEntry.COLUMN_LEAGUE_TYPE, gameData.getLeagueType().getPackageName());
            values.put(GameEntry.COLUMN_GAME_DATE_TIME, gameData.getGameDateTime());
            values.put(GameEntry.COLUMN_SCORE_TYPE, gameData.getScoreType().getValue());
            values.put(GameEntry.COLUMN_BID_LIST, Bid.createJsonArray(gameData.getBidList()));
            values.put(GameEntry.COLUMN_BID_RESULT, gameData.getBidResult().getValue());
            values.put(GameEntry.COLUMN_FIRST_TEAM_SCORE, gameData.getFirstTeamScore());
            values.put(GameEntry.COLUMN_SECOND_TEAM_SCORE, gameData.getSecondTeamScore());
            values.put(GameEntry.COLUMN_UPDATED_ON, new Date().getTime());


            for (ChildGameEventListener listener : childGameEventListener)
                listener.onChildChanged(gameData);

            String selection = GameEntry._ID + " = ?";
            String[] selectionArgs = {String.valueOf(databaseId)};

            db.update(GameEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);

            for (ChildGameEventListener listener : childGameEventListener)
                if (gameData.getBidList().size() > 2)
                    listener.onChildChanged(gameData);
        }

        private void onInsetGame(final Game gameData) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(GameEntry._ID, gameData.get_id());
            values.put(GameEntry.COLUMN_FIRST_TEAM, gameData.getFirstTeam().get_teamID());
            values.put(GameEntry.COLUMN_SECOND_TEAM, gameData.getSecondTeam().get_teamID());
            values.put(GameEntry.COLUMN_LEAGUE_TYPE, gameData.getLeagueType().getPackageName());
            values.put(GameEntry.COLUMN_GAME_DATE_TIME, gameData.getGameDateTime());
            values.put(GameEntry.COLUMN_GAME_ADD_DATE, gameData.getGameAddDate());
            values.put(GameEntry.COLUMN_SCORE_TYPE, gameData.getScoreType().getValue());
            values.put(GameEntry.COLUMN_BID_LIST, Bid.createJsonArray(gameData.getBidList()));
            values.put(GameEntry.COLUMN_BID_RESULT, gameData.getBidResult().getValue());
            values.put(GameEntry.COLUMN_FIRST_TEAM_SCORE, gameData.getFirstTeamScore());
            values.put(GameEntry.COLUMN_SECOND_TEAM_SCORE, gameData.getSecondTeamScore());
            values.put(GameEntry.COLUMN_UPDATED_ON, new Date().getTime());

            onInsertTeam(gameData.getFirstTeam());
            onInsertTeam(gameData.getSecondTeam());

            db.insert(
                    DatabaseContract.GameEntry.TABLE_NAME,
                    null,
                    values);

            for (ChildGameEventListener listener : childGameEventListener)
                if (gameData.getBidList().size() > 2)
                    listener.onChildAdded(gameData);
        }


        public List<Game> selectUpcomingGames(long currentDate) {
            SQLiteDatabase db = getReadableDatabase();

            String[] selectionArgs = {String.valueOf(currentDate)};
            List<Game> data = new LinkedList<>();
            Cursor res = db.rawQuery("SELECT " + GameEntry._ID +
                            " FROM " + GameEntry.TABLE_NAME +
                            " WHERE " + GameEntry.COLUMN_GAME_ADD_DATE + " = ? " +
                            " ORDER BY " + GameEntry.COLUMN_GAME_DATE_TIME,
                    selectionArgs);
            res.moveToFirst();

            while (!res.isAfterLast()) {
                Game game = onSelectGame(String.valueOf(res.getInt(res.getColumnIndex(GameEntry._ID))));
                if (game.getBidList().size() > 2) {
                    data.add(game);
                }
                res.moveToNext();

            }
            res.close();
            return data;
        }

        public Game onSelectGame(String gameId) {
            SQLiteDatabase db = getReadableDatabase();

            String[] projection = {
                    GameEntry._ID,
                    GameEntry.COLUMN_FIRST_TEAM,
                    GameEntry.COLUMN_SECOND_TEAM,
                    GameEntry.COLUMN_LEAGUE_TYPE,
                    GameEntry.COLUMN_GAME_DATE_TIME,
                    GameEntry.COLUMN_GAME_ADD_DATE,
                    GameEntry.COLUMN_SCORE_TYPE,
                    GameEntry.COLUMN_BID_LIST,
                    GameEntry.COLUMN_BID_RESULT,
                    GameEntry.COLUMN_FIRST_TEAM_SCORE,
                    GameEntry.COLUMN_SECOND_TEAM_SCORE,
            };
            String selection = GameEntry._ID + " = ?";
            String sortOrder =
                    GameEntry.COLUMN_UPDATED_ON + " DESC";

            String[] selectionArgs = {gameId};

            Cursor res = db.query(
                    true,
                    GameEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder,
                    null
            );
            res.moveToFirst();

            final Game game = DefaultFactory.Game.constructDefault();
            while (!res.isAfterLast()) {
                try {
                    game.set_id(
                            res.getLong(res.getColumnIndex(
                                    GameEntry._ID)));
                    game.setFirstTeam(onSelectTeam(
                            res.getString(res.getColumnIndex(GameEntry.COLUMN_LEAGUE_TYPE)),
                            res.getString(res.getColumnIndex(GameEntry.COLUMN_FIRST_TEAM))
                    ));

                    game.setSecondTeam(onSelectTeam(
                            res.getString(res.getColumnIndex(GameEntry.COLUMN_LEAGUE_TYPE)),
                            res.getString(res.getColumnIndex(GameEntry.COLUMN_SECOND_TEAM))));

                    game.setLeagueType(onSelectLeague(
                            res.getString(res.getColumnIndex(
                                    GameEntry.COLUMN_LEAGUE_TYPE))));
                    game.setGameDateTime(
                            res.getLong(res.getColumnIndex(
                                    GameEntry.COLUMN_GAME_DATE_TIME)));
                    game.setGameAddDate(
                            res.getLong(res.getColumnIndex(
                                    GameEntry.COLUMN_GAME_ADD_DATE)));
                    game.setScoreType(ScoreType.match(
                            res.getString(res.getColumnIndex(
                                    GameEntry.COLUMN_SCORE_TYPE))));
                    game.setBidList(Bid.createArrayFromJson(
                            res.getString(res.getColumnIndex(
                                    GameEntry.COLUMN_BID_LIST))));
                    game.setBidResult(BidResult.match(
                            res.getString(res.getColumnIndex(
                                    GameEntry.COLUMN_BID_RESULT))));
                    game.setFirstTeamScore(
                            res.getInt(res.getColumnIndex(
                                    GameEntry.COLUMN_FIRST_TEAM_SCORE)));
                    game.setSecondTeamScore
                            (res.getInt(res.getColumnIndex(
                                    GameEntry.COLUMN_SECOND_TEAM_SCORE)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                res.moveToNext();
            }
            res.close();

            for (ChildGameEventListener listener : childGameEventListener)
                if (game.getBidList().size() > 2)
                    listener.onChildAdded(game);

            return game;
        }

        private List<Game> createGameListFromId(String idListJson) {
            List<String> idList = Game.getIdArrayFromJSON(idListJson);
            List<Game> games = new LinkedList<>();
            for (String id : idList) {
                games.add(onSelectGame(id));
            }
            return games;
        }

        public long checkForGame(League leagueType, Team firstTeam, Team secondTeam, long dateTime) {
            SQLiteDatabase db = getReadableDatabase();

            String[] projection = {
                    GameEntry._ID};

            String selection = GameEntry.COLUMN_LEAGUE_TYPE + " = ?" + AND_SEP +
                    GameEntry.COLUMN_FIRST_TEAM + " like ?" + AND_SEP +
                    GameEntry.COLUMN_SECOND_TEAM + " like ?" + AND_SEP +
                    GameEntry.COLUMN_GAME_DATE_TIME + " = ? ";

            String[] selectionArgs = {
                    leagueType.getPackageName(),
                    String.valueOf(firstTeam.get_teamID()),
                    String.valueOf(secondTeam.get_teamID()),
                    String.valueOf(dateTime)
            };
            Cursor res = db.query(
                    true,
                    GameEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null,
                    null
            );
            if (res.getCount() <= 0) {
                res.close();
                return 0L;
            }
            res.moveToFirst();
            long id = res.getLong(res.getColumnIndex(GameEntry._ID));
            res.close();
            return id;
        }

        public void onInsertTeam(Team team) {
            SQLiteDatabase db = getWritableDatabase();
            // check if available: if yes update
            long databaseId = checkForTeam(team.getLeagueType(), team.get_teamID());
            if (databaseId == 0L) {
                ContentValues values = new ContentValues();
                values.put(TeamEntry._ID, team.get_id());
                values.put(TeamEntry.COLUMN_TEAM_ID, team.get_teamID());
                values.put(TeamEntry.COLUMN_CITY, team.getCity());
                values.put(TeamEntry.COLUMN_NAME, team.getName());
                values.put(TeamEntry.COLUMN_ACRONYM, team.getAcronym());
                values.put(TeamEntry.COLUMN_LEAGUE_TYPE, team.getLeagueType().getPackageName());

                db.insert(
                        TeamEntry.TABLE_NAME,
                        null,
                        values);
            }
        }

        private long checkForTeam(League leagueType, Long teamID) {
            SQLiteDatabase db = getWritableDatabase();

            String[] projection = {
                    TeamEntry._ID};

            String selection = TeamEntry.COLUMN_LEAGUE_TYPE + " = ?" + AND_SEP +
                    TeamEntry.COLUMN_TEAM_ID + " = ? ";

            String[] selectionArgs = {
                    leagueType.getPackageName(),
                    teamID.toString()
            };
            Cursor res = db.query(
                    TeamEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );
            if (res.getCount() <= 0) {
                res.close();
                return 0L;
            }
            res.moveToFirst();
            long id = res.getLong(res.getColumnIndex(TeamEntry._ID));
            res.close();
            return id;
        }

        private Team onSelectTeam(String league, String teamID) {
            SQLiteDatabase db = getReadableDatabase();

            String[] projection = {
                    TeamEntry._ID,
                    TeamEntry.COLUMN_TEAM_ID,
                    TeamEntry.COLUMN_CITY,
                    TeamEntry.COLUMN_NAME,
                    TeamEntry.COLUMN_ACRONYM,
                    TeamEntry.COLUMN_LEAGUE_TYPE
            };
            String selection = TeamEntry.COLUMN_TEAM_ID + " = ? " + AND_SEP +
                    TeamEntry.COLUMN_LEAGUE_TYPE + " = ? ";

            String[] selectionArgs = {teamID, league};

            Cursor res = db.query(
                    TeamEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );
            res.moveToFirst();

            Team team = DefaultFactory.Team.constructDefault();
            while (!res.isAfterLast()) {
                try {
                    team.set_id(
                            res.getLong(res.getColumnIndex(
                                    TeamEntry._ID)));
                    team.set_teamId(
                            res.getLong(res.getColumnIndex(
                                    TeamEntry.COLUMN_TEAM_ID)));
                    team.setCity(
                            res.getString(res.getColumnIndex(
                                    TeamEntry.COLUMN_CITY)));
                    team.setName(
                            res.getString(res.getColumnIndex(
                                    TeamEntry.COLUMN_ACRONYM)));
                    team.setLeagueType((League) Class.forName(
                            res.getString(res.getColumnIndex(
                                    TeamEntry.COLUMN_LEAGUE_TYPE))).newInstance());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                res.moveToNext();
            }
            res.close();
            return team;
        }

        private League onSelectLeague(String leagueClass) {
            SQLiteDatabase db = getReadableDatabase();

            String[] projection = {
                    LeagueEntry._ID,
                    LeagueEntry.COLUMN_CLASSPATH,
                    LeagueEntry.REFRESH_INTERVAL
            };
            String selection = LeagueEntry.COLUMN_CLASSPATH + " = ? ";

            String[] selectionArgs = {leagueClass};

            Cursor res = db.query(
                    LeagueEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );
            res.moveToFirst();

            League league = DefaultFactory.League.constructDefault();
            while (!res.isAfterLast()) {
                try {
                    league = (League) Class.forName(res.getString(res.getColumnIndex(LeagueEntry.COLUMN_CLASSPATH))).newInstance();
                    league.setRefreshInterval(res.getLong(res.getColumnIndex(LeagueEntry.REFRESH_INTERVAL)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                res.moveToNext();
            }
            res.close();
            return league;
        }


        public void onInsertLeague(League league) {
            SQLiteDatabase db = getWritableDatabase();

            // check if available: if yes don't add
            ContentValues values = new ContentValues();
            if (!checkForLeague(league.getPackageName())) {
                values.put(LeagueEntry.COLUMN_CLASSPATH, league.getPackageName());
                values.put(LeagueEntry.REFRESH_INTERVAL, league.getRefreshInterval());

                db.insert(
                        LeagueEntry.TABLE_NAME,
                        null,
                        values);
            }
        }

        private boolean checkForLeague(String packageName) {
            SQLiteDatabase db = getWritableDatabase();

            String[] projection = {
                    LeagueEntry._ID};

            String selection = LeagueEntry.COLUMN_CLASSPATH + " = ? ";
            String[] selectionArgs = {packageName};
            Cursor res = db.query(
                    LeagueEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );
            if (res.getCount() <= 0) {
                res.close();
                return false;
            }
            res.close();
            return true;
        }

        public List<League> getLeagues() {
            SQLiteDatabase db = getReadableDatabase();
            List<League> data = new LinkedList<>();
            Cursor res = db.rawQuery("SELECT DISTINCT * " +
                            " FROM " + LeagueEntry.TABLE_NAME,
                    null);
            res.moveToFirst();
            while (!res.isAfterLast()) {
                try {
                    League league = (League) Class.forName(res.getString(res.getColumnIndex(LeagueEntry.COLUMN_CLASSPATH))).newInstance();
                    league.setRefreshInterval(res.getLong(res.getColumnIndex(LeagueEntry.REFRESH_INTERVAL)));
                    data.add(league);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                res.moveToNext();
            }
            res.close();
            return data;
        }

        public HashMap<String, String> getGridKeys() {
            SQLiteDatabase db = getReadableDatabase();
            HashMap<String, String> data = new HashMap<>();
            Cursor res = db.rawQuery("SELECT DISTINCT " +
                            GridEntry._ID + COMMA_SEP + GridEntry.COLUMN_GRID_NAME +
                            " FROM " + GridEntry.TABLE_NAME,
                    null);
            res.moveToFirst();
            while (!res.isAfterLast()) {
                try {
                    data.put(res.getString(res.getColumnIndex(GridEntry._ID)), res.getString(res.getColumnIndex(GridEntry.COLUMN_GRID_NAME)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                res.moveToNext();
            }
            res.close();
            return data;
        }

        public List<Grid> getGrids() {
            SQLiteDatabase db = getReadableDatabase();
            List<Grid> data = new LinkedList<>();
            Cursor res = db.rawQuery("SELECT DISTINCT " +
                            GridEntry._ID +
                            " FROM " + GridEntry.TABLE_NAME,
                    null);
            res.moveToFirst();
            while (!res.isAfterLast()) {
                try {
                    data.add(onSelectGrid(res.getString(res.getColumnIndex(GridEntry._ID))));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                res.moveToNext();
            }
            res.close();
            return data;
        }

        public void onInsertGrid(Grid grid) {
            SQLiteDatabase db = getWritableDatabase();

            // check if available: if yes don't add
            ContentValues values = new ContentValues();
            values.put(GridEntry._ID, grid.get_id());
            values.put(GridEntry.COLUMN_GRID_NAME, grid.getGridName());
            values.put(GridEntry.COLUMN_ROW_NO, grid.getRowNo());
            values.put(GridEntry.COLUMN_COLUMN_NO, grid.getColumnNo());
            values.put(GridEntry.COLUMN_GAME_LIST, Game.getIDArrayToJSSON(grid.getGameList()));
            values.put(GridEntry.COLUMN_KEEP_UPDATES, grid.isKeepUpdates());
            values.put(GridEntry.COLUMN_GRID_LEAGUES, GridLeagues.createJsonArray(grid.getGridLeagues()));
            values.put(GridEntry.COLUMN_UPDATED_ON, grid.getUpdatedOn());

            db.insert(
                    GridEntry.TABLE_NAME,
                    null,
                    values);

        }

        public Grid onSelectGrid(String gridId) {
            SQLiteDatabase db = getReadableDatabase();

            String[] projection = {
                    GridEntry._ID,
                    GridEntry.COLUMN_GRID_NAME,
                    GridEntry.COLUMN_ROW_NO,
                    GridEntry.COLUMN_COLUMN_NO,
                    GridEntry.COLUMN_GAME_LIST,
                    GridEntry.COLUMN_KEEP_UPDATES,
                    GridEntry.COLUMN_GRID_LEAGUES,
                    GridEntry.COLUMN_UPDATED_ON
            };
            String selection = GridEntry._ID + " = ?";

            String[] selectionArgs = {gridId};

            Cursor res = db.query(
                    GridEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );
            res.moveToFirst();

            Grid grid = DefaultFactory.Grid.constructDefault();
            while (!res.isAfterLast()) {
                try {
                    grid.set_id(
                            res.getLong(res.getColumnIndex(
                                    GridEntry._ID)));
                    grid.setGridName(
                            res.getString(res.getColumnIndex(
                                    GridEntry.COLUMN_GRID_NAME)));
                    grid.setRowNo(
                            res.getInt(res.getColumnIndex(
                                    GridEntry.COLUMN_ROW_NO)));
                    grid.setColumnNo(
                            res.getInt(res.getColumnIndex(
                                    GridEntry.COLUMN_COLUMN_NO)));
                    grid.setGameList(createGameListFromId(
                            res.getString(res.getColumnIndex(
                                    GridEntry.COLUMN_GAME_LIST))));
                    grid.setKeepUpdates(res.getInt(
                            res.getColumnIndex(
                                    GridEntry.COLUMN_KEEP_UPDATES)) == 1);
                    grid.setGridLeagues(GridLeagues.createArrayFromJson(
                            res.getString(res.getColumnIndex(
                                    GridEntry.COLUMN_GRID_LEAGUES))));
                    grid.setUpdatedOn(
                            res.getLong(res.getColumnIndex(
                                    GridEntry.COLUMN_UPDATED_ON)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                res.moveToNext();
            }
            res.close();
            return grid;
        }

        public void onUpdateGrid(long gridId, Grid grid) {
            SQLiteDatabase db = getReadableDatabase();

            ContentValues values = new ContentValues();
            values.put(GridEntry.COLUMN_GRID_NAME, grid.getGridName());
            values.put(GridEntry.COLUMN_ROW_NO, grid.getRowNo());
            values.put(GridEntry.COLUMN_COLUMN_NO, grid.getColumnNo());
            values.put(GridEntry.COLUMN_GAME_LIST, Game.getIDArrayToJSSON(grid.getGameList()));
            values.put(GridEntry.COLUMN_KEEP_UPDATES, grid.isKeepUpdates());
            values.put(GridEntry.COLUMN_GRID_LEAGUES, GridLeagues.createJsonArray(grid.getGridLeagues()));
            values.put(GridEntry.COLUMN_UPDATED_ON, grid.getUpdatedOn());

            String selection = GameEntry._ID + " = ?";
            String[] selectionArgs = {String.valueOf(gridId)};

            db.update(GridEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
        }

        public void addChildGameEventListener(ChildGameEventListener childGameEventListener) {
            DbHelper.childGameEventListener.add(childGameEventListener);
        }

        public void removeChildGameEventListener(ChildGameEventListener childGameEventListener) {
            DbHelper.childGameEventListener.remove(childGameEventListener);

        }
    }
}