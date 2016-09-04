package com.calebtrevino.tallystacker.controllers.sources;

import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.enums.ScoreType;

import java.util.List;

/**
 * Created by fatal on 9/3/2016.
 */

public interface League {
    public ScoreType getScoreType();

    public String getName();

    public String getAcronym();

    public String getBaseUrl();

    public String getCSSQuery();

    public List<Game> pullGamesFromNetwork();

}
