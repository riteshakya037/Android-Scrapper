package com.calebtrevino.tallystacker.controllers.sources.espn_scrappers;

import com.calebtrevino.tallystacker.controllers.sources.espn_scrappers.exceptions.ExpectedElementNotFound;
import com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.bases.League;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.espn.EspnJson;
import com.calebtrevino.tallystacker.utils.StringUtils;
import com.google.gson.Gson;

import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ritesh Shakya
 */

public class EspnScoreboardParser {

    private static Map<String, EspnScoreboardParser> leagueList = new HashMap<>();
    private League league;
    private Document document;
    private Map<String, List<String>> teamsList = new HashMap<>();

    private EspnScoreboardParser(League league) throws ExpectedElementNotFound {
        this.league = league;
        if (StringUtils.isNotNull(league.getEspnUrl())) {
            this.init();
            this.getGames();
        }
    }


    public static EspnScoreboardParser getInstance(League league) throws ExpectedElementNotFound {
        if (!contains(leagueList, league)) {
            leagueList.put(league.getAcronym(), new EspnScoreboardParser(league));
        }
        return leagueList.get(league.getAcronym());
    }

    private static boolean contains(Map<String, EspnScoreboardParser> leagueList, League leagueToCheck) {
        for (String league : leagueList.keySet()) {
            if (league.equals(leagueToCheck.getAcronym())) {
                return true;
            }
        }
        return false;
    }

    private void init() {
        try {
            this.document = Jsoup.connect(league.getEspnUrl() + "/scoreboard/_/group/50/date/" + getTodaysDate())
                    .timeout(60 * 1000)
                    .maxBodySize(0)
                    .get();
        } catch (Exception e) {
            throw new RuntimeException("Could not get the list of games for " + this.league.getName());
        }
    }

    private String getTodaysDate() {
        return new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new DateTime().toDate());
    }

    private void getGames() throws ExpectedElementNotFound {
        Elements scriptElements = document.getElementsByTag("script");
        Pattern pattern = Pattern.compile("window.espn.scoreboardData[\\s\t]*= (.*);.*window.espn.scoreboardSettings.*");
        for (Element element : scriptElements) {
            for (DataNode node : element.dataNodes()) {
                if (node.getWholeData().startsWith("window.espn.scoreboardData")) {
                    Matcher matcher = pattern.matcher(node.getWholeData());
                    if (matcher.matches()) {
                        EspnJson espnJson = new Gson().fromJson(matcher.group(1), EspnJson.class);
                        teamsList = espnJson.getTeams();
                    }
                }
            }
        }
        if (teamsList.isEmpty()) {
            throw new ExpectedElementNotFound("Couldn't find any games to download.");
        }
    }

    public void setGameUrl(Game game) {
        for (Map.Entry<String, List<String>> entry : teamsList.entrySet()) {
            boolean firstTeam = false, secondTeam = false;
            for (String teamCity : entry.getValue()) {
                if (teamCity.equals(game.getFirstTeam().getAcronym())) {
                    firstTeam = true;
                } else if (teamCity.equals(game.getSecondTeam().getAcronym())) {
                    secondTeam = true;
                }
            }
            if (firstTeam && secondTeam)
                game.setGameUrl(game.getLeagueType().getEspnUrl() + "/game?gameId=" + entry.getKey());
        }
    }
}