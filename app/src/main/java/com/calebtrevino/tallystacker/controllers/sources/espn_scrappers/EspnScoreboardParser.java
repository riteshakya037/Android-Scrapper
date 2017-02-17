package com.calebtrevino.tallystacker.controllers.sources.espn_scrappers;

import com.calebtrevino.tallystacker.controllers.sources.espn_scrappers.exceptions.ExpectedElementNotFound;
import com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.bases.League;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.espn.Competitor;
import com.calebtrevino.tallystacker.models.espn.EspnJson;
import com.calebtrevino.tallystacker.utils.DateUtils;
import com.calebtrevino.tallystacker.utils.StringUtils;
import com.google.gson.Gson;

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

public class EspnScoreboardParser {

    private static Map<String, EspnScoreboardParser> leagueList = new HashMap<>();
    private League league;
    private Document document;
    private Document documentTomorrow;
    private Map<String, List<Competitor>> teamsList = new HashMap<>();

    private EspnScoreboardParser(League league) throws Exception {
        this.league = league;
        if (StringUtils.isNotNull(league.getEspnUrl())) {
            this.init();
            this.getGames();
        }
    }


    public static EspnScoreboardParser getInstance(League league) throws Exception {
        if (!contains(leagueList, league)) {
            leagueList.put(league.getAcronym(), new EspnScoreboardParser(league));
        }
        return leagueList.get(league.getAcronym());
    }

    public static EspnScoreboardParser getObject(League league) throws Exception {
        return new EspnScoreboardParser(league);
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
            this.document = Jsoup.connect(league.getEspnUrl() + "/scoreboard/_/group/50/" + "date/" + DateUtils.getDatePlus("yyyyMMdd", 0))
                    .timeout(60 * 1000)
                    .maxBodySize(0)
                    .get();
            this.documentTomorrow = Jsoup.connect(league.getEspnUrl() + "/scoreboard/_/group/50/" + "date/" + DateUtils.getDatePlus("yyyyMMdd", 1))
                    .timeout(60 * 1000)
                    .maxBodySize(0)
                    .get();
        } catch (Exception e) {
            throw new RuntimeException("Could not get the list of games for " + this.league.getName());
        }
    }


    private void getGames() throws ExpectedElementNotFound {
        appendGames(document);
        appendGames(documentTomorrow);
        if (teamsList.isEmpty()) {
            throw new ExpectedElementNotFound("Couldn't find any games to download.");
        }
    }

    private void appendGames(Document document) {
        Elements scriptElements = document.getElementsByTag("script");
        Pattern pattern = Pattern.compile("window.espn.scoreboardData[\\s\t]*= (.*);.*window.espn.scoreboardSettings.*");
        for (Element element : scriptElements) {
            for (DataNode node : element.dataNodes()) {
                if (node.getWholeData().startsWith("window.espn.scoreboardData")) {
                    Matcher matcher = pattern.matcher(node.getWholeData());
                    if (matcher.matches()) {
                        EspnJson espnJson = new Gson().fromJson(matcher.group(1), EspnJson.class);
                        teamsList.putAll(espnJson.getTeams());
                    }
                }
            }
        }
    }

    public void setGameUrl(Game game) {
        for (Map.Entry<String, List<Competitor>> entry : teamsList.entrySet()) {
            boolean firstTeam = false, secondTeam = false;
            for (Competitor competitor : entry.getValue()) {
                if (competitor.getAbbreviation().equals(game.getFirstTeam().getAcronym())) {
                    firstTeam = true;
                } else if (competitor.getAbbreviation().equals(game.getSecondTeam().getAcronym())) {
                    secondTeam = true;
                }
            }
            if (firstTeam && secondTeam) {
                game.setGameUrl(game.getLeagueType().getEspnUrl() + "/game?gameId=" + entry.getKey());
                game.setReqManual(false);
            } else {
                game.setReqManual(true);
            }
        }
    }
}