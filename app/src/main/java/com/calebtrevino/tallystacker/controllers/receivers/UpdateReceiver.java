package com.calebtrevino.tallystacker.controllers.receivers;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.controllers.sources.AFL_Spread;
import com.calebtrevino.tallystacker.controllers.sources.AFL_Total;
import com.calebtrevino.tallystacker.controllers.sources.CFL_Spread;
import com.calebtrevino.tallystacker.controllers.sources.CFL_Total;
import com.calebtrevino.tallystacker.controllers.sources.MLB_Total;
import com.calebtrevino.tallystacker.controllers.sources.NBA_Spread;
import com.calebtrevino.tallystacker.controllers.sources.NBA_Total;
import com.calebtrevino.tallystacker.controllers.sources.NCAA_BK_Spread;
import com.calebtrevino.tallystacker.controllers.sources.NCAA_BK_Total;
import com.calebtrevino.tallystacker.controllers.sources.NCAA_FB_Spread;
import com.calebtrevino.tallystacker.controllers.sources.NCAA_FB_Total;
import com.calebtrevino.tallystacker.controllers.sources.NFL_Spread;
import com.calebtrevino.tallystacker.controllers.sources.NFL_Total;
import com.calebtrevino.tallystacker.controllers.sources.Soccer_Spread;
import com.calebtrevino.tallystacker.controllers.sources.Soccer_Total;
import com.calebtrevino.tallystacker.controllers.sources.WNBA_Spread;
import com.calebtrevino.tallystacker.controllers.sources.WNBA_Total;
import com.calebtrevino.tallystacker.controllers.sources.bases.League;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.database.DatabaseContract;
import com.calebtrevino.tallystacker.models.listeners.ChildGameEventListener;
import com.calebtrevino.tallystacker.models.preferences.MultiProcessPreference;
import com.calebtrevino.tallystacker.utils.Constants;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import org.greenrobot.eventbus.EventBus;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Deals with fetching games from the site as well as self handles exceptions by relaunching itself until a successful fetch is made for a day.
 *
 * @author Ritesh Shakya
 */

public class UpdateReceiver extends BroadcastReceiver implements ChildGameEventListener {
    public static final int ALARM_ID = 15927;
    private static final int ALARM_ID_ERROR = 15928;
    private static final String TAG = UpdateReceiver.class.getName();
    private static final String LAST_UPDATE = "last_update";
    private Context mContext;
    public static final String STARTED_BY = "started_by";
    private static final String ERROR_REPEAT = "error_repeat";
    public static final String RESTART_REPEAT = "restart_repeat";
    public static final String NORMAL_REPEAT = "normal_repeat";
    private String startedBy;

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        this.mContext = context;
        startedBy = intent.getStringExtra(STARTED_BY);
        Log.i(TAG, "onReceive: " + startedBy);

