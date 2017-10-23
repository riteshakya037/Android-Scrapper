package com.calebtrevino.tallystacker.controllers.sources.sofascore_scrappers;

import android.util.Log;
import com.calebtrevino.tallystacker.controllers.sources.ScoreParser;
import com.calebtrevino.tallystacker.controllers.sources.espn_scrappers.exceptions.ExpectedElementNotFound;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.IntermediateResult;
import com.calebtrevino.tallystacker.models.enums.GameStatus;
import com.calebtrevino.tallystacker.models.sofascore.Event;
import com.calebtrevino.tallystacker.models.sofascore.GameScore;
import com.google.gson.Gson;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * @author Ritesh Shakya
 */

public class SofaGameScoreParser extends ScoreParser {

    private static final String TAG = SofaGameScoreParser.class.getSimpleName();
    private static final String LAST_UPDATE = "sofa_last_update";
    private Event entry;
    private Game game;

    public static SofaGameScoreParser getInstance() {
        return new SofaGameScoreParser();
    }

    private void init() {
        Log.i(TAG, "init: fetched");
        try {
            Document document = Jsoup.connect(game.getGameUrl())
                    .timeout(60 * 1000)
                    .maxBodySize(0)
                    .header("Accept", "text/javascript")
                    .ignoreContentType(true)
                    .get();
            entry = new Gson().fromJson(document.text(), GameScore.class).getEvent();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override public IntermediateResult getCurrentScore(Game game) throws ExpectedElementNotFound {
        this.game = game;
        if (game.getLeagueType().hasSecondPhase()) {
            this.init();
        }
        return game.getLeagueType().scrapeScoreBoard(this);
    }

    public IntermediateResult scrapeUsual() {
        IntermediateResult result = new IntermediateResult();
        Log.i(TAG, "checkGameCompletion: Team Match");
        // If the game status is completed.
        if (entry.getStatus().getCode() == 100) {
            result.setGameStatus(GameStatus.COMPLETE);
            result.add(entry.getHomeTeam().getId(),
                    String.valueOf(entry.getHomeScore().getCurrent()));
            result.add(entry.getAwayTeam().getId(),
                    String.valueOf(entry.getAwayScore().getCurrent()));
        } else if (entry.getStatus().getCode() == 60) {
            result.setGameStatus(GameStatus.CANCELLED);
        }
        return result;
    }
}
