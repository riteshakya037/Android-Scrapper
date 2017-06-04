package com.calebtrevino.tallystacker.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.models.preferences.MultiProcessPreference;
import com.calebtrevino.tallystacker.presenters.events.ErrorEvent;
import com.calebtrevino.tallystacker.views.activities.TallyStackerApplication;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import static com.calebtrevino.tallystacker.views.fragments.DashFragment.LOG_FILE_LOCATION;

/**
 * @author Ritesh Shakya
 */
public class LogWriter {
    public static final int LOG_RESULT = 0;
    private static final String CHILD = "database";
    private static final String LOG_FILE = "log.txt";

    public static void writeLog(Context baseContext) {
        PackageInfo info = getPackageInfo(baseContext);
        String model = getModel();

        // Make file name - file must be saved to external storage or it wont be readable by
        // the email app.
        File cachePath = new File(TallyStackerApplication.get().getCacheDir(), CHILD);
        cachePath.mkdirs();
        File file = new File(cachePath, LOG_FILE);

        FileWriter writer = null;
        InputStreamReader reader = null;
        try {
            reader = getInputStreamReader();
            // write output stream
            writer = writeOutput(info, model, file, reader);

            reader.close();
            writer.close();
        } catch (IOException e) {
            if (writer != null)
                try {
                    writer.close();
                } catch (IOException ignored) {
                }
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException ignored) {
                }
        }
        MultiProcessPreference.getDefaultSharedPreferences().edit().putString(LOG_FILE_LOCATION, file.getAbsolutePath()).commit();
    }

    @NonNull
    private static FileWriter writeOutput(PackageInfo info, String model, File file, InputStreamReader reader) throws IOException {
        FileWriter writer = new FileWriter(file);
        writer.write("Android version: " + Build.VERSION.SDK_INT + "\n");
        writer.write("Device: " + model + "\n");
        writer.write("App version: " + (info == null ? "(null)" : info.versionCode) + "\n");

        char[] buffer = new char[10000];
        do {
            int n = reader.read(buffer, 0, buffer.length);
            if (n == -1)
                break;
            writer.write(buffer, 0, n);
        } while (true);
        return writer;
    }

    @NonNull
    private static InputStreamReader getInputStreamReader() throws IOException {
        // For Android 4.0 and earlier, you will get all app's log output, so filter it to
        // mostly limit it to your app's output.  In later versions, the filtering isn't needed.
        String cmd = (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) ?
                "logcat -d -v time MyApp:v dalvikvm:v System.err:v *:s" :
                "logcat -d -v time";

        // get input stream
        Process process = Runtime.getRuntime().exec(cmd);
        return new InputStreamReader(process.getInputStream());
    }

    @NonNull
    private static String getModel() {
        String model = Build.MODEL;
        if (!model.startsWith(Build.MANUFACTURER))
            model = Build.MANUFACTURER + " " + model;
        return model;
    }

    @Nullable
    private static PackageInfo getPackageInfo(Context baseContext) {
        PackageManager manager = baseContext.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(baseContext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return info;
    }

    public static void sendLog(FragmentActivity context) {
        File file = new File(MultiProcessPreference.getDefaultSharedPreferences().getString(LOG_FILE_LOCATION, ""));
        if (!file.exists())
            return;
        Uri contentUri = FileProvider.getUriForFile(context, "com.calebtrevino.tallystacker.file_provider", file);
        if (contentUri != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"riteshakya037@gmail.com"});
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "TallyStacker Database");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Please have a look");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            shareIntent.setType(context.getContentResolver().getType(contentUri));
            context.startActivityForResult(Intent.createChooser(shareIntent, context.getResources().getText(R.string.send_to)), LOG_RESULT);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    MultiProcessPreference.getDefaultSharedPreferences().edit().putString(LOG_FILE_LOCATION, "").commit();
                    EventBus.getDefault().post(new ErrorEvent(false));
                }
            }, 10 * 1000);

        }
    }
}
