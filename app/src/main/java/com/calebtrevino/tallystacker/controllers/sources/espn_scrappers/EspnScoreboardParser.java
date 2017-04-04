package com.calebtrevino.tallystacker.controllers.sources.espn_scrappers;

import android.os.Environment;

import com.calebtrevino.tallystacker.controllers.sources.ScoreBoardParser;
import com.calebtrevino.tallystacker.controllers.sources.espn_scrappers.exceptions.ExpectedElementNotFound;
import com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.bases.League;
import com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.bases.NCAA_BK;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.espn.Competitor;
import com.calebtrevino.tallystacker.models.espn.EspnJson;
import com.calebtrevino.tallystacker.utils.DateUtils;
import com.google.gson.Gson;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ritesh Shakya
 */

public class EspnScoreboardParser extends ScoreBoardParser {

    private static Map<String, EspnScoreboardParser> leagueList = new HashMap<>();
    private League league;
    private Document documentDefault;
    private Document document;
    private Document documentTomorrow;
    private Map<String, List<Competitor>> teamsList = new HashMap<>();

    private EspnScoreboardParser(League league) throws ExpectedElementNotFound {
        this.league = league;
        if (league.hasSecondPhase()) {
            this.init();
            this.getGames();
            if (league instanceof NCAA_BK) {
                this.getGames(100);
                this.getGames(56);
                this.getGames(55);
            }
            if (teamsList.isEmpty()) {
                throw new ExpectedElementNotFound("Couldn't find any games to download for " + league.getName());
            }
        }
    }


    public static EspnScoreboardParser getInstance(League league) throws ExpectedElementNotFound {
        if (!contains(leagueList, league)) {
            leagueList.put(league.getAcronym(), new EspnScoreboardParser(league));
        }
        return leagueList.get(league.getAcronym());
    }

    public static EspnScoreboardParser getObject(League league) throws ExpectedElementNotFound {
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

    public static void writeGames() {
        try {
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/Tallystacker/" + new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
            myDir.mkdirs();
            for (String leagueAcrn : leagueList.keySet()) {
                final File f = new File(myDir, leagueAcrn + ".txt");
                FileUtils.writeStringToFile(f, leagueList.get(leagueAcrn).teamsList.toString(), "UTF-8");
            }
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        try {
            this.documentDefault = Jsoup.connect(league.getBaseScoreUrl() + "/scoreboard/_/group/50/" + "date/" + DateUtils.getDatePlus("yyyyMMdd", -1))
                    .timeout(60 * 1000)
                    .maxBodySize(0)
                    .get();
            this.document = Jsoup.connect(league.getBaseScoreUrl() + "/scoreboard/_/group/50/" + "date/" + DateUtils.getDatePlus("yyyyMMdd", 0))
                    .timeout(60 * 1000)
                    .maxBodySize(0)
                    .get();
            this.documentTomorrow = Jsoup.connect(league.getBaseScoreUrl() + "/scoreboard/_/group/50/" + "date/" + DateUtils.getDatePlus("yyyyMMdd", 1))
                    .timeout(60 * 1000)
                    .maxBodySize(0)
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init(int i) {
        try {
            this.documentDefault = Jsoup.connect(league.getBaseScoreUrl() + "/scoreboard/_/group/" + i + "/date/" + DateUtils.getDatePlus("yyyyMMdd", -1))
                    .timeout(60 * 1000)
                    .maxBodySize(0)
                    .get();
            this.document = Jsoup.connect(league.getBaseScoreUrl() + "/scoreboard/_/group/" + i + "/date/" + DateUtils.getDatePlus("yyyyMMdd", 0))
                    .timeout(60 * 1000)
                    .maxBodySize(0)
                    .get();
            this.documentTomorrow = Jsoup.connect(league.getBaseScoreUrl() + "/scoreboard/_/group/" + i + "/date/" + DateUtils.getDatePlus("yyyyMMdd", 1))
                    .timeout(60 * 1000)
                    .maxBodySize(0)
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getGames() throws ExpectedElementNotFound {
        appendGames(documentDefault);
        appendGames(document);
        appendGames(documentTomorrow);
    }

    private void getGames(int i) {
        init(i);
        appendGames(documentDefault);
        appendGames(document);
        appendGames(documentTomorrow);
    }

    private void appendGames(Document document) {
        if (document != null) {
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
    }

    @Override
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
                game.setGameUrl(game.getLeagueType().getBaseScoreUrl() + game.getLeagueType().getScoreBoardURL() + "?gameId=" + entry.getKey());
                game.setReqManual(false);
            } else {
                game.setReqManual(true);
            }
        }
    }
}