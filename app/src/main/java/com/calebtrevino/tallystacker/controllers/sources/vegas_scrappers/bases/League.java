package com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.bases;

import android.content.Context;
import android.os.Parcelable;

import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.enums.ScoreType;

import java.util.List;

/**
 * @author Ritesh Shakya
 */

public interface League extends Parcelable {
    ScoreType getScoreType();

    String getName();

    String getAcronym();

    String getBaseUrl();

    String getCSSQuery();

    List<Game> pullGamesFromNetwork(Context context) throws Exception;

    String getPackageName();

    long getRefreshInterval();

    void setRefreshInterval(long refreshInterval);

    String getEspnUrl();
}
