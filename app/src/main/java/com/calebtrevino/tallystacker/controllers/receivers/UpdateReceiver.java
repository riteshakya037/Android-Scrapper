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
 * @author Ritesh Shakya
 */

public class UpdateReceiver extends BroadcastReceiver implements ChildGameEventListener {
    public static final int ALARM_ID = 15927;
    public static final int ALARM_ID_ERROR = 15928;
    private static String TAG = UpdateReceiver.class.getName();
    public static final String ACTION_GET_UPDATE = TAG + ".UPDATE_RECEIVER";
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
        onKeyMetric();
        new GetLeague().execute();
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
                    List<Game> gameList = league.pullGamesFromNetwork(mContext);
                    if (gameList.size() != 0 && nullList) {
                        for (Game game : gameList) {
                            if (dbHelper.checkForGame(game.getLeagueType(), game.getFirstTeam(), game.getSecondTeam(), game.getGameDateTime()) != 0L && nullList) {
                                nullList = false;
                            }
                        }
                    }
                }
                if (nullList) {
                    throw new Exception("Ignore");
                }
                dbHelper.addGamesToGrids();


                MultiProcessPreference.getDefaultSharedPreferences(mContext)
                        .edit().putBoolean(mContext.getString(R.string.key_first_run), false).commit();
                createAlarms();
            } catch (Exception e) {
                e.printStackTrace();
                showNotification(true);
                updateGames(new DateTime().getMillis());
            } finally {
                dbHelper.close();
            }
        }

        private void createAlarms() {
            Log.i(TAG, "Creating Pending Intents");
            DatabaseContract.DbHelper dbHelper = new DatabaseContract.DbHelper(mContext);
            List<Game> gameList = dbHelper.selectUpcomingGames(new DateTime(Constants.DATE.VEGAS_TIME_ZONE).withTimeAtStartOfDay().getMillis());
            dbHelper.close();
            for (Game game : gameList) {
                Intent gameIntent = new Intent(GameUpdateReceiver.ACTION_GET_RESULT);
                gameIntent.putExtra("game", game.get_id());
                PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, (int) game.get_id(), gameIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                long interval = game.getLeagueType().getRefreshInterval() * 1000L;
                AlarmManager manager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                manager.setRepeating(AlarmManager.RTC_WAKEUP, new DateTime(game.getGameDateTime(), Constants.DATE.VEGAS_TIME_ZONE).getMillis(), interval, pendingIntent);
            }
        }
    }

    public void updateGames(long updateTime) {
        Intent updateIntent = new Intent(UpdateReceiver.ACTION_GET_UPDATE);
        updateIntent.putExtra(STARTED_BY, ERROR_REPEAT);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, UpdateReceiver.ALARM_ID_ERROR, updateIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager manager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        long interval = Integer.valueOf(MultiProcessPreference.getDefaultSharedPreferences(mContext)
                .getString(mContext.getString(R.string.key_retry_frequency), "15")) * 60 * 1000L;
        manager.setRepeating(AlarmManager.RTC_WAKEUP, updateTime + interval, interval, pendingIntent);
    }

    private void showNotification(boolean isError) {
        android.support.v4.app.NotificationCompat.Builder mBuilder =
                new android.support.v4.app.NotificationCompat.Builder(mContext)
                        .setSmallIcon(isError ? android.R.drawable.ic_dialog_alert : R.drawable.ic_league_white_24px)
                        .setContentTitle(isError ? "Error Fetching: Trying again in " + MultiProcessPreference.getDefaultSharedPreferences(mContext)
                                .getString(mContext.getString(R.string.key_retry_frequency), "15") + " min" : "Fetching games from Site");
        // Sets an ID for the notification
        int mNotificationId = 100;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    public void onKeyMetric() {
        DateTime dateTime = new DateTime(Constants.DATE.VEGAS_TIME_ZONE);
        Answers.getInstance().logCustom(new CustomEvent("Update Logger")
                .putCustomAttribute("Time Text", android.os.Build.MODEL + " " + startedBy + " " + dateTime.toString("hh:mm aa")));
    }
}