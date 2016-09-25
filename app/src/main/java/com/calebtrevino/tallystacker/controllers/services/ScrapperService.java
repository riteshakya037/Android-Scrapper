package com.calebtrevino.tallystacker.controllers.services;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.controllers.receivers.GameUpdateReceiver;
import com.calebtrevino.tallystacker.controllers.sources.League;
import com.calebtrevino.tallystacker.controllers.sources.MLB_Total;
import com.calebtrevino.tallystacker.controllers.sources.WNBA_Total;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.database.DatabaseContract;
import com.calebtrevino.tallystacker.models.preferences.MultiProcessPreference;
import com.calebtrevino.tallystacker.utils.StringUtils;
import com.calebtrevino.tallystacker.views.activities.SettingsActivity;

import org.joda.time.DateTime;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ScrapperService extends Service {
    private static final String TAG = ScrapperService.class.getSimpleName();

    public ScrapperService() {
    }

    private Timer timer;

    private class UpdateTask extends TimerTask {
        @Override
        public void run() {
            createServiceAndAlarms();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Service creating");
        StartForegroundNotification();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Service Starting");
        reloadTimer();

        boolean firstRun = MultiProcessPreference.getDefaultSharedPreferences(getBaseContext())
                .getBoolean(getString(R.string.key_first_run), true);
        if (firstRun) {
            createServiceAndAlarms();
        }
        return Service.START_STICKY;
    }

    private void createServiceAndAlarms() {
        new GetLeague().execute();
        DatabaseContract.DbHelper dbHelper = new DatabaseContract.DbHelper(getBaseContext());
        List<Game> gameList = dbHelper.selectUpcomingGames(new DateTime().withTimeAtStartOfDay().getMillis());
        dbHelper.close();
        for (Game game : gameList) {
            Intent gameIntent = new Intent(GameUpdateReceiver.ACTION_GET_RESULT);
            gameIntent.putExtra("game", game.get_id());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), (int) game.get_id(), gameIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            long interval = game.getLeagueType().getRefreshInterval() * 1000L;
            AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            manager.setRepeating(AlarmManager.RTC_WAKEUP, game.getGameDateTime(), interval, pendingIntent);
        }
    }

    private void reloadTimer() {
        String updateTime = MultiProcessPreference.getDefaultSharedPreferences(getBaseContext())
                .getString(getString(R.string.key_bid_update_time), "0:0");

        DateTime dateTime = new DateTime();
        dateTime = dateTime.withTimeAtStartOfDay().plusHours(StringUtils.getHour(updateTime)).plusMinutes(StringUtils.getMinute(updateTime));
        if (dateTime.isBeforeNow()) {
            dateTime = dateTime.plusDays(1);
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        timer = new Timer("Get Bets");
        timer.schedule(new UpdateTask(), new Date(dateTime.getMillis()), 24 * 60 * 60 * 1000L);
    }

    private void StartForegroundNotification() {
        Intent resultIntent = new Intent(this, SettingsActivity.class);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        Notification notification = new NotificationCompat.Builder(this)
                .setOngoing(false)
                .setSmallIcon(R.drawable.ic_league_white_24px)
                .setColor(getResources().getColor(R.color.colorAccent))
                .setContentTitle(getString(R.string.running_in_background))
                .setContentText(getString(R.string.change_fetch_times))
                .setContentIntent(resultPendingIntent)
                .build();
        startForeground(101, notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Service destroying");

        timer.cancel();
        timer = null;
    }

    private class GetLeague extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            Log.i(TAG, "Timer task started bid work");
            League league = new WNBA_Total();
            League league2 = new MLB_Total();

            try {
                league.pullGamesFromNetwork(getApplicationContext());
                league2.pullGamesFromNetwork(getApplicationContext());
                DatabaseContract.DbHelper dbHelper = new DatabaseContract.DbHelper(getApplicationContext());
                dbHelper.addGamesToGrids();
                dbHelper.close();
                MultiProcessPreference.getDefaultSharedPreferences(getBaseContext())
                        .edit().putBoolean(getString(R.string.key_first_run), false).commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
