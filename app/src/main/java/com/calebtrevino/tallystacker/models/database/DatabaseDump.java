package com.calebtrevino.tallystacker.models.database;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.content.FileProvider;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.views.activities.TallyStackerApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * @author Ritesh Shakya
 */
public class DatabaseDump {
    private static final String CHILD = "database";
    private static final String DATABASE_NAME = "database.json";
    private final SQLiteDatabase mDatabase;
    private final Context mContext;
    private ProgressDialog progress;

    private DatabaseDump(Context context) {
        mDatabase = new DatabaseContract.DbHelper(context).getReadableDatabase();
        mContext = context;
    }

    public static DatabaseDump getInstance(Context context) {
        return new DatabaseDump(context);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void saveDatabase(String jsonData) {
        try {
            File cachePath = new File(TallyStackerApplication.get().getCacheDir(), CHILD);
            cachePath.mkdirs();
            FileOutputStream stream = new FileOutputStream(cachePath + "/" + DATABASE_NAME);
            BufferedOutputStream bos = new BufferedOutputStream(stream);

            Exporter mExporter = new Exporter(bos);
            mExporter.writeJson(jsonData);
            mExporter.close();
            stream.close();
            File newFile = new File(cachePath, DATABASE_NAME);
            Uri contentUri = FileProvider.getUriForFile(mContext, "com.calebtrevino.tallystacker.file_provider", newFile);
            if (contentUri != null) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"riteshakya037@gmail.com"});
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "TallyStacker Database");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Please have a look");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                shareIntent.setType(mContext.getContentResolver().getType(contentUri));
                mContext.startActivity(Intent.createChooser(shareIntent, mContext.getResources().getText(R.string.send_to)));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportData() {

        progress = ProgressDialog.show(mContext, "Saving Database",
                "Fetching data.", true, false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String sql = "SELECT * FROM sqlite_master";

                    Cursor cur = mDatabase.rawQuery(sql, new String[0]);
                    cur.moveToFirst();
                    JSONObject rowObject = new JSONObject();

                    String tableName;
                    while (cur.getPosition() < cur.getCount()) {
                        tableName = cur.getString(cur.getColumnIndex("name"));

                        // don't process these two tables since they are used
                        // for metadata
                        if (!"android_metadata".equals(tableName)
                                && !"sqlite_sequence".equals(tableName)) {
                            rowObject.put(tableName, exportTable(tableName));
                        }
                        cur.moveToNext();
                    }
                    cur.close();
                    saveDatabase(rowObject.toString());
                    progress.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private JSONArray exportTable(String tableName) throws JSONException {

        // get everything from the table
        String sql = "select * from " + tableName;
        Cursor cur = mDatabase.rawQuery(sql, new String[0]);
        int numCols = cur.getColumnCount();
        JSONArray resultSet = new JSONArray();
        cur.moveToFirst();

        // move through the table, creating rows
        // and adding each column with name and value
        // to the row
        while (cur.getPosition() < cur.getCount()) {
            JSONObject rowObject = new JSONObject();
            for (int idx = 0; idx < numCols; idx++) {
                rowObject.put(cur.getColumnName(idx), cur.getString(idx));
            }

            resultSet.put(rowObject);
            cur.moveToNext();
        }

        cur.close();
        return resultSet;
    }

    private class Exporter {

        private final BufferedOutputStream mBufferOS;


        Exporter(BufferedOutputStream bos) {
            mBufferOS = bos;
        }

        public void close() throws IOException {
            if (mBufferOS != null) {
                mBufferOS.close();
            }
        }

        private void writeJson(String jsonData) throws IOException {
            mBufferOS.write(jsonData.getBytes());
        }
    }

}
