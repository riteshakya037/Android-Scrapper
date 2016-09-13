package com.calebtrevino.tallystacker.models.database;

import android.os.AsyncTask;

/**
 * Created by fatal on 9/13/2016.
 */

public abstract class DatabaseTask extends AsyncTask<Object, Object, Object> {
    private DatabaseContract.DbHelper dbHelper;
    private Object object;

    public DatabaseTask(DatabaseContract.DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    protected Object doInBackground(Object... params) {
        object = executeStatement(dbHelper);
        return object;
    }

    @Override
    protected void onPostExecute(Object o) {
        callInUI(o);
    }

    protected abstract void callInUI(Object o);

    protected abstract Object executeStatement(DatabaseContract.DbHelper dbHelper);
}
