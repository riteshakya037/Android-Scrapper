package com.calebtrevino.tallystacker;

import com.calebtrevino.tallystacker.controllers.factories.DefaultFactory;
import com.calebtrevino.tallystacker.controllers.sources.espn_scrappers.exceptions.ExpectedElementNotFound;
import com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.Soccer_Spread;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.Team;
import com.calebtrevino.tallystacker.utils.StringUtils;
import com.calebtrevino.tallystacker.utils.TeamPreference;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

/**
 * @author Ritesh Shakya
 */

public class SoccerSiteAvailabilityTest {

    private List<TeamPreference.TeamsWrapper> teamList;
    private String today = null;

    @Test public void testGameAvailability() throws ExpectedElementNotFound, IOException {
        List<Game> updatedGameList = new LinkedList<>();
        updateSoccerList();
        Document parsedDocument =
                Jsoup.connect("http://www.vegasinsider.com/soccer/odds/las-vegas/spread/")
                        .timeout(60 * 1000)
                        .get();

        updatedGameList = scrapeUpdateGamesFromParsedDocument(updatedGameList, parsedDocument);
        syncDateWithEspn(updatedGameList);
    }

    private void updateSoccerList() {
        teamList = new ArrayList<>();
        String line;
        try (InputStream inputStream = new FileInputStream(
                "D:\\Projects\\AndroidStudioProjects\\TallyStacker\\app\\src\\main\\res\\raw\\soccer_teams.txt");
             InputStreamReader isr = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
             BufferedReader br = new BufferedReader(isr)) {
            while ((line = br.readLine()) != null) {
                String[] lineMap = line.split(",");
                if (lineMap.length == 4) {
                    teamList.add(new TeamPreference.TeamsWrapper(lineMap[0], lineMap[1], lineMap[2],
                            lineMap[3]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void syncDateWithEspn(List<Game> updatedGameList)
            throws IOException, ExpectedElementNotFound {
        for (Game game : updatedGameList) {
            updateTeamInfo(game);
            if (game.getFirstTeam().getAcronym().equals(DefaultFactory.Team.ACRONYM)) {
                System.out.println(
                        game.getFirstTeam().getCity() + " " + game.getSecondTeam().getAcronym());
            }
            if (game.getSecondTeam().getAcronym().equals(DefaultFactory.Team.ACRONYM)) {
                System.out.println(
                        game.getSecondTeam().getCity() + " " + game.getFirstTeam().getAcronym());
            }
        }
    }

    public void updateTeamInfo(Game game) {
        TeamPreference.TeamsWrapper firstTeam =
                new TeamPreference.TeamsWrapper(game.getFirstTeam().getCity());
        if (teamList.contains(firstTeam) && StringUtils.isNull(firstTeam.getTeamAbbr())) {
            firstTeam = teamList.get(teamList.indexOf(firstTeam));
            game.getFirstTeam().setName(firstTeam.getTeamName());
            game.getFirstTeam().setCity(firstTeam.getTeamCity());
            game.getFirstTeam().setAcronym(firstTeam.getTeamAbbr());
        }
        TeamPreference.TeamsWrapper secondTeam =
                new TeamPreference.TeamsWrapper(game.getSecondTeam().getCity());
        if (teamList.contains(secondTeam) && StringUtils.isNull(secondTeam.getTeamAbbr())) {
            secondTeam = teamList.get(teamList.indexOf(secondTeam));
            game.getSecondTeam().setName(secondTeam.getTeamName());
            game.getSecondTeam().setCity(secondTeam.getTeamCity());
            game.getSecondTeam().setAcronym(secondTeam.getTeamAbbr());
        }
    }

    private List<Game> scrapeUpdateGamesFromParsedDocument(List<Game> updatedGameList,
            Document parsedDocument) {
        Elements updatedHtmlBlocks =
                parsedDocument.select("table.frodds-data-tbl > tbody>tr:has(td:not(.game-notes))");
        int VICount = 0;
        for (Element currentHtmlBlock : updatedHtmlBlocks) {
            Game currentGame = constructGameFromHtmlBlock(currentHtmlBlock);
            currentGame.setVIRow(VICount++);
            updatedGameList.add(currentGame);
        }

        return updatedGameList;
    }

    private Game constructGameFromHtmlBlock(Element currentHtmlBlock) {
        Game gameFromHtmlBlock = DefaultFactory.Game.constructDefault();
        gameFromHtmlBlock.setLeagueType(new Soccer_Spread());
        Elements updatedHtmlBlocks = currentHtmlBlock.select("td");
        boolean once = true;
        int position = 0;
        for (Element currentColumnBlock : updatedHtmlBlocks) {
            if (once) {
                once = false;
                createGameInfo(
                        Jsoup.parse(currentColumnBlock.html().replaceAll("(?i)<br[^>]*>", "br2n"))
                                .text(), gameFromHtmlBlock);
            }
            position++;
        }
        gameFromHtmlBlock.setVIBid();
        gameFromHtmlBlock.createID();
        return gameFromHtmlBlock;
    }

    public void createGameInfo(String bodyText, Game gameFromHtmlBlock) {
        // Header: 09/08 8:30 PM 451 Carolina 452 Denver
        Pattern pattern = Pattern.compile("([0-9]{2}/[0-9]{2}" + // Date of match
                "\\s" + "[0-9]{1,2}:[0-9]{2}" + "\\s" + "[A|P]M)" + // Time of match
                "br2n " + "([0-9]{6})" + // First team code
                ".?(\\w.*)br2n " + // First team city
                "([0-9]{6})" + // Second team code
                ".?(\\w.*)br2n " +// Second team city
                "([0-9]{6})" + ".?Drawbr2n " + "([0-9]{6})" + ".?Totalbr2n");
        setGameInfo(bodyText, gameFromHtmlBlock, pattern);
    }

    protected void setGameInfo(String bodyText, Game gameFromHtmlBlock, Pattern pattern) {
        Matcher m = pattern.matcher(bodyText);
        if (m.matches()) {
            // Initialize gameFromHtmlBlock
            if (today == null || today.equals(m.group(1).substring(0, 5))) {
                today = m.group(1).substring(0, 5);
                gameFromHtmlBlock.setGameDateTime(1);
            } else {
                gameFromHtmlBlock.setGameDateTime(0);
            }

            Team firstTeam = DefaultFactory.Team.constructDefault();
            firstTeam.setLeagueType(new Soccer_Spread());
            firstTeam.setTeamId(Long.valueOf(m.group(2)));
            firstTeam.setCity(m.group(3));
            firstTeam.createID();
            gameFromHtmlBlock.setFirstTeam(firstTeam);

            Team secondTeam = DefaultFactory.Team.constructDefault();
            secondTeam.setLeagueType(new Soccer_Spread());
            secondTeam.setTeamId(Long.valueOf(m.group(4)));
            secondTeam.setCity(m.group(5));
            secondTeam.createID();
            gameFromHtmlBlock.setSecondTeam(secondTeam);
        }
    }
}
