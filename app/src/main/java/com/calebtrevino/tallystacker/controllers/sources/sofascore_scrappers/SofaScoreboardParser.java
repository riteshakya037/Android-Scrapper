package com.calebtrevino.tallystacker.controllers.sources.sofascore_scrappers;

import android.os.Environment;

import com.calebtrevino.tallystacker.controllers.sources.ScoreBoardParser;
import com.calebtrevino.tallystacker.controllers.sources.espn_scrappers.exceptions.ExpectedElementNotFound;
import com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.bases.League;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.sofascore.Event;
import com.calebtrevino.tallystacker.models.sofascore.SofaScoreJson;
import com.calebtrevino.tallystacker.utils.DateUtils;
import com.google.gson.Gson;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Ritesh Shakya
 */

public class SofaScoreboardParser extends ScoreBoardParser {

    private static Map<String, SofaScoreboardParser> leagueList = new HashMap<>();
    private League league;
    private Document document;
    private List<Event> gameStatusMap = new ArrayList<>();

    private SofaScoreboardParser(League league) throws ExpectedElementNotFound {
        this.league = league;
        if (league.hasSecondPhase()) {
            this.init();
            this.getGames();
        }
    }


    public static SofaScoreboardParser getInstance(League league) throws ExpectedElementNotFound {
        if (!contains(leagueList, league)) {
            leagueList.put(league.getAcronym(), new SofaScoreboardParser(league));
        }
        return leagueList.get(league.getAcronym());
    }


    private static boolean contains(Map<String, SofaScoreboardParser> leagueList, League leagueToCheck) {
        for (String league : leagueList.keySet()) {
            if (league.equals(leagueToCheck.getAcronym())) {
                return true;
            }
        }
        return false;
    }

    private void init() {
        try {
            document = Jsoup.connect(league.getBaseScoreUrl() + DateUtils.getDatePlus("yyyy-MM-dd", 0) + "/json")
                    .timeout(60 * 1000)
                    .maxBodySize(0)
                    .header("Accept", "text/javascript")
                    .ignoreContentType(true)
                    .get();
        } catch (IOException e) {
            throw new RuntimeException("Could not get the list of games for " + this.league.getName());
        }
    }


    private void getGames() throws ExpectedElementNotFound {
        SofaScoreJson espnJson = new Gson().fromJson(document.text(), SofaScoreJson.class);
        gameStatusMap.addAll(espnJson.getEvents());
        if (gameStatusMap.isEmpty()) {
            throw new ExpectedElementNotFound("Couldn't find any games to download.");
        }
    }

    @Override
    public void setGameUrl(Game game) {
        for (Event entry : gameStatusMap) {
            if ((entry.getAwayTeam().getId().equals(game.getFirstTeam().getAcronym()) && entry.getHomeTeam().getId().equals(game.getSecondTeam().getAcronym())) ||
                    (entry.getHomeTeam().getId().equals(game.getSecondTeam().getAcronym()) && entry.getAwayTeam().getId().equals(game.getSecondTeam().getAcronym()))
                    ) {
                game.setGameUrl(document.baseUri());
                game.setReqManual(false);
            } else {
                game.setReqManual(true);
            }
        }
    }

    public static void writeGames() {
        try {
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/Tallystacker/" + new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
            myDir.mkdirs();
            for (String leagueAcrn : leagueList.keySet()) {
                final File f = new File(myDir, leagueAcrn + ".txt");
                FileUtils.writeStringToFile(f, leagueList.get(leagueAcrn).gameStatusMap.toString(), "UTF-8");
            }
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
        }
    }
}