package com.calebtrevino.tallystacker.controllers.sources.sofascore_scrappers;

import android.util.Log;

import com.calebtrevino.tallystacker.controllers.sources.ScoreParser;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.IntermediateResult;
import com.calebtrevino.tallystacker.models.enums.GameStatus;
import com.calebtrevino.tallystacker.models.preferences.MultiProcessPreference;
import com.calebtrevino.tallystacker.models.sofascore.Event;
import com.calebtrevino.tallystacker.models.sofascore.SofaScoreJson;
import com.calebtrevino.tallystacker.utils.DateUtils;
import com.google.gson.Gson;

import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ritesh Shakya
 */

public class SofaScoreParser extends ScoreParser {

    private static final String TAG = SofaScoreParser.class.getSimpleName();
    private static final String LAST_UPDATE = "sofa_last_update";
    private static SofaScoreParser _instance;
    private Game game;
    private List<Event> gameStatusMap = new ArrayList<>();

    private void init() {
        if (MultiProcessPreference.getDefaultSharedPreferences().getLong(LAST_UPDATE, 0) < new DateTime().minusMinutes(15).getMillis()) {
            Log.i(TAG, "init: fetched");
            try {
                for (int i = -1; i < 1; i++) {
                    Document document = Jsoup.connect(game.getLeagueType().getBaseScoreUrl() + DateUtils.getDatePlus("yyyy-MM-dd", i) + "/json")
                            .timeout(60 * 1000)
                            .maxBodySize(0)
                            .header("Accept", "text/javascript")
                            .ignoreContentType(true)
                            .get();
                    SofaScoreJson espnJson = new Gson().fromJson(document.text(), SofaScoreJson.class);
                    gameStatusMap.addAll(espnJson.getEvents());
                }
                MultiProcessPreference.getDefaultSharedPreferences().edit().putLong(LAST_UPDATE, new DateTime().getMillis()).commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static SofaScoreParser getInstance() throws Exception {
        if (_instance == null) {
            _instance = new SofaScoreParser();
        }
        return _instance;
    }

    @Override
    public IntermediateResult getCurrentScore(Game game) throws Exception {
        this.game = game;
        if (game.getLeagueType().hasSecondPhase()) {
            this.init();
        }
        return game.getLeagueType().scrapeScoreBoard(this);
    }

    public IntermediateResult scrapeUsual() throws Exception {
        IntermediateResult result = new IntermediateResult();
        for (Event entry : gameStatusMap) {
            if (entry.getAwayTeam().getId().equals(game.getFirstTeam().getAcronym()) && entry.getHomeTeam().getId().equals(game.getSecondTeam().getAcronym()) ||
                    entry.getHomeTeam().getId().equals(game.getSecondTeam().getAcronym()) || entry.getAwayTeam().getId().equals(game.getSecondTeam().getAcronym())) {
                Log.i(TAG, "checkGameCompletion: Team Match");
                // If the game status is completed.
                if (entry.getStatus().getCode() == 100) {
                    result.setGameStatus(GameStatus.COMPLETE);
                    result.add(entry.getHomeTeam().getId(), String.valueOf(entry.getHomeScore().getCurrent()));
                    result.add(entry.getAwayTeam().getId(), String.valueOf(entry.getAwayScore().getCurrent()));
                } else if (entry.getStatus().getCode() == 60) {
                    result.setGameStatus(GameStatus.CANCELLED);
                }
                break;
            }
        }
        return result;
    }

}