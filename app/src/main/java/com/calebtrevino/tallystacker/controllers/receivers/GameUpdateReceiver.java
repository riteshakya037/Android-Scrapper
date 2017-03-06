package com.calebtrevino.tallystacker.controllers.receivers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.controllers.sources.CalculateResult;
import com.calebtrevino.tallystacker.controllers.sources.espn_scrappers.EspnScoreboardParser;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.IntermediateResult;
import com.calebtrevino.tallystacker.models.database.DatabaseContract;
import com.calebtrevino.tallystacker.models.preferences.MultiProcessPreference;
import com.calebtrevino.tallystacker.utils.Constants;
import com.calebtrevino.tallystacker.utils.StringUtils;
import com.crashlytics.android.Crashlytics;

import org.joda.time.DateTime;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Launched when a certain game starts.
 *
 * @author Ritesh Shakya
 */
public class GameUpdateReceiver extends BroadcastReceiver {
    private static final String TAG = GameUpdateReceiver.class.getName();
    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.mContext = context;
        long _id = intent.getLongExtra("game", 0L);

        new GetScores(context).execute(_id);
    }

    private void showNotification(Game game) {
        DateTime dateTime = new DateTime(game.getGameDateTime(), Constants.DATE.VEGAS_TIME_ZONE).plusSeconds(60);
        if (dateTime.isAfterNow()) {
            String ringtonePath = MultiProcessPreference.getDefaultSharedPreferences().getString(mContext.getString(R.string.key_notification_ringtone), null);
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

    private void showNotification(Game game, IntermediateResult result) {
        DateTime dateTime = new DateTime(game.getGameDateTime(), Constants.DATE.VEGAS_TIME_ZONE).plusSeconds(60);
        if (dateTime.isAfterNow()) {
            String ringtonePath = MultiProcessPreference.getDefaultSharedPreferences().getString(mContext.getString(R.string.key_notification_ringtone), null);
            Uri soundUri;
            if (ringtonePath == null) {
                soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION); //// TODO: 9/27/2016
            } else {
                soundUri = Uri.parse(ringtonePath);
            }
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(mContext)
                            .setSmallIcon(R.drawable.ic_league_white_24px)
                            .setContentTitle("Game Started - " + result.getTeamScore(game.getFirstTeam()) + " - " + result.getTeamScore(game.getSecondTeam()))
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

    private class GetScores extends AsyncTask<Long, Void, Void> {

        private Context context;

        GetScores(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Long... _ids) {
            DatabaseContract.DbHelper dbHelper = new DatabaseContract.DbHelper(mContext);
            Game game = dbHelper.onSelectGame(String.valueOf(_ids[0]));
            Log.i(TAG, "onReceive " + game.getFirstTeam().getName() + " - " + game.getSecondTeam().getName());

            // Only shows notification if enabled from the settings.
            if (MultiProcessPreference.getDefaultSharedPreferences()
                    .getBoolean(context.getString(R.string.key_notification_show), false)) {
                showNotification(game);
            }
            // Check if second phase implemented.
            if (game.getLeagueType().hasSecondPhase()) {
                if (StringUtils.isNull(game.getGameUrl())) {
                    try {
                        EspnScoreboardParser.getObject(game.getLeagueType()).setGameUrl(game);
                        if (StringUtils.isNull(game.getGameUrl())) {
                            cancelRepeatingUpdates(game.get_id());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (game.isComplete()) {
                    Log.i(TAG, "isComplete: " + game.isComplete());
                    cancelRepeatingUpdates(game.get_id());
                } else {
                    try {
                        IntermediateResult result = game.getLeagueType().getParser().getCurrentScore(game);
                        CalculateResult.ResultOut resultOut = (new CalculateResult()).calculateResult(game, result);
                        showNotification(game, result);
                        if (resultOut.isGameCompleted()) {
                            // Game scores reached conclusion needed.
                            CalculateResult.setResult(game, result, resultOut, true);
                            Log.i(TAG, "isGameCompleted: " + game);
                            dbHelper.onUpdateGame(game.get_id(), game);
                            // By default the alarm is calibrated so that if checks for game status. Thus if a game is completed or the bid condition matched we have to stop it manually.
                            cancelRepeatingUpdates(game.get_id());
                        } else {
                            CalculateResult.setResult(game, result, resultOut, false);
                            dbHelper.onUpdateGame(game.get_id(), game);
                            //reschedule update
                        }
                    } catch (Exception e) {
                        Crashlytics.logException(e);
                        e.printStackTrace();
                    }
                }
            } else {
                Log.i(TAG, "Cancel : " + game.getFirstTeam().getName() + " - " + game.getSecondTeam().getName());
                cancelRepeatingUpdates(game.get_id());
            }
            dbHelper.close();
            return null;
        }
    }

    /**
     * Create unique id for each notification.
     *
     * @param s A unique attribute of the game.
     * @return hashed value of the received param
     */
    private int createHash(String s) {
        int hash = 7;
        for (int i = 0; i < s.length(); i++) {
            hash = hash * 31 + s.charAt(i);
        }
        return hash;
    }

    /**
     * By default the alarm is calibrated so that if checks for game status. Thus if a game is completed or the bid condition matched we have to stop it manually.
     *
     * @param _id id of the game, also used as the id of the Pending Alarm.
     */
    private void cancelRepeatingUpdates(long _id) {
        Intent gameIntent = new Intent(mContext, GameUpdateReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, (int) _id, gameIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        pendingIntent.cancel();
    }
}
