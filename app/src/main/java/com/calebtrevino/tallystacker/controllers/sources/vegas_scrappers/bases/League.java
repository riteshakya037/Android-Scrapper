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
    /**
     * @return Score Type either Spread or Total
     */
    ScoreType getScoreType();

    /**
     * @return Name of the League
     */
    String getName();

    /**
     * @return Acronym of the League
     */
    String getAcronym();

    /**
     * @return Base url for vegasinsider.
     */
    String getBaseUrl();

    /**
     * @return {@link org.jsoup.Jsoup} query for scraping rows.
     */
    String getCSSQuery();

    /**
     * Scrape the games with the help of Jsoup.
     *
     * @throws IOException when cannot jsoup connect to internet
     * @throws ExpectedElementNotFound when we don't find a single game in {@link ScoreBoardParser}
     */
    List<Game> pullGamesFromNetwork(Context context) throws IOException, ExpectedElementNotFound;

    /**
     * @return Package name of league (used to get league type from database).
     */
    String getPackageName();

    /**
     * Class that handles scoreboard related parsing.
     *
     * @throws ExpectedElementNotFound when we don't find a single game
     */
    ScoreBoardParser getScoreBoardParser() throws ExpectedElementNotFound;

    /**
     * Flushes scoreboard to clear memory.
     */
    void clearScoreBoardParser() ;

    /**
     * @return The interval for which we fetch the scores.
     */
    long getRefreshInterval();

    /**
     * Set the interval for which we fetch the scores.
     *
     * @param refreshInterval interval for refresh.
     */
    void setRefreshInterval(long refreshInterval);

    /**
     * @return Base url for scoreboard parsing
     */
    String getBaseScoreUrl();

    /**
     * @return Resource file that contains a map of teams to their site
     */
    @RawRes
    int getTeamResource();

    /**
     * @return The average time the league completes in
     */
    int getAvgTime();

    /**
     * @return {@code true} if second phase has been implemented for the league.
     */
    boolean hasSecondPhase();

    /**
     * @return The url for specific game in consideration
     */
    String getScoreBoardURL();

    /**
     * Stores the result of the game at a given time.
     *
     * @throws ExpectedElementNotFound when we don't find a single game
     */
    IntermediateResult scrapeScoreBoard(ScoreParser scoreParser) throws ExpectedElementNotFound;

    /**
     * @throws ExpectedElementNotFound when we don't find a single game
     */
    ScoreParser getParser() throws ExpectedElementNotFound;

    /**
     * Contrary ScoreType for the one is consideration.
     * Spread => Total
     * Total => Spread
     *
     * @return Contrary league
     */
    String getContraryPackageName();
}
