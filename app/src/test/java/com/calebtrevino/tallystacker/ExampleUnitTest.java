package com.calebtrevino.tallystacker;

import com.calebtrevino.tallystacker.controllers.factories.DefaultFactory;
import com.calebtrevino.tallystacker.controllers.sources.espn_scrappers.exceptions.ExpectedElementNotFound;
import com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.NFL_Spread;
import com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.Soccer_Total;
import com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.bases.League;
import com.calebtrevino.tallystacker.models.Bid;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.GridLeagues;
import com.calebtrevino.tallystacker.models.IntermediateResult;
import com.calebtrevino.tallystacker.models.database.DatabaseContract;
import com.calebtrevino.tallystacker.models.enums.BidCondition;
import com.calebtrevino.tallystacker.models.enums.GameStatus;
import com.calebtrevino.tallystacker.models.espn.EspnJson;
import com.calebtrevino.tallystacker.models.sofascore.GameScore;
import com.calebtrevino.tallystacker.models.sofascore.SofaScoreJson;
import com.calebtrevino.tallystacker.utils.ParseUtils;
import com.calebtrevino.tallystacker.utils.StringUtils;
import com.calebtrevino.tallystacker.utils.TeamPreference;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.tz.UTCProvider;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@SuppressWarnings("unused")
public class ExampleUnitTest {
    @Test
    public void teamInfo() {
        String bodyText = "09/04 1:10 PM 901 St. Louis 902 Cincinnati";
        Pattern pattern = Pattern.compile("([0-9]{2}/[0-9]{2})" + "\\s+" + "([0-9]{1,2}:[0-9]{2}" + "\\s+" + "[A|P]M)" + "\\s+" + "([0-9]{3})" + ".?(\\w.*)" + "([0-9]{3})" + ".?(\\w.*)");
        Matcher m = pattern.matcher(bodyText);
        if (m.matches()) {
            assertEquals("09/04", m.group(1));
            assertEquals("1:10 PM", m.group(2));
            assertEquals("901", m.group(3));
            assertEquals("St. Louis", m.group(4));
            assertEquals("902", m.group(5));
            assertEquals("Cincinnati", m.group(6));
        }
    }

    @Test
    public void bidInfo() {
        String text = "162½u-05 " +
                "-1 -05";
        Pattern pattern = Pattern.compile(".*(\\d+[\\p{N}]?)([uUoO]).*");
        Matcher m = pattern.matcher(text);
        if (m.matches()) {
            assertEquals("2½", m.group(1));
            assertEquals("UNDER", BidCondition.match(m.group(2)).name());
        }

    }

    @Test
    public void totalCheck() {
        String bodyText = "162½u-05 " +
                "-1 -05";
        assertEquals(true, bodyText.matches(".*[\\d]+.*?[oOuU][+|-]?[\\d]+.*"));
    }

    @Test
    public void dateParser() {
        System.out.println("date = " + new Date(ParseUtils.parseDate("09/08 8:30 PM")));
        assertEquals(true, true);
    }

    @Test
    public void packageTest() throws Exception {
        League league = new Soccer_Total();
        DateTimeZone.setProvider(new UTCProvider());

        for (Game game : league.pullGamesFromNetwork(null)) {
            System.out.println(game.getFirstTeam().getCity() + ", " + game.getSecondTeam().getCity());
        }
        assertEquals(true, league.hasSecondPhase());
    }
//    @Test
//    public void leagueStatusCheck() throws Exception {
//        EspnJson espnJson = new Gson().fromJson(Jsoup.connect("http://site.api.espn.com/apis/site/v2/sports/basketball/mens-college-basketball/scoreboard")
//                        .timeout(60 * 1000).ignoreContentType(true).execute().body()
//                , EspnJson.class);
//        espnJson.getTeams();
//    }

