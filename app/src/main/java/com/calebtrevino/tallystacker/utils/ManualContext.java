package com.calebtrevino.tallystacker.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.database.DatabaseContract;
import java.util.List;

/**
 * @author Ritesh Shakya
 */
public class ManualContext {
    private static final String TAG = ManualContext.class.getSimpleName();
    @SuppressLint("StaticFieldLeak") private static ManualContext _instance;
    private Context context;
    private List<Game> gameList;

    private ManualContext(Context context) {
        this.context = context;
    }

    public static ManualContext getInstance(Context context) {
        if (_instance == null) _instance = new ManualContext(context);
        return _instance;
    }

    public void getManualGames() {
        DatabaseContract.DbHelper dbHelper = new DatabaseContract.DbHelper(context);
        gameList = dbHelper.getManualGames();
        Log.i(TAG, "getManualGames: " + gameList);
    }

    public List<Game> getGameList() {
        return gameList;
    }
}
