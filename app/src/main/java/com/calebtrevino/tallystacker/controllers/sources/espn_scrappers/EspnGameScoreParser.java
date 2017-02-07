package com.calebtrevino.tallystacker.controllers.sources.espn_scrappers;

import com.calebtrevino.tallystacker.controllers.sources.espn_scrappers.exceptions.ExpectedElementNotFound;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.Team;
import com.calebtrevino.tallystacker.models.espn.Competitor;
import com.calebtrevino.tallystacker.models.espn.EspnJson;
import com.calebtrevino.tallystacker.models.espn.Status;
import com.calebtrevino.tallystacker.utils.DateUtils;
import com.calebtrevino.tallystacker.utils.StringUtils;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ritesh Shakya
 */

public class EspnGameScoreParser {

    private static HashMap<String, EspnGameScoreParser> gameList = new HashMap<>();
    private final Game game;
    private Document document;
    private Document scoreBoardDocument;

    private EspnGameScoreParser(Game game) throws ExpectedElementNotFound {
        this.game = game;
        if (StringUtils.isNotNull(game.getGameUrl())) {
            this.init();
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

    private void initScoreboard() {
        try {
            this.scoreBoardDocument = Jsoup.connect(game.getLeagueType().getEspnUrl() + "/scoreboard/_/group/50/date/" + DateUtils.getTodaysDate("yyyyMMdd"))
                    .timeout(60 * 1000)
                    .maxBodySize(0)
                    .get();
        } catch (Exception e) {
            throw new RuntimeException("Could not get the list of games for " + this.game.getLeagueType().getName());
        }
    }


    public static EspnGameScoreParser getInstance(Game game) throws ExpectedElementNotFound {
        if (!gameList.containsKey(game.getGameUrl())) {
            gameList.put(game.getGameUrl(), new EspnGameScoreParser(game));
        }
        return gameList.get(game.getGameUrl());
    }

    public IntermediateResult getCurrentScore() throws ExpectedElementNotFound {
        if (new DateTime(game.getGameDateTime(), DateTimeZone.getDefault()).plusMinutes(game.getLeagueType().getAvgTime()).isBeforeNow()) {
            IntermediateResult result = new IntermediateResult();
            checkGameCompletion(result);
            // Game is completed return else carry on with scraping game URL.
            if (result.isCompleted())
                return result;
        }

        // Scrape Game Url
        Elements element = document.select("table#linescore>tbody>tr");
        IntermediateResult result = new IntermediateResult();
        for (int i = 0; i < element.size(); i++) {
            result.add(element.get(i).select("td.team-name").text(), element.get(i).select("td.final-score").text());
        }
        if (result.isEmpty()) {
            throw new ExpectedElementNotFound("Couldn't find any games to download.");
        }
        result.setCompleted(false);
        return result;
    }

    private void checkGameCompletion(IntermediateResult result) {
        initScoreboard();
        Map<Status, List<Competitor>> gameStatusMap = scrapeScoreboard();
        for (Map.Entry<Status, List<Competitor>> entry : gameStatusMap.entrySet()) {
            boolean firstTeam = false, secondTeam = false;
            for (Competitor competitor : entry.getValue()) {
                if (competitor.getAbbreviation().equals(game.getFirstTeam().getAcronym())) {
                    firstTeam = true;
                } else if (competitor.getAbbreviation().equals(game.getSecondTeam().getAcronym())) {
                    secondTeam = true;
                }
            }
            // If both first team and second team is found
            if (firstTeam && secondTeam) {
                // If the game status is completed.
                if (entry.getKey().type.completed) {
                    System.out.println("finished");
                    result.setCompleted(true);
                    for (Competitor competitor : entry.getValue()) {
                        result.add(competitor.getAbbreviation(), competitor.score);
                    }
                }
            }
        }
    }

    private Map<Status, List<Competitor>> scrapeScoreboard() {
        Elements scriptElements = scoreBoardDocument.getElementsByTag("script");
        Pattern pattern = Pattern.compile("window.espn.scoreboardData[\\s\t]*= (.*);.*window.espn.scoreboardSettings.*");

        for (Element element : scriptElements) {
            for (DataNode node : element.dataNodes()) {
                if (node.getWholeData().startsWith("window.espn.scoreboardData")) {
                    Matcher matcher = pattern.matcher(node.getWholeData());
                    if (matcher.matches()) {
                        EspnJson espnJson = new Gson().fromJson(matcher.group(1), EspnJson.class);
                        return espnJson.getStatus();
                    }
                }
            }
        }
        scoreBoardDocument = null;
        return new HashMap<>();
    }


    public static class IntermediateResult {
        private HashMap<String, Integer> resultList = new HashMap<>();
        private boolean completed = false;

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

        public int getTeamScore(Team team) {
            if (resultList.containsKey(team.getAcronym()))
                return resultList.get(team.getAcronym());
            else return 0;
        }

        @Override
        public String toString() {
            return "IntermediateResult{" +
                    "resultList=" + resultList +
                    ", completed=" + completed +
                    '}';
        }

        public void setCompleted(boolean completed) {
            this.completed = completed;
        }

        public boolean isCompleted() {
            return completed;
        }
    }
}
