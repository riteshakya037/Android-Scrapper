package com.calebtrevino.tallystacker.controllers.services;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.ServiceInterface;
import com.calebtrevino.tallystacker.ServiceListener;
import com.calebtrevino.tallystacker.controllers.receivers.UpdateReceiver;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.preferences.MultiProcessPreference;
import com.calebtrevino.tallystacker.utils.StringUtils;
import com.calebtrevino.tallystacker.views.activities.SettingsActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.ArrayList;
import java.util.List;

import static com.calebtrevino.tallystacker.controllers.receivers.UpdateReceiver.NORMAL_REPEAT;
import static com.calebtrevino.tallystacker.controllers.receivers.UpdateReceiver.RESTART_REPEAT;
import static com.calebtrevino.tallystacker.controllers.receivers.UpdateReceiver.STARTED_BY;

@SuppressWarnings("SameParameterValue")
public class ScrapperService extends Service {
    private static final String TAG = ScrapperService.class.getSimpleName();
    public static final String FETCH_TIME_CHANGE = "fetch_time_change";
    private final List<ServiceListener> listeners = new ArrayList<>();

    private final ServiceInterface.Stub serviceInterface = new ServiceInterface.Stub() {
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


    @SuppressWarnings("unused")
    @Subscribe
    public void onChildAdded(Game game) {
        synchronized (listeners) {
            for (ServiceListener listener : listeners) {
                try {
                    listener.databaseReady(game);
                } catch (RemoteException e) {
                    Log.w(TAG, "Failed to notify listener " + listener, e);
                }
            }
        }
    }

    private void updateGames() {
        Intent updateIntent = new Intent(getBaseContext(), UpdateReceiver.class);
        updateIntent.putExtra(STARTED_BY, RESTART_REPEAT);
        sendBroadcast(updateIntent);
    }

    private void updateGames(long updateTime, String intentExtra) {
        Intent updateIntent = new Intent(getBaseContext(), UpdateReceiver.class);
        updateIntent.putExtra(STARTED_BY, intentExtra);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), UpdateReceiver.ALARM_ID, updateIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Log.i(TAG, "updateGames on " + new DateTime(updateTime).toString("hh:mm"));
        manager.setRepeating(AlarmManager.RTC_WAKEUP, updateTime, AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Service creating");
        StartForegroundNotification();
        reloadTimer();
        updateGames();
        EventBus.getDefault().register(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Service Starting");

        if (intent != null && intent.hasExtra(FETCH_TIME_CHANGE)) {
            reloadTimer();
        }
        return Service.START_STICKY;
    }


    private void reloadTimer() {
        String updateTime = MultiProcessPreference.getDefaultSharedPreferences(getBaseContext())
                .getString(getString(R.string.key_bid_update_time), "0:0");

        DateTime dateTime = new DateTime();
        dateTime = dateTime.withTimeAtStartOfDay().plusHours(StringUtils.getHour(updateTime)).plusMinutes(StringUtils.getMinute(updateTime));
        if (dateTime.isBeforeNow()) {
            dateTime = dateTime.plusDays(1);
        }
        updateGames(new DateTime(dateTime.getMillis()).toDateTime(DateTimeZone.getDefault()).getMillis(), NORMAL_REPEAT);
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
                .setColor(ContextCompat.getColor(getBaseContext(), R.color.colorAccent))
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
        EventBus.getDefault().unregister(this);
    }

}