        // Only update from vegas Insider once a day
        if (!MultiProcessPreference.getDefaultSharedPreferences().getString(LAST_UPDATE, "").equals(new DateTime().withTimeAtStartOfDay().toString())) {
            // Save fetch time to answers.
            saveToAnswers();
            // Fetch games from site.
            new GetLeague().execute();
        }
    }

    @Override
    public void onChildAdded(Game game) {
        EventBus.getDefault().post(game);
    }

    @Override
    public void onChildChanged(Game game) {

    }

    @Override
    public void onChildRemoved(Game game) {

    }


    private class GetLeague extends AsyncTask<Boolean, String, String> {

        @Override
        protected String doInBackground(Boolean... getBids) {
            Log.i(TAG, "Timer task started bid work");
            // Show notification without error.
            showNotification(false);
            //Fetch games.
            fetchGames();
            return null;
        }

        private void fetchGames() {
            League[] leagueList = new League[]{
                    new MLB_Total(),
                    new WNBA_Total(),
                    new WNBA_Spread(),
                    new CFL_Total(),
                    new CFL_Spread(),
                    new NBA_Total(),
                    new NBA_Spread(),
                    new NFL_Total(),
                    new NFL_Spread(),
                    new NCAA_FB_Total(),
                    new NCAA_FB_Spread(),
                    new Soccer_Spread(),
                    new Soccer_Total(),
                    new AFL_Spread(),
                    new AFL_Total(),
                    new NCAA_BK_Spread(),
                    new NCAA_BK_Total()
            };
            DatabaseContract.DbHelper dbHelper;
            boolean nullList = true;
            dbHelper = new DatabaseContract.DbHelper(mContext);
            dbHelper.addChildGameEventListener(UpdateReceiver.this);
            try {
                for (League league : leagueList) {
                    dbHelper.onInsertLeague(league);
                    List<Game> gameList = league.pullGamesFromNetwork(mContext);
                    if (gameList.size() != 0 && nullList) {
                        for (Game game : gameList) {
                            if (dbHelper.checkForGame(game.getLeagueType(), game.getFirstTeam(), game.getSecondTeam(), game.getGameDateTime()) != 0L && nullList) {
                                nullList = false;
                            }
                        }
                    }
                }
                if (nullList) { // If a single game wasn't added to the database, catch exception.
                    throw new Exception("Ignore");
                }
                // Add games added to the grids currently in action.
                dbHelper.addGamesToGrids();

                // Since update was a success cancel any other repeating updates which were created in case of error in fetching.
                cancelRepeatingUpdates();

                // Log the current day so that no further updates for the day happen.
                MultiProcessPreference.getDefaultSharedPreferences().edit().putString(LAST_UPDATE, new DateTime().withTimeAtStartOfDay().toString()).commit();

                // Create alarms for all the games scheduled for today.
                createAlarms();
            } catch (Exception e) { // Catch any exception and create a repeating alarm.
                Log.i(TAG, "Couldn't fetch games trying again.");
                // Show a notification with error message.
                showNotification(true);
                // Create a repeating alarm to fetch games until no exception is caught.
                updateGames(new DateTime().getMillis());
            } finally {
                dbHelper.close();
            }
        }

        /**
         * Create alarms for all the games scheduled for today.
         */
        private void createAlarms() {
            Log.i(TAG, "Creating alarms for games scheduled today");
            DatabaseContract.DbHelper dbHelper = new DatabaseContract.DbHelper(mContext);
            List<Game> gameList = dbHelper.selectUpcomingGames(new DateTime(Constants.DATE.VEGAS_TIME_ZONE).withTimeAtStartOfDay().getMillis());
            dbHelper.close();
            for (Game game : gameList) {
                Intent gameIntent = new Intent(mContext, GameUpdateReceiver.class);
                gameIntent.putExtra("game", game.get_id());
                PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, (int) game.get_id(), gameIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                long interval = game.getLeagueType().getRefreshInterval() * 1000L;
                AlarmManager manager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                manager.setRepeating(AlarmManager.RTC_WAKEUP, new DateTime(game.getGameDateTime(), Constants.DATE.VEGAS_TIME_ZONE).getMillis(), interval, pendingIntent);
            }
        }
    }

    /**
     * Relaunch this receiver after a a certain time starting from updateTime.
     *
     * @param updateTime the current epoch time.
     */
    private void updateGames(long updateTime) {
        Intent updateIntent = new Intent(mContext, UpdateReceiver.class);
        updateIntent.putExtra(STARTED_BY, ERROR_REPEAT);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, ALARM_ID_ERROR, updateIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager manager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        long interval = Integer.valueOf(MultiProcessPreference.getDefaultSharedPreferences()
                .getString(mContext.getString(R.string.key_retry_frequency), "15")) * 60 * 1000L;
        manager.setRepeating(AlarmManager.RTC_WAKEUP, updateTime + interval, interval, pendingIntent);
    }

    /**
     * Cancel all repeating alarms because the last fetch was a success.
     */
    private void cancelRepeatingUpdates() {
        Intent updateIntent = new Intent(mContext, UpdateReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, ALARM_ID_ERROR, updateIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        pendingIntent.cancel();
    }

    private void showNotification(boolean isError) {
        android.support.v4.app.NotificationCompat.Builder mBuilder =
                new android.support.v4.app.NotificationCompat.Builder(mContext)
                        .setSmallIcon(isError ? android.R.drawable.ic_dialog_alert : R.drawable.ic_league_white_24px)
                        .setContentTitle(isError ? "Error Fetching: Trying again in " + MultiProcessPreference.getDefaultSharedPreferences()
                                .getString(mContext.getString(R.string.key_retry_frequency), "15") + " min" : "Fetching games from Site");
        // Sets an ID for the notification
        int mNotificationId = 100;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    /**
     * Log update time to answers.
     */
    private void saveToAnswers() {
        DateTime dateTime = new DateTime(Constants.DATE.VEGAS_TIME_ZONE);
        Answers.getInstance().logCustom(new CustomEvent("Update Logger")
                .putCustomAttribute("Time Text", android.os.Build.MODEL + " " + startedBy + " " + dateTime.toString("hh:mm aa")));
    }
}