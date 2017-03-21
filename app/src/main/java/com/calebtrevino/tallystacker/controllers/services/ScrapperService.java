package com.calebtrevino.tallystacker.controllers.services;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.BadParcelableException;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.ServiceInterface;
import com.calebtrevino.tallystacker.ServiceListener;
import com.calebtrevino.tallystacker.controllers.events.GameAddedEvent;
import com.calebtrevino.tallystacker.controllers.events.GameModifiedEvent;
import com.calebtrevino.tallystacker.controllers.events.GameRemovedEvent;
import com.calebtrevino.tallystacker.controllers.receivers.UpdateReceiver;
import com.calebtrevino.tallystacker.models.preferences.MultiProcessPreference;
import com.calebtrevino.tallystacker.utils.StringUtils;
import com.calebtrevino.tallystacker.views.activities.ManualEntryActivity;
import com.calebtrevino.tallystacker.views.activities.SettingsActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.SubscriberExceptionEvent;
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
    public void onChildAdded(GameAddedEvent event) {
        synchronized (listeners) {
            for (ServiceListener listener : listeners) {
                try {
                    listener.gameAdded(event.getGameData());
                } catch (RemoteException | BadParcelableException ignore) { // // TODO: 2/27/2017 solve BadParcelableException error
                }
            }
        }
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onChildModified(GameModifiedEvent event) {
        synchronized (listeners) {
            for (ServiceListener listener : listeners) {
                try {
                    listener.gameModified(event.getGameData());
                } catch (RemoteException ignored) {
                }
            }
        }
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onChildRemoved(GameRemovedEvent event) {
        synchronized (listeners) {
            for (ServiceListener listener : listeners) {
                try {
                    listener.gameDeleted(event.getGameData());
                } catch (RemoteException e) {
                    Log.w(TAG, "Failed to notify listener " + listener, e);
                }
            }
        }
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onError(SubscriberExceptionEvent event) {
        event.throwable.printStackTrace();
    }

    /**
     * Update games only called at the creation of the service.
     *
     * @see #onCreate()
     */
    private void updateGames() {
        Intent updateIntent = new Intent(getBaseContext(), UpdateReceiver.class);
        updateIntent.putExtra(STARTED_BY, RESTART_REPEAT);
        sendBroadcast(updateIntent);
    }

    /**
     * Update games at a specific time with a repeating nature.
     *
     * @param updateTime  Time at which to update.
     * @param intentExtra Condition in which the update is initiated.
     */
    private void updateGames(long updateTime, String intentExtra) {
        Intent updateIntent = new Intent(getBaseContext(), UpdateReceiver.class);
        updateIntent.putExtra(STARTED_BY, intentExtra);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), UpdateReceiver.ALARM_ID, updateIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Log.i(TAG, "updateGames on " + new DateTime(updateTime).toString("hh:mm"));
        manager.setRepeating(AlarmManager.RTC_WAKEUP, updateTime, AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    /**
     * Called when service is initially started either by reboot/install/forced relaunch.
     * 1. Creates persistent notification to keep active in background.
     * 2. Sets the alarm for game updates and tries to perform a update.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Service creating");
//        startForegroundNotification();
        startManualEntryNotification();
        reloadTimer();
        updateGames();
        EventBus.getDefault().register(this);
    }

    private void startManualEntryNotification() {
        Intent resultIntent = new Intent(this, ManualEntryActivity.class);
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
                .setContentText(getString(R.string.click_to_set_manual))
                .setContentIntent(resultPendingIntent)
                .build();
        startForeground(102, notification);
    }

    /**
     * Called when service initially started and for every other call there after. Used for adjusting the time for games updates as changed in the setting.
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Service Starting");

        if (intent != null && intent.hasExtra(FETCH_TIME_CHANGE)) {
            reloadTimer();
        }
        return Service.START_STICKY;
    }


    /**
     * Change the time at which the games are to be fetched.
     */
    private void reloadTimer() {
        String updateTime = MultiProcessPreference.getDefaultSharedPreferences()
                .getString(getString(R.string.key_bid_update_time), "0:0");

        DateTime dateTime = new DateTime();
        dateTime = dateTime.withTimeAtStartOfDay().plusHours(StringUtils.getHour(updateTime)).plusMinutes(StringUtils.getMinute(updateTime));
        if (dateTime.isBeforeNow()) {
            dateTime = dateTime.plusDays(1);
        }
        updateGames(new DateTime(dateTime.getMillis()).toDateTime(DateTimeZone.getDefault()).getMillis(), NORMAL_REPEAT);
        Log.i(TAG, "Reloaded Timer");
    }

    /**
     * Persistent notification to keep the service alive in background.
     */
    private void startForegroundNotification() {
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
