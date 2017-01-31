package com.calebtrevino.tallystacker.controllers.sources.espn_scrappers;

import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.espn.EspnJson;
import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ritesh Shakya
 */

public class EspnBaseScrapper {

    private Game game;

    public EspnBaseScrapper(Game game) {
        this.game = game;
    }

    public String getGameUrl() throws IOException {
        Document doc = Jsoup.connect(game.getLeagueType().getEspnUrl() + "/scoreboard?date=" + getCurrentDate())
                .get();
        Elements scriptElements = doc.getElementsByTag("script");
        Pattern pattern = Pattern.compile("window.espn.scoreboardData[\\s\t]*= (.*);.*window.espn.scoreboardSettings.*");
        for (Element element : scriptElements) {
            for (DataNode node : element.dataNodes()) {
                if (node.getWholeData().startsWith("window.espn.scoreboardData")) {
                    Matcher matcher = pattern.matcher(node.getWholeData());
                    if (matcher.matches()) {
                        EspnJson espnJson = new Gson().fromJson(matcher.group(1), EspnJson.class);
                        return getUrl(espnJson.getTeams());
                    }
                }
            }
        }
        return "";
    }

    private String getUrl(Map<String, List<String>> teams) {
        for (Map.Entry<String, List<String>> entry : teams.entrySet()) {
            System.out.println(entry);
            boolean firstTeam = false, secondTeam = false;
            for (String teamCity : entry.getValue()) {
                if (teamCity.equals(game.getFirstTeam().getCity())) {
                    firstTeam = true;
                } else if (teamCity.equals(game.getSecondTeam().getCity())) {
                    secondTeam = true;
                }
            }
            if (firstTeam && secondTeam)
                return game.getLeagueType().getEspnUrl() + "game?gameId=" + entry.getKey();
        }
        return "";
    }

    private String getCurrentDate() {
        return "";
    }

    public boolean checkGameStatus() throws IOException {
        Document parsedDocument = Jsoup.connect(game.getGameUrl()).timeout(60 * 1000).get();
        Elements element = parsedDocument.select("div#gamepackage-linescore");
        System.out.println(element);
        return false;
    }
}
