package com.calebtrevino.tallystacker.models.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.calebtrevino.tallystacker.controllers.events.GameAddedEvent;
import com.calebtrevino.tallystacker.controllers.events.GameModifiedEvent;
import com.calebtrevino.tallystacker.controllers.factories.DefaultFactory;
import com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.bases.League;
import com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.bases.Soccer;
import com.calebtrevino.tallystacker.models.Bid;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.Grid;
import com.calebtrevino.tallystacker.models.GridLeagues;
import com.calebtrevino.tallystacker.models.Team;
import com.calebtrevino.tallystacker.models.enums.BidResult;
import com.calebtrevino.tallystacker.models.enums.GameStatus;
import com.calebtrevino.tallystacker.models.enums.GridMode;
import com.calebtrevino.tallystacker.models.enums.ScoreType;
import com.calebtrevino.tallystacker.utils.Constants;
import com.calebtrevino.tallystacker.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;
import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Ritesh Shakya
 */

public class DatabaseContract {
    private static final String TAG = DatabaseContract.class.getSimpleName();
    private static final String TEXT_TYPE = " TEXT";
    private static final String BOOLEAN_TYPE = " INTEGER";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String AND_SEP = " AND ";
    private static final String EQUAL_SEP = " = ? ";
    private static final String LESS_THAN = " <= ? ";

    private DatabaseContract() {

    }

    /**
     * Check game valididty
     *
     * @param game Game in consideration.
     * @return {@code true} If game was scheduled yesterday and still not completed or is scheduled for today.
     */
    public static boolean checkGameValidity(Game game, int lag) {
        return game.getGameAddDate() == new DateTime(Constants.DATE.VEGAS_TIME_ZONE).minusDays(Constants.DATE_LAG + lag).withTimeAtStartOfDay().getMillis();
    }

    static abstract class GameEntry implements BaseColumns {
        private static final String TABLE_NAME = "game_table";

        private static final String COLUMN_FIRST_TEAM = "first_team";            // Team
        private static final String COLUMN_SECOND_TEAM = "second_team";          // Team
        private static final String COLUMN_LEAGUE_TYPE = "league_type";          // League
        private static final String COLUMN_GAME_DATE_TIME = "game_date_time";    // long
        private static final String COLUMN_GAME_ADD_DATE = "game_added_time";  // long
        private static final String COLUMN_SCORE_TYPE = "score_type";            // ScoreType
        private static final String COLUMN_BID_LIST = "bid_list";                // BID_LIST
        private static final String COLUMN_BID_RESULT = "bid_result";            // Result
        private static final String COLUMN_FIRST_TEAM_SCORE = "first_team_score";      // Long
        private static final String COLUMN_SECOND_TEAM_SCORE = "second_team_score";    // Long
        private static final String COLUMN_UPDATED_ON = "updated_on";            // long
        private static final String COLUMN_GAME_URL = "game_url";            // long
        private static final String COLUMN_GAME_COMPLETED = "game_completed";            // long
        private static final String COLUMN_REQ_MANUAL = "req_manual";            // long
        private static final String COLUMN_VI_ROW = "vi_row";            // long

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
                        COLUMN_UPDATED_ON + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_GAME_URL + TEXT_TYPE + COMMA_SEP +
                        COLUMN_GAME_COMPLETED + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_REQ_MANUAL + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_VI_ROW + INTEGER_TYPE +
                        " )";

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    static abstract class TeamEntry implements BaseColumns {
        private static final String TABLE_NAME = "team_table";

        private static final String COLUMN_TEAM_ID = "team_id";                  // String
        private static final String COLUMN_CITY = "city";                        // String
        private static final String COLUMN_NAME = "name";                        // String
        private static final String COLUMN_ACRONYM = "acronym";                  // String
        private static final String COLUMN_LEAGUE_TYPE = "league_type";          // League

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
        private static final String TABLE_NAME = "league_table";

        private static final String COLUMN_CLASSPATH = "classpath";
        private static final String REFRESH_INTERVAL = "refresh_interval";

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
        private static final String TABLE_NAME = "grid_table";

        private static final String COLUMN_GRID_NAME = "grid_name";              // String
        private static final String COLUMN_ROW_NO = "row_no";                    // Int
        private static final String COLUMN_COLUMN_NO = "column_no";              // Int
        private static final String COLUMN_GAME_LIST = "game_list";              // Game List
        private static final String COLUMN_KEEP_UPDATES = "keep_updates";        // Bool
        private static final String COLUMN_GRID_LEAGUES = "grid_leagues";        // Grid Leagues List
        private static final String COLUMN_UPDATED_ON = "updated_on";           // Long
        private static final String COLUMN_GRID_MODE = "grid_mode";             // Long


        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY," +
                        COLUMN_GRID_NAME + TEXT_TYPE + COMMA_SEP +
                        COLUMN_ROW_NO + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_COLUMN_NO + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_GAME_LIST + TEXT_TYPE + COMMA_SEP +
                        COLUMN_KEEP_UPDATES + BOOLEAN_TYPE + COMMA_SEP +
                        COLUMN_GRID_LEAGUES + TEXT_TYPE + COMMA_SEP +
                        COLUMN_UPDATED_ON + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_GRID_MODE + TEXT_TYPE +
                        " )";

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class DbHelper extends SQLiteOpenHelper {

        private static final int DATABASE_VERSION = 3;
        private static final String DATABASE_NAME = "tally_stacker.db";

        public DbHelper(Context activity) {
            super(activity.getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);
        }

        /**
         * Check the validity of a game.
         * Usage in [{@link #onInsertGame(Game)}, {@link #onSelectGame(String)}, {@link #onSelectGames(GridLeagues, List)}, {@link #onUpdateGame(long, Game)}, {@link #selectUpcomingGames()}]
         *
         * @param game Game Object
         * @return {@code true} if game is valid; {@code false} otherwise.
         */
        public static synchronized boolean checkBid(Game game) {
//            return (!(game.getLeagueType() instanceof Soccer) && game.getBidList().size() > 3 && !game.getVIBid().equals(DefaultFactory.Bid.constructDefault())) || (
//                    game.getLeagueType() instanceof Soccer && (
//                            game.getVIBid().getVigAmount() >= Constants.VALUES.SOCCER_MIN_VALUE &&
//                                    !String.valueOf(game.getVIBid().getBidAmount()).endsWith(".25") &&
//                                    !String.valueOf(game.getVIBid().getBidAmount()).endsWith(".75")
//                    )
//            );
            return true;
        }

        public static synchronized boolean checkBidValid(Game game) {
            return (!(game.getLeagueType() instanceof Soccer) && game.getBidList().size() > 3 && !game.getVIBid().equals(DefaultFactory.Bid.constructDefault())) || (
                    game.getLeagueType() instanceof Soccer && (
                            game.getVIBid().getVigAmount() >= Constants.VALUES.SOCCER_MIN_VALUE &&
                                    !String.valueOf(game.getVIBid().getBidAmount()).endsWith(".25") &&
                                    !String.valueOf(game.getVIBid().getBidAmount()).endsWith(".75")
                    )
            );
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

        /**
         * Adds or updates the database with the list of games received.
         *
         * @param data List of games fetched today.
         */
        public void onInsertGame(List<Game> data) {
            for (Game gameData : data) {
                // check if available: if yes update
                long databaseId = checkForGame(gameData.getLeagueType(), gameData.getFirstTeam(), gameData.getSecondTeam(), gameData.getGameDateTime());
                if (databaseId == 0L) {
                    onInsertGame(gameData);
                } else {
                    Game game = onSelectGame(String.valueOf(databaseId));
                    if (!checkBidValid(game) || // Update if game was previous invalid
                            StringUtils.isNull(game.getGameUrl()) || // Update if gameUrl was previously null
                            game.getFirstTeam().getAcronym().equals(DefaultFactory.Team.ACRONYM) || game.getSecondTeam().getAcronym().equals(DefaultFactory.Team.ACRONYM) // Update if teams weren't in database previously
                            ) {
                        onUpdateGame(databaseId, gameData);
                    }
                }
            }
        }

        /**
         * Called after all the games of today are fetched and stored in database. Updates the grid with data from today.
         */
        public void addGamesToGrids() {
            long dateToday = new DateTime(Constants.DATE.VEGAS_TIME_ZONE).withTimeAtStartOfDay().getMillis();
            List<Grid> gridList = getGrids();
            for (Grid grid : gridList) {
                if (grid.getUpdatedOn() != dateToday && grid.isKeepUpdates()) {
                    addGamesToGrid(grid);
                }
            }
        }

        /**
         * Adds valid games to a old/new grid.
         *
         * @param grid Grid object
         */
        public void addGamesToGrid(Grid grid) {
            List<GridLeagues> gridLeaguesList = grid.getGridLeagues();
            List<Game> gameList = grid.getGameList();
            List<Game> addedToday = new LinkedList<>();
            // List of all the Leagues within the grid
            for (GridLeagues gridLeague : gridLeaguesList) {
                onSelectGames(gridLeague, addedToday);
            }
            // Limit the no. of games added to the grid as per requirement.
            if (addedToday.size() > grid.getRowNo()) {
                addedToday = addedToday.subList(0, grid.getRowNo());
            }
            Log.i(TAG, "Added Today = " + grid.getGridName() + " " + addedToday.size());
            gameList.addAll(addedToday);
            grid.setGameList(gameList);
            // Set the updated time in the grid so that the grid isn't updated anymore today.
            grid.setUpdatedOn(new DateTime(Constants.DATE.VEGAS_TIME_ZONE).withTimeAtStartOfDay().getMillis());

            // Check if grid id exists. used to either update or create a new grid.
            long databaseId = checkForGrid(grid.getId());
            if (databaseId == 0L) {
                onInsertGrid(grid);
            } else {
                onUpdateGrid(grid.getId(), grid);
            }
        }

        /**
         * Check for the grid in the database.
         *
         * @param gridId Id of the grid.
         * @return {@link GridEntry#_ID} for grid found. {@code 0L} Otherwise.
         */
        private long checkForGrid(long gridId) {
            SQLiteDatabase db = getReadableDatabase();

            String[] projection = {
                    GridEntry._ID};

            String selection = GridEntry._ID + EQUAL_SEP;

            String[] selectionArgs = {
                    String.valueOf(gridId)
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

        /**
         * Adds games to {@param gameList} as defined in {@link GridLeagues}.
         *
         * @param gridLeague League stored in the grid along with additional infos.
         * @param gameList   List of games to add to grid.
         * @see GridLeagues
         */
        private void onSelectGames(GridLeagues gridLeague, List<Game> gameList) {
            League league = gridLeague.getLeague();
            List<Game> addedGames = onSelectGame(league.getPackageName(), new DateTime(Constants.DATE.VEGAS_TIME_ZONE).withTimeAtStartOfDay().getMillis());
            List<Game> copyList = new LinkedList<>(addedGames);
            for (Game game : copyList) {
                if (!checkBid(game)) {
                    addedGames.remove(game);
                }
            }
            if (gridLeague.getStartNo() <= addedGames.size()) {
                int start = gridLeague.getStartNo() != 0 ? gridLeague.getStartNo() - 1 : 0;
                int end = addedGames.size() >= gridLeague.getEndNumber() ? gridLeague.getEndNumber() : addedGames.size();
                addedGames = addedGames.subList(
                        start,
                        end
                );
                gameList.addAll(addedGames);
            }
        }

        /**
         * List of games for a particular league for a particular day.
         *
         * @param leaguePackageName Classpath of the league in consideration.
         * @param dateToday         The epoch time for start of the the day under consideration in Vegas Insider TimeZone.
         * @return List of games.
         */
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
                    GameEntry.COLUMN_UPDATED_ON,
                    GameEntry.COLUMN_GAME_URL,
                    GameEntry.COLUMN_GAME_COMPLETED,
                    GameEntry.COLUMN_REQ_MANUAL,
                    GameEntry.COLUMN_VI_ROW

            };
            String selection = GameEntry.COLUMN_LEAGUE_TYPE + EQUAL_SEP + AND_SEP +
                    GameEntry.COLUMN_GAME_ADD_DATE + EQUAL_SEP;
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
                    setGameAttrs(res, game);
                    gameList.add(game);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                res.moveToNext();
            }
            res.close();
            return gameList;
        }

        /**
         * Updates a existing game into the database. {@link GameEntry#_ID} remains the same throughout the process.
         *
         * @param databaseId Id of the game to update
         * @param gameData   Game object
         */
        public void onUpdateGame(long databaseId, final Game gameData) {
            SQLiteDatabase db = getWritableDatabase();

            ContentValues values = new ContentValues();
            setGameVariables(gameData, values);

            String selection = GameEntry._ID + EQUAL_SEP;
            String[] selectionArgs = {String.valueOf(databaseId)};

            db.update(GameEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);

            if (checkBid(gameData))
                EventBus.getDefault().post(new GameModifiedEvent(gameData));
        }

        /**
         * Insert a game to the database
         *
         * @param gameData Game object
         */
        private void onInsertGame(final Game gameData) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            setGameVariables(gameData, values);


            db.insert(
                    DatabaseContract.GameEntry.TABLE_NAME,
                    null,
                    values);

            if (checkBid(gameData))
                EventBus.getDefault().post(new GameAddedEvent(gameData));
        }

        private void setGameVariables(Game gameData, ContentValues values) {
            values.put(GameEntry._ID, gameData.getId());
            values.put(GameEntry.COLUMN_FIRST_TEAM, onInsertTeam(gameData.getFirstTeam()));
            values.put(GameEntry.COLUMN_SECOND_TEAM, onInsertTeam(gameData.getSecondTeam()));
            values.put(GameEntry.COLUMN_LEAGUE_TYPE, gameData.getLeagueType().getPackageName());
            values.put(GameEntry.COLUMN_GAME_DATE_TIME, gameData.getGameDateTime());
            values.put(GameEntry.COLUMN_GAME_ADD_DATE, gameData.getGameAddDate());
            values.put(GameEntry.COLUMN_SCORE_TYPE, gameData.getScoreType().getValue());
            values.put(GameEntry.COLUMN_BID_LIST, Bid.createJsonArray(gameData.getBidList()));
            values.put(GameEntry.COLUMN_BID_RESULT, gameData.getBidResult().getValue());
            values.put(GameEntry.COLUMN_FIRST_TEAM_SCORE, gameData.getFirstTeamScore());
            values.put(GameEntry.COLUMN_SECOND_TEAM_SCORE, gameData.getSecondTeamScore());
            values.put(GameEntry.COLUMN_UPDATED_ON, new DateTime().getMillis());
            values.put(GameEntry.COLUMN_GAME_URL, gameData.getGameUrl());
            values.put(GameEntry.COLUMN_GAME_COMPLETED, gameData.getGameStatus().getValue());
            values.put(GameEntry.COLUMN_REQ_MANUAL, gameData.isReqManual());
            values.put(GameEntry.COLUMN_VI_ROW, gameData.getVIRow());
        }


        /**
         * Used to get games that are queued for today. Used in dashboard, creating game notifications and calculating no of games for each league while creating new grid.
         *
         * @return List of games for today.
         */
        public List<Game> selectUpcomingGames() {
            return selectUpcomingGames(0);
        }

        /**
         * Used to get games that are queued for today. Used in dashboard, creating game notifications and calculating no of games for each league while creating new grid.
         *
         * @return List of games for today.
         */

        public List<Game> selectUpcomingGames(int lag) {
            SQLiteDatabase db = getReadableDatabase();

            String[] selectionArgs = {
                    String.valueOf(new DateTime(Constants.DATE.VEGAS_TIME_ZONE).minusDays(Constants.DATE_LAG + lag).withTimeAtStartOfDay().getMillis())};
            List<Game> data = new LinkedList<>();
            Cursor res = db.rawQuery("SELECT " + GameEntry._ID +
                            " FROM " + GameEntry.TABLE_NAME +
                            " WHERE " + GameEntry.COLUMN_GAME_ADD_DATE + EQUAL_SEP +
                            " ORDER BY " + GameEntry.COLUMN_GAME_DATE_TIME,
                    selectionArgs);
            res.moveToFirst();

            while (!res.isAfterLast()) {
                Game game = onSelectGame(String.valueOf(res.getInt(res.getColumnIndex(GameEntry._ID))));
                if (checkBid(game) && checkGameValidity(game, lag)) {
                    data.add(game);
                }
                res.moveToNext();

            }
            res.close();
            return data;
        }

        /**
         * Used to get games that require manual entry.
         *
         * @return List of games for today.
         */
        public List<Game> getManualGames() {
            SQLiteDatabase db = getReadableDatabase();

            String[] selectionArgs = {String.valueOf(1), String.valueOf(new DateTime(Constants.DATE.VEGAS_TIME_ZONE).getMillis()), ""};
            List<Game> data = new LinkedList<>();
            Cursor res = db.rawQuery("SELECT " + GameEntry._ID +
                            " FROM " + GameEntry.TABLE_NAME +
                            " WHERE " + GameEntry.COLUMN_REQ_MANUAL + EQUAL_SEP + AND_SEP +
                            GameEntry.COLUMN_GAME_DATE_TIME + LESS_THAN + AND_SEP +
                            GameEntry.COLUMN_GAME_URL + EQUAL_SEP +
                            " ORDER BY " + GameEntry.COLUMN_GAME_DATE_TIME,
                    selectionArgs);
            res.moveToFirst();

            while (!res.isAfterLast()) {
                Game game = onSelectGame(String.valueOf(res.getInt(res.getColumnIndex(GameEntry._ID))));
                if (checkBid(game) && game.getGameStatus() == GameStatus.NEUTRAL) {
                    data.add(game);
                }
                res.moveToNext();

            }
            res.close();
            return data;
        }

        /**
         * Queries the database to get Game object for given id.
         *
         * @param gameId Id of the game to fetch.
         * @return Game object.
         */
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
                    GameEntry.COLUMN_UPDATED_ON,
                    GameEntry.COLUMN_GAME_URL,
                    GameEntry.COLUMN_GAME_COMPLETED,
                    GameEntry.COLUMN_REQ_MANUAL,
                    GameEntry.COLUMN_VI_ROW
            };
            String selection = GameEntry._ID + EQUAL_SEP;
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
                    setGameAttrs(res, game);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                res.moveToNext();
            }
            res.close();