    @Test
    public void leagueStatusCheck() throws Exception {
        Document doc = Jsoup.connect("http://www.espn.com/college-football/scoreboard/_/group/80/date/20170830")
                .timeout(60 * 1000)
                .maxBodySize(0)
                .get();
        Elements scriptElements = doc.getElementsByTag("script");
        Pattern pattern = Pattern.compile("window.espn.scoreboardData[\\s\t]*= (.*);.*window.espn.scoreboardSettings.*");
        for (Element element : scriptElements) {
            for (DataNode node : element.dataNodes()) {
                if (node.getWholeData().startsWith("window.espn.scoreboardData")) {
                    Matcher matcher = pattern.matcher(node.getWholeData());
                    if (matcher.matches()) {
                        EspnJson espnJson = new Gson().fromJson(matcher.group(1), EspnJson.class);
                        System.out.println(espnJson.getTeams());
                        assertEquals(false, espnJson.getTeams().isEmpty());
                    }
                }
            }
        }
    }

    @Test
    public void sofaScoreCheck() throws Exception {
        Document doc = Jsoup.connect("http://www.sofascore.com/football//2017-08-19/json")
                .timeout(60 * 1000)
                .maxBodySize(0)
                .header("Accept", "text/javascript")
                .ignoreContentType(true)
                .get();
        SofaScoreJson espnJson = new Gson().fromJson(doc.text(), SofaScoreJson.class);
        espnJson.printTeams();
        assertEquals(false, espnJson.getEvents().isEmpty());
    }

    @Test
    public void sofaGameScoreCheck() throws Exception {
        Document doc = Jsoup.connect("http://www.sofascore.com/event/7224879/json")
                .timeout(60 * 1000)
                .maxBodySize(0)
                .header("Accept", "text/javascript")
                .ignoreContentType(true)
                .get();
        System.out.println("doc.text() = " + doc.text());
        GameScore gameScore = new Gson().fromJson(doc.text(), GameScore.class);
        System.out.println("event = " + gameScore.getEvent().getAwayTeam());
        System.out.println("event = " + gameScore.getEvent().getHomeTeam());
        assertEquals(false, false);
    }

    @Test
    public void gameStatusCheck() throws Exception {
        Document parsedDocument = Jsoup.connect("http://www.espn.com/wnba/boxscore?gameId=400927392").timeout(60 * 1000).get();
        // Scrape Game Url
        Elements titleElement = parsedDocument.select("table.linescore>tbody>tr.periods>td");
        int incNo = 0;
        int runRow = 0;
        for (Element element : titleElement) {
            if (element.text().equals("R")) {
                runRow = incNo;
            }
            incNo++;
        }
        Elements element = parsedDocument.select("table.linescore>tbody>tr");
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
        System.out.println(result);
        assertEquals(false, result.isEmpty());
    }

    @Test
    public void checkBidTest() throws Exception {
        Game game = DefaultFactory.Game.constructDefault();
        game.setLeagueType(new Soccer_Total());
        Bid bid1 = DefaultFactory.Bid.constructDefault();
        bid1.setBidAmount(2.5F);
        bid1.setVIColumn(true);
        bid1.setCondition(BidCondition.UNDER);
        bid1.setVigAmount(-2F);
        game.getBidList().add(bid1);
        game.setVIBid();
        assertEquals(true, DatabaseContract.DbHelper.checkBid(game));
    }

    @Test
    public void tryJsoup() throws Exception {
        Document parsedDocument = Jsoup.connect("http://www.espn.com/wnba/teams").timeout(60 * 1000).get();
        Elements elements = parsedDocument.select("div.mod-content>ul>li>div>b");
        for (Element element : elements) {
            String[] urlSplit = element.select("b>a").get(0).attr("href").split("/");
            System.out.println(element.text() + "," + urlSplit[urlSplit.length - 2].toUpperCase());
        }
        assertEquals(true, elements.size() >= 0);
    }

    @Test
    public void tryJsoupLooper() throws Exception {
        Document parsedDocument = Jsoup.connect("http://www.espn.com/college-football/teams").timeout(60 * 1000).get();
        Elements elements = parsedDocument.select("div.mod-content>ul>li>h5");
        ExecutorService pool = Executors.newFixedThreadPool(10);
        int count = 0;
        for (Element element : elements) {
            pool.execute(new AsyncCaller(element.text(), element.select("h5>a").get(0).attr("href")));
            count++;
        }
        pool.shutdown();
        while (!pool.isTerminated()) {
            Thread.sleep(100);
        }
        assertEquals(true, count >= 0);
    }

    @Test
    public void internalTeamTest() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource("nfl_teams.txt");
        File file = new File(resource.getPath());
        String line;
        Collection<TeamPreference.TeamsWrapper> teamList = new ArrayList<>();
        try (InputStream fis = new FileInputStream(file);
             InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
             BufferedReader br = new BufferedReader(isr)
        ) {
            while ((line = br.readLine()) != null) {
                String[] lineMap = line.split(",");
                teamList.add(new TeamPreference.TeamsWrapper(lineMap[0], lineMap[1], lineMap[2], lineMap[3]));
            }
        }
        assertEquals(true, teamList.contains(new TeamPreference.TeamsWrapper("New England")));
    }

    @Test
    public void jsonTest() {
        List<GridLeagues> bidList = new LinkedList<>();
        bidList.add(DefaultFactory.GridLeagues.constructDefault());
        bidList.add(DefaultFactory.GridLeagues.constructDefault());
        bidList.add(DefaultFactory.GridLeagues.constructDefault());
        bidList.add(DefaultFactory.GridLeagues.constructDefault());
        bidList = bidList.subList(0, 2);
        assertEquals(2, bidList.size());
    }

    @Test
    public void spaceTrim() {
        String s = " Philadelphia Tat";
        assertEquals("Philadelphia Tats", s.trim().replaceAll(" ", "") + "s");
    }

    @Test
    public void checkTime() {
        Date date = new Date(1478851200000L);
        System.out.println(date);
        assertEquals("Fri Nov 11 13:45:00 NPT 2016", date.toString());
    }

    @Test
    public void download() throws Exception {
        League league = new NFL_Spread();
        Document parsedDocument = Jsoup.connect(league.getBaseUrl()).timeout(60 * 1000).get();

        try {
            File myDir = new File("Tallystacker/" + new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
            //noinspection ResultOfMethodCallIgnored
            myDir.mkdirs();
            final File f = new File(myDir, league.getAcronym() + "-" + league.getScoreType() + ".html");
            FileUtils.writeStringToFile(f, parsedDocument.select("table.frodds-data-tbl").outerHtml(), "UTF-8");
            assertEquals(true, f.exists());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class AsyncCaller implements Runnable {

        private String teamCity;
        private String teamName;
        private String href;

        AsyncCaller(String teamCity, String href) {
            String[] urlSplit = href.split("/");
            this.teamName = urlSplit[urlSplit.length - 1].replace("-", " ").replace(teamCity.toLowerCase(), "").trim();
            this.teamCity = teamCity;
            this.href = href;
        }

        @Override
        public void run() {
            Document parsedDocument = null;
            try {
                parsedDocument = Jsoup.connect(href).timeout(600 * 1000).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert parsedDocument != null;
            Elements scriptElements = parsedDocument.getElementsByTag("script");

            Pattern pattern = Pattern.compile(".*value\":\"(.*)\"\\},\\{\"name.*");
            for (Element element : scriptElements) {
                for (DataNode node : element.dataNodes()) {
                    Matcher matcher = pattern.matcher(node.getWholeData().replaceAll("\n", ""));
                    if (matcher.matches()) {
                        System.out.println(teamCity + "," + StringUtils.capitalize(teamName) + "," + matcher.group(1).toUpperCase());
                    }
                }
            }
            Thread.currentThread().interrupt();
        }
    }
}