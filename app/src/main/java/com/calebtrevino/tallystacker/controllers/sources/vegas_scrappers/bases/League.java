package com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.bases;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.RawRes;

import com.calebtrevino.tallystacker.controllers.sources.ScoreBoardParser;
import com.calebtrevino.tallystacker.controllers.sources.ScoreParser;
import com.calebtrevino.tallystacker.controllers.sources.espn_scrappers.exceptions.ExpectedElementNotFound;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.IntermediateResult;
import com.calebtrevino.tallystacker.models.enums.ScoreType;

import java.io.IOException;
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

    List<Game> pullGamesFromNetwork(Context context) throws IOException, ExpectedElementNotFound;

    String getPackageName();

    ScoreBoardParser getScoreBoardParser() throws ExpectedElementNotFound;

    long getRefreshInterval();

    void setRefreshInterval(long refreshInterval);

    String getBaseScoreUrl();

    @RawRes
    int getTeamResource();

    int getAvgTime();

    boolean hasSecondPhase();

    String getScoreBoardURL();

    IntermediateResult scrapeScoreBoard(ScoreParser scoreParser) throws ExpectedElementNotFound;

    ScoreParser getParser() throws ExpectedElementNotFound;
}
