package com.calebtrevino.tallystacker.controllers.services;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.ServiceInterface;
import com.calebtrevino.tallystacker.ServiceListener;
import com.calebtrevino.tallystacker.controllers.receivers.GameUpdateReceiver;
import com.calebtrevino.tallystacker.controllers.sources.CFL_Spread;
import com.calebtrevino.tallystacker.controllers.sources.CFL_Total;
import com.calebtrevino.tallystacker.controllers.sources.MLB_Total;
import com.calebtrevino.tallystacker.controllers.sources.NBA_Spread;
import com.calebtrevino.tallystacker.controllers.sources.NBA_Total;
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
import com.calebtrevino.tallystacker.models.preferences.MultiProcessPreference;
import com.calebtrevino.tallystacker.utils.StringUtils;
import com.calebtrevino.tallystacker.views.activities.SettingsActivity;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ScrapperService extends Service {
    private static final String TAG = ScrapperService.class.getSimpleName();
    public static final String FETCH_TIME_CHANGE = "fetch_time_change";
    private final List<ServiceListener> listeners = new ArrayList<>();

    private ServiceInterface.Stub serviceInterface = new ServiceInterface.Stub() {
        @Override
        public void addListener(ServiceListener listener) throws RemoteException {
            synchronized (listeners) {
                listeners.add(listener);
            }
        }

        @Override
        public void removeListener(ServiceListener listener) throws RemoteException {
            synchronized (listeners) {
                listeners.remove(listener);
            }
        }
    };

    public ScrapperService() {
    }

    private Timer timer;

    private class UpdateTask extends TimerTask {
        @Override
        public void run() {
            createServiceAndAlarms(true);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Service creating");
        StartForegroundNotification();
        reloadTimer();
        createServiceAndAlarms(true);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Service Starting");

        if (intent != null && intent.hasExtra(FETCH_TIME_CHANGE)) {
            reloadTimer();
        }
        return Service.START_STICKY;
    }

    private void createServiceAndAlarms(boolean getBids) {
        new GetLeague().execute(getBids);
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
        Log.i(TAG, "Reloaded Timer");
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
                .setContentText(getString(R.string.change_settings))
                .setContentIntent(resultPendingIntent)
                .build();
        startForeground(101, notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return serviceInterface;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Service destroying");

        timer.cancel();
        timer = null;
    }

    private class GetLeague extends AsyncTask<Boolean, String, String> {

        @Override
        protected String doInBackground(Boolean... getBids) {
            Log.i(TAG, "Timer task started bid work");
            if (getBids[0]) {
                fetchGames();
            }
            createAlarms();
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
                    new Soccer_Spread()
            };
            DatabaseContract.DbHelper dbHelper;
            boolean nullList = true;
            dbHelper = new DatabaseContract.DbHelper(getApplicationContext());
            try {
                for (League league : leagueList) {
                    List<Game> gameList = league.pullGamesFromNetwork(getApplicationContext());
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

                synchronized (listeners) {
                    for (ServiceListener listener : listeners) {
                        try {
                            listener.databaseReady();
                        } catch (RemoteException e) {
                            Log.w(TAG, "Failed to notify listener " + listener, e);
                        }
                    }
                }

                MultiProcessPreference.getDefaultSharedPreferences(getBaseContext())
                        .edit().putBoolean(getString(R.string.key_first_run), false).commit();
            } catch (Exception e) {
                e.printStackTrace();
                Timer delayedTimer = new Timer();
                delayedTimer.schedule(new UpdateTask(), 15 * 60 * 1000L);
            } finally {
                dbHelper.close();
            }
        }

        private void createAlarms() {
            Log.i(TAG, "Creating Pending Intents");
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
    }
}
