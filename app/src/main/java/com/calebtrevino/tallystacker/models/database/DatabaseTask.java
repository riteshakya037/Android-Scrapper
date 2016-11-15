package com.calebtrevino.tallystacker.models.database;

import android.os.AsyncTask;

/**
 * @author Ritesh Shakya
 */

public abstract class DatabaseTask<T> extends AsyncTask<Object, Object, T> {
    private final DatabaseContract.DbHelper dbHelper;

    protected DatabaseTask(DatabaseContract.DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    protected T doInBackground(Object... params) {
        return executeStatement(dbHelper);
    }

    @Override
    protected void onPostExecute(T o) {
        callInUI(o);
    }

    protected abstract void callInUI(T o);

    protected abstract T executeStatement(DatabaseContract.DbHelper dbHelper);
}