            if (checkBid(game))
                EventBus.getDefault().post(new GameAddedEvent(game));

            return game;
        }

        private Game setGameAttrs(Cursor res, Game game) {
            game.setId(
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
            game.setSecondTeamScore(
                    res.getInt(res.getColumnIndex(
                            GameEntry.COLUMN_SECOND_TEAM_SCORE)));
            game.setUpdatedTime(
                    res.getLong(res.getColumnIndex(
                            GameEntry.COLUMN_UPDATED_ON)));
            game.setGameUrl(
                    res.getString(res.getColumnIndex(
                            GameEntry.COLUMN_GAME_URL
                    ))
            );
            game.setGameStatus(GameStatus.match(res.getString(
                    res.getColumnIndex(
                            GameEntry.COLUMN_GAME_COMPLETED))));
            game.setReqManual(res.getInt(
                    res.getColumnIndex(
                            GameEntry.COLUMN_REQ_MANUAL)) == 1);
            game.setVIRow(res.getInt(
                    res.getColumnIndex(
                            GameEntry.COLUMN_VI_ROW)));
            game.setVIBid();
            return game;
        }

        /**
         * Database only store the id of each game as a list. Method used to convert this list of _id to games.
         *
         * @param rowNo
         * @param idListJson List of {@link GameEntry#_ID} stored in database.
         * @return List of games in a specific grid.
         */
        private List<Game> createGameListFromId(int rowNo, String idListJson) {
            List<String> idList = Game.getIdArrayFromJSON(idListJson);
            List<Game> games = new LinkedList<>();
            BidResult previousStatus = BidResult.NEUTRAL;
            for (int position = 0; position < idList.size(); position++) {
                Game game = onSelectGame(idList.get(position));
                if (position > 0)
                    setBatchMarker(game, games.get(position - 1));
                int count = 1;
                int modValue = position % rowNo;
                int column = 0;
                for (int i = 0; i < position; i++) {
                    if (i == column * rowNo + modValue) {
                        if (previousStatus == games.get(i).getBidResult()) {
                            count++;
                        } else {
                            count = 1;
                            previousStatus = games.get(i).getBidResult();
                        }
                        column++;
                    }
                }
                game.setGridCount(count);
                game.setPreviousGridStatus(previousStatus);
                games.add(game);
            }
            return games;
        }

        private void setBatchMarker(Game currentGame, Game previousGame) {
            long previousTs;
            previousTs = previousGame.getGameAddDate();
            setBannerVisibility(currentGame.getGameAddDate(), previousTs, currentGame);
        }

        private void setBannerVisibility(long ts1, long ts2, Game currentGame) {
            if (ts2 == 0) {
                currentGame.setBannerVisibility(true);
            } else {
                boolean sameDay = ts1 == ts2;
                if (sameDay) {
                    currentGame.setBannerVisibility(false);
                } else {
                    currentGame.setBannerVisibility(true);
                }

            }
        }

        /**
         * Check if a specific game exists in database.
         *
         * @param leagueType League type of the game.
         * @param firstTeam  First team as listed in vegas insider.
         * @param secondTeam Second team as listed in vegas insider.
         * @param dateTime   The time at which the game is scheduled.
         * @return {@link GameEntry#_ID} for game found. {@code 0L} Otherwise.
         */
        public long checkForGame(League leagueType, Team firstTeam, Team secondTeam, long dateTime) {
            SQLiteDatabase db = getReadableDatabase();

            String[] projection = {
                    GameEntry._ID};

            String selection = GameEntry.COLUMN_LEAGUE_TYPE + EQUAL_SEP + AND_SEP +
                    GameEntry.COLUMN_FIRST_TEAM + EQUAL_SEP + AND_SEP +
                    GameEntry.COLUMN_SECOND_TEAM + EQUAL_SEP + AND_SEP +
                    GameEntry.COLUMN_GAME_DATE_TIME + EQUAL_SEP;

            String[] selectionArgs = {
                    leagueType.getPackageName(),
                    String.valueOf(checkForTeam(firstTeam.getLeagueType(), firstTeam.getCity(), firstTeam.getName())),
                    String.valueOf(checkForTeam(secondTeam.getLeagueType(), secondTeam.getCity(), secondTeam.getName())),
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

        /**
         * Insert team into database.
         *
         * @param team Team object to insert.
         * @return Id of the team inserted.
         */
        private long onInsertTeam(Team team) {
            SQLiteDatabase db = getWritableDatabase();
            // check if available: if yes update
            long databaseId = checkForTeam(team.getLeagueType(), team.getCity(), team.getName());
            if (databaseId == 0L) {
                ContentValues values = new ContentValues();
                values.put(TeamEntry._ID, team.getId());
                values.put(TeamEntry.COLUMN_TEAM_ID, team.getTeamID());
                values.put(TeamEntry.COLUMN_CITY, team.getCity());
                values.put(TeamEntry.COLUMN_NAME, team.getName());
                values.put(TeamEntry.COLUMN_ACRONYM, team.getAcronym());
                values.put(TeamEntry.COLUMN_LEAGUE_TYPE, team.getLeagueType().getPackageName());

                boolean teamIdExists = checkForTeam(team.getId());
                if (teamIdExists) {
                    team.setId(DefaultFactory.Team.constructDefault().getId());
                    values.put(TeamEntry._ID, team.getId());

                }
                db.insert(
                        TeamEntry.TABLE_NAME,
                        null,
                        values);
                return team.getId();
            }
            return databaseId;
        }

        /**
         * Check whether a team exists in the database.
         *
         * @param id ID associated with the team.
         * @return {@code true} If found in databse. {@code false} Otherwise.
         */
        private boolean checkForTeam(long id) {
            SQLiteDatabase db = getReadableDatabase();

            String[] projection = {
                    TeamEntry._ID};

            String selection = TeamEntry._ID + EQUAL_SEP;

            String[] selectionArgs = {
                    String.valueOf(id)
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
                return false;
            }
            res.close();
            return true;
        }

        /**
         * Check whether a team exists in the database.
         *
         * @param leagueType League object to which the team belongs to.
         * @param teamCity   The name of the team.
         * @param teamName   The name of the team.
         * @return {@link TeamEntry#_ID} for teams found. {@code 0L} Otherwise.
         */
        private long checkForTeam(League leagueType, String teamCity, String teamName) {
            SQLiteDatabase db = getWritableDatabase();

            String[] projection = {
                    TeamEntry._ID};

            String selection = TeamEntry.COLUMN_LEAGUE_TYPE + EQUAL_SEP + AND_SEP +
                    TeamEntry.COLUMN_CITY + EQUAL_SEP + AND_SEP +
                    TeamEntry.COLUMN_NAME + EQUAL_SEP;

            String[] selectionArgs = {
                    leagueType.getPackageName(),
                    teamCity,
                    teamName
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
            String selection = TeamEntry._ID + EQUAL_SEP + AND_SEP +
                    TeamEntry.COLUMN_LEAGUE_TYPE + EQUAL_SEP;

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
                    team.setId(
                            res.getLong(res.getColumnIndex(
                                    TeamEntry._ID)));
                    team.setTeamId(
                            res.getLong(res.getColumnIndex(
                                    TeamEntry.COLUMN_TEAM_ID)));
                    team.setCity(
                            res.getString(res.getColumnIndex(
                                    TeamEntry.COLUMN_CITY)));
                    team.setName(
                            res.getString(res.getColumnIndex(
                                    TeamEntry.COLUMN_NAME)));
                    team.setAcronym(
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

        /**
         * Returns an object of specified classpath.
         *
         * @param leagueClass ClassPath to the league in consideration.
         * @return League object.
         */
        private League onSelectLeague(String leagueClass) {
            SQLiteDatabase db = getReadableDatabase();

            String[] projection = {
                    LeagueEntry._ID,
                    LeagueEntry.COLUMN_CLASSPATH,
                    LeagueEntry.REFRESH_INTERVAL
            };
            String selection = LeagueEntry.COLUMN_CLASSPATH + EQUAL_SEP;

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


        /**
         * Insert league into the database if if not present.
         *
         * @param league League object ot insert.
         */
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

        /**
         * Checks whether League exists in the database.
         *
         * @param packageName Path of the child class inheriting league.
         * @return {@code true} If the league already exists in the database.{@code false} Otherwise.
         */
        private boolean checkForLeague(String packageName) {
            SQLiteDatabase db = getReadableDatabase();

            String[] projection = {
                    LeagueEntry._ID};

            String selection = LeagueEntry.COLUMN_CLASSPATH + EQUAL_SEP;
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

        /**
         * Get the complete list of available Leagues.
         *
         * @return List of all the available Leagues.
         */
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

        /**
         * Gets the list of Grids added by user.
         *
         * @return List of Grids int he database.
         */
        private List<Grid> getGrids() {
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

        /**
         * Insert a new grid to database.
         *
         * @param grid Grid Object
         */
        private void onInsertGrid(Grid grid) {
            SQLiteDatabase db = getWritableDatabase();

            // check if available: if yes don't add
            ContentValues values = new ContentValues();
            values.put(GridEntry._ID, grid.getId());
            values.put(GridEntry.COLUMN_GRID_NAME, grid.getGridName());
            values.put(GridEntry.COLUMN_ROW_NO, grid.getRowNo());
            values.put(GridEntry.COLUMN_COLUMN_NO, grid.getColumnNo());
            values.put(GridEntry.COLUMN_GAME_LIST, Game.getIDArrayToJSSON(grid.getGameList()));
            values.put(GridEntry.COLUMN_KEEP_UPDATES, grid.isKeepUpdates());
            values.put(GridEntry.COLUMN_GRID_LEAGUES, GridLeagues.createJsonArray(grid.getGridLeagues()));
            values.put(GridEntry.COLUMN_UPDATED_ON, grid.getUpdatedOn());
            values.put(GridEntry.COLUMN_GRID_MODE, grid.getGridMode().getValue());
            db.insert(
                    GridEntry.TABLE_NAME,
                    null,
                    values);

        }

        /**
         * Fetch grid from the database.
         *
         * @param gridId Id of the grid to be constructed from database.
         * @return Grid object.
         */
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
                    GridEntry.COLUMN_UPDATED_ON,
                    GridEntry.COLUMN_GRID_MODE
            };
            String selection = GridEntry._ID + EQUAL_SEP;

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
                    grid.setId(
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
                    grid.setGameList(createGameListFromId(grid.getRowNo(),
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
                    grid.setGridMode(GridMode.match(
                            res.getString(res.getColumnIndex(
                                    GridEntry.COLUMN_GRID_MODE))));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                res.moveToNext();
            }
            res.close();
            return grid;
        }

        /**
         * Used to update a grid in the database with {@link GridEntry#_ID} remaining the same.
         *
         * @param gridId Id of the grid to update
         * @param grid   New Grid Object
         */
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
            values.put(GridEntry.COLUMN_GRID_MODE, grid.getGridMode().getValue());

            String selection = GameEntry._ID + EQUAL_SEP;
            String[] selectionArgs = {String.valueOf(gridId)};

            db.update(GridEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
        }
    }
}