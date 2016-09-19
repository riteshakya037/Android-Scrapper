package com.calebtrevino.tallystacker.models.database;

import android.os.AsyncTask;

/**
 * @author Ritesh Shakya
 */

public abstract class DatabaseTask extends AsyncTask<Object, Object, Object> {
    private final DatabaseContract.DbHelper dbHelper;

    public DatabaseTask(DatabaseContract.DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    protected Object doInBackground(Object... params) {
        return executeStatement(dbHelper);
    }

    @Override
    protected void onPostExecute(Object o) {
        callInUI(o);
    }

    protected abstract void callInUI(Object o);

    protected abstract Object executeStatement(DatabaseContract.DbHelper dbHelper);
}
