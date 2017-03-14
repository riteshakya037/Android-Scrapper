package com.calebtrevino.tallystacker.controllers.sources.espn_scrappers;

import android.util.Log;

import com.calebtrevino.tallystacker.controllers.sources.ScoreParser;
import com.calebtrevino.tallystacker.controllers.sources.espn_scrappers.exceptions.ExpectedElementNotFound;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.IntermediateResult;
import com.calebtrevino.tallystacker.models.enums.GameStatus;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ritesh Shakya
 */

public class EspnGameScoreParser extends ScoreParser {

    private static final String TAG = EspnGameScoreParser.class.getSimpleName();
    private Game game;
    private Document document;
    private Map<Status, List<Competitor>> gameStatusMap = new HashMap<>();

    private void init() {
        try {
            this.document = Jsoup.connect(game.getGameUrl())
                    .timeout(60 * 1000)
                    .maxBodySize(0)
                    .get();
            Log.i(TAG, "init: " + game.getFirstTeam().getName() + " " + game.getSecondTeam().getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean initScoreboard(IntermediateResult result) {
        try {
            Document scoreBoardDocument = Jsoup.connect(game.getLeagueType().getBaseScoreUrl() + "/scoreboard/_/group/50/")
                    .timeout(60 * 1000)
                    .maxBodySize(0)
                    .get();
            // Return false if game not found
            return scrapeScoreboard(scoreBoardDocument, result);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not get the list of games for " + this.game.getLeagueType().getName());
        }
    }

    private boolean initScoreboardYesterday(IntermediateResult result) {
        try {
            Document scoreBoardDocumentYesterday = Jsoup.connect(game.getLeagueType().getBaseScoreUrl() + "/scoreboard/_/group/50/" + "date/" + DateUtils.getDatePlus("yyyyMMdd", -1))
                    .timeout(60 * 1000)
                    .maxBodySize(0)
                    .get();
            // Return false if game not found
            return scrapeScoreboard(scoreBoardDocumentYesterday, result);
        } catch (IOException e) {
            Crashlytics.logException(e);
            e.printStackTrace();
            throw new RuntimeException("Could not get the list of games for " + this.game.getLeagueType().getName());
        }
    }


    public static EspnGameScoreParser getInstance() throws ExpectedElementNotFound {
        return new EspnGameScoreParser();
    }

    @Override
    public IntermediateResult getCurrentScore(Game game) throws ExpectedElementNotFound {
        this.game = game;
        if (game.getLeagueType().hasSecondPhase()) {
            this.init();
        }
        if (new DateTime(game.getGameDateTime(), DateTimeZone.getDefault()).plusMinutes(game.getLeagueType().getAvgTime()).isBeforeNow()) {
            IntermediateResult result = new IntermediateResult();
            checkGameCompletion(result);
            // Game is completed return else carry on with scraping game URL.
            if (result.getGameStatus() == GameStatus.COMPLETE || result.getGameStatus() == GameStatus.CANCELLED) {
                Log.i(TAG, "getCurrentScore: complete " + game.getFirstTeam().getName() + " " + game.getSecondTeam().getName());
                return result;
            }
        }

        return game.getLeagueType().scrapeScoreBoard(this);
    }

    private void checkGameCompletion(IntermediateResult result) {
        // search for games today if not found search in yesterdays date.
        boolean gameFound = initScoreboard(result);
        if (!gameFound) {
            initScoreboardYesterday(result);
        }
    }

    private boolean scrapeScoreboard(Document scoreBoardDocument, IntermediateResult result) {
        HashMap<Status, List<Competitor>> hashMap = new HashMap<>();
        appendGames(scoreBoardDocument, hashMap);
        gameStatusMap.putAll(hashMap);
        boolean foundGame = false;
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
                Log.i(TAG, "checkGameCompletion: Team Match");
                // If the game status is completed.
                foundGame = true;
                if (entry.getKey().type.completed) {
                    result.setGameStatus(GameStatus.COMPLETE);
                    for (Competitor competitor : entry.getValue()) {
                        result.add(competitor.getAbbreviation(), competitor.score);
                    }
                }
            }
        }
        return foundGame;
    }

    private void appendGames(Document scoreBoardDocument, HashMap<Status, List<Competitor>> hashMap) {
        Elements scriptElements = scoreBoardDocument.getElementsByTag("script");
        Pattern pattern = Pattern.compile("window.espn.scoreboardData[\\s\t]*= (.*);.*window.espn.scoreboardSettings.*");
        for (Element element : scriptElements) {
            for (DataNode node : element.dataNodes()) {
                if (node.getWholeData().startsWith("window.espn.scoreboardData")) {
                    Matcher matcher = pattern.matcher(node.getWholeData());
                    if (matcher.matches()) {
                        EspnJson espnJson = new Gson().fromJson(matcher.group(1), EspnJson.class);
                        hashMap.putAll(espnJson.getStatus());
                    }
                }
            }
        }
    }

    @Override
    public IntermediateResult scrapeUsual() throws ExpectedElementNotFound {
        // Scrape Game Url
        Elements element = document.select("table#linescore>tbody>tr");
        IntermediateResult result = new IntermediateResult();
        for (int i = 0; i < element.size(); i++) {
            result.add(element.get(i).select("td.team-name").text(), element.get(i).select("td.final-score").text());
        }
        if (result.isEmpty()) {
            throw new ExpectedElementNotFound("Couldn't find any games to download.");
        }
        result.setGameStatus(GameStatus.NEUTRAL);
        return result;
    }

    public IntermediateResult scrapeMLB() throws ExpectedElementNotFound {
        // Scrape Game Url
        Elements titleElement = document.select("table.linescore>tbody>tr.periods>td");
        int incNo = 0, runRow = 0;
        for (Element element : titleElement) {
            if (element.text().equals("R")) {
                runRow = incNo;
            }
            incNo++;
        }
        Elements element = document.select("table.linescore>tbody>tr");
        IntermediateResult result = new IntermediateResult();
        for (int i = 0; i < element.size(); i++) {
            if (StringUtils.isNotNull(element.get(i).select("td.team").text())) {
                result.add(element.get(i).select("td.team").text(), element.get(i).select("td").get(runRow).text());
            }
        }
        if (result.isEmpty()) {
            throw new ExpectedElementNotFound("Couldn't find any games to download.");
        }
        result.setGameStatus(GameStatus.NEUTRAL);
        return result;
    }
}
