package com.calebtrevino.tallystacker.controllers.sources;

import android.app.Activity;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;

import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.enums.ScoreType;

import java.util.List;

/**
 * Created by fatal on 9/3/2016.
 */

public interface League extends Parcelable {
    public ScoreType getScoreType();

    public String getName();

    public String getAcronym();

    public String getBaseUrl();

    public String getCSSQuery();

    public List<Game> pullGamesFromNetwork(Activity appCompatActivity) throws Exception;

    public String getPackageName();
}
