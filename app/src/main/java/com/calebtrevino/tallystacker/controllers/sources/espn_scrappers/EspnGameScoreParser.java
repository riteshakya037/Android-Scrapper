package com.calebtrevino.tallystacker.controllers.sources.espn_scrappers;

import com.calebtrevino.tallystacker.controllers.sources.espn_scrappers.exceptions.ExpectedElementNotFound;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.utils.StringUtils;
import com.crashlytics.android.Crashlytics;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.HashMap;

/**
 * @author Ritesh Shakya
 */

public class EspnGameScoreParser {

    private static HashMap<String, EspnGameScoreParser> gameList = new HashMap<>();
    private final Game game;
    private Document document;

    private EspnGameScoreParser(Game game) throws ExpectedElementNotFound {
        this.game = game;
        if (StringUtils.isNotNull(game.getGameUrl())) {
            this.init();
            this.getCurrentScore();
        }
    }

    private void init() {
        try {
            this.document = Jsoup.connect(game.getGameUrl())
                    .timeout(60 * 1000)
                    .maxBodySize(0)
                    .get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static EspnGameScoreParser getInstance(Game game) throws ExpectedElementNotFound {
        if (!gameList.containsKey(game.getGameUrl())) {
            gameList.put(game.getGameUrl(), new EspnGameScoreParser(game));
        }
        return gameList.get(game.getGameUrl());
    }

    public IntermediateResult getCurrentScore() throws ExpectedElementNotFound {
        Elements element = document.select("table#linescore>tbody>tr");
        IntermediateResult result = new IntermediateResult();
        for (int i = 0; i < element.size(); i++) {
            result.add(element.get(i).select("td.team-name").text(), element.get(i).select("td.final-score").text());
        }
        if (result.isEmpty()) {
            throw new ExpectedElementNotFound("Couldn't find any games to download.");
        }
        return result;
    }


    public static class IntermediateResult {
        HashMap<String, Integer> resultList = new HashMap<>();

        public void add(String teamAbbr, String teamScore) {
            try {
                resultList.put(teamAbbr, Integer.valueOf(teamScore));
            } catch (NumberFormatException e) {
                Crashlytics.logException(e);
                e.printStackTrace();
            }
        }

        public boolean isEmpty() {
            return resultList.isEmpty();
        }

        public int getTotal() {
            int totalScore = 0;
            for (Integer score : resultList.values()) {
                totalScore += score;
            }
            return totalScore;
        }

        public int getTeamScore(String teamAbbr) {
            if (resultList.containsKey(teamAbbr))
                return resultList.get(teamAbbr);
            else return 0;
        }

        @Override
        public String toString() {
            return "IntermediateResult{" +
                    "resultList=" + resultList +
                    '}';
        }
    }
}
