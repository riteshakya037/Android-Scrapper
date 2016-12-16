package com.calebtrevino.tallystacker.controllers.receivers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.database.DatabaseContract;
import com.calebtrevino.tallystacker.models.preferences.MultiProcessPreference;
import com.calebtrevino.tallystacker.utils.Constants;

import org.joda.time.DateTime;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * @author Ritesh Shakya
 */
public class GameUpdateReceiver extends BroadcastReceiver {
    private static final String TAG = GameUpdateReceiver.class.getName();
    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.mContext = context;
        long _id = intent.getLongExtra("game", 0L);
        Log.i(TAG, "onReceive game ID: " + _id);

        if (MultiProcessPreference.getDefaultSharedPreferences(context)
                .getBoolean(context.getString(R.string.key_notification_show), false)) {
            showNotification(_id);
        }
        cancelRepeatingUpdates(_id);
    }

    private void showNotification(long id) {
        DatabaseContract.DbHelper dbHelper = new DatabaseContract.DbHelper(mContext);
        Game game = dbHelper.onSelectGame(String.valueOf(id));
        dbHelper.close();
        DateTime dateTime = new DateTime(game.getGameDateTime(), Constants.DATE.VEGAS_TIME_ZONE).plusSeconds(60);
        if (dateTime.isAfterNow()) {
            String ringtonePath = MultiProcessPreference.getDefaultSharedPreferences(mContext).getString(mContext.getString(R.string.key_notification_ringtone), null);
            Uri soundUri;
            if (ringtonePath == null) {
                soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION); //// TODO: 9/27/2016
            } else {
                soundUri = Uri.parse(ringtonePath);
            }
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(mContext)
                            .setSmallIcon(R.drawable.ic_league_white_24px)
                            .setContentTitle("Game Started - " + game.getLeagueType().getAcronym())
                            .setContentText(mContext.getString(R.string.team_vs_team_full, game.getFirstTeam().getCity(), game.getSecondTeam().getCity()))
                            .setSound(soundUri);
            // Sets an ID for the notification
            int mNotificationId = createHash(game.getFirstTeam().getCity() + game.getSecondTeam().getCity());
            // Gets an instance of the NotificationManager service
            NotificationManager mNotifyMgr =
                    (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
            // Builds the notification and issues it.
            mNotifyMgr.notify(mNotificationId, mBuilder.build());
        }
    }

    private int createHash(String s) {
        int hash = 7;
        for (int i = 0; i < s.length(); i++) {
            hash = hash * 31 + s.charAt(i);
        }
        return hash;
    }

    private void cancelRepeatingUpdates(long _id) {
        Intent gameIntent = new Intent(mContext, GameUpdateReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, (int) _id, gameIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        pendingIntent.cancel();
    }
}
