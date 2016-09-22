package com.calebtrevino.tallystacker.controllers.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.controllers.sources.League;
import com.calebtrevino.tallystacker.controllers.sources.MLB_Total;
import com.calebtrevino.tallystacker.controllers.sources.WNBA_Total;
import com.calebtrevino.tallystacker.models.database.DatabaseContract;
import com.calebtrevino.tallystacker.views.activities.MainActivity;
import com.calebtrevino.tallystacker.views.activities.SettingsActivity;

import java.util.Timer;
import java.util.TimerTask;

public class ScrapperService extends Service {
    public static final String GAMES = "games";
    private static final String TAG = ScrapperService.class.getSimpleName();

    public ScrapperService() {
    }

    private Timer timer;
    private TimerTask updateTask = new TimerTask() {
        @Override
        public void run() {
            Log.i(TAG, "Timer task doing work");
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        StartForeground();
        new GetLeague().execute();

//        Intent gameIntent = new Intent(GameUpdateReceiver.ACTION_GET_RESULT);
//        gameIntent.putExtra("game", gameData.get_id());
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, (int) gameData.get_id(), gameIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//        int interval = 20000;
//        AlarmManager manager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
//
//        manager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() - interval, pendingIntent);
        return Service.START_REDELIVER_INTENT;
    }

    private void StartForeground() {
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
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Service creating");

        timer = new Timer("TweetCollectorTimer");
        timer.schedule(updateTask, 1000L, 60 * 1000L);
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
            League league = new WNBA_Total();
            League league2 = new MLB_Total();

            try {
                league.pullGamesFromNetwork(getApplicationContext());
                league2.pullGamesFromNetwork(getApplicationContext());
                DatabaseContract.DbHelper dbHelper=new DatabaseContract.DbHelper(getApplicationContext());
                dbHelper.addGamesToGrids();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
