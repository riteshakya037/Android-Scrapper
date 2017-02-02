package com.calebtrevino.tallystacker.utils;

import android.content.Context;

import com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.bases.LeagueBase;
import com.calebtrevino.tallystacker.models.Game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ritesh Shakya
 */
public class TeamPreference {
    private static Map<LeagueBase, TeamPreference> instanceMap = new HashMap<>();
    private List<TeamsWrapper> teamList;

    private TeamPreference(Context context, LeagueBase leagueBase) throws IOException {
        teamList = new ArrayList<>();
        String line;
        try (InputStream inputStream = context.getResources().openRawResource(leagueBase.getTeamResource());
             InputStreamReader isr = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
             BufferedReader br = new BufferedReader(isr)) {
            while ((line = br.readLine()) != null) {
                String[] lineMap = line.split(",");
                if (lineMap.length == 4)
                    teamList.add(new TeamPreference.TeamsWrapper(lineMap[0], lineMap[1], lineMap[2], lineMap[3]));
            }
        }
    }

    public static TeamPreference getInstance(Context context, LeagueBase leagueBase) throws IOException {
        if (!instanceMap.containsKey(leagueBase)) {
            instanceMap.put(leagueBase, new TeamPreference(context, leagueBase));
        }
        return instanceMap.get(leagueBase);
    }

    public void updateTeamInfo(Game game) {
        TeamsWrapper firstTeam = new TeamsWrapper(game.getFirstTeam().getCity());
        if (teamList.contains(firstTeam)) {
            firstTeam = teamList.get(teamList.indexOf(firstTeam));
            game.getFirstTeam().setName(firstTeam.teamName);
            game.getFirstTeam().setCity(firstTeam.teamCity);
            game.getFirstTeam().setAcronym(firstTeam.teamAbbr);
        }
        TeamsWrapper secondTeam = new TeamsWrapper(game.getSecondTeam().getCity());
        if (teamList.contains(secondTeam)) {
            secondTeam = teamList.get(teamList.indexOf(secondTeam));
            game.getSecondTeam().setName(secondTeam.teamName);
            game.getSecondTeam().setCity(secondTeam.teamCity);
            game.getSecondTeam().setAcronym(secondTeam.teamAbbr);
        }
    }


    public static class TeamsWrapper {
        private String vegasDisplay;
        private String teamCity;
        private String teamName;
        private String teamAbbr;

        public TeamsWrapper(String vegasDisplay, String teamCity, String teamName, String teamAbbr) {
            this.vegasDisplay = StringUtils.isNotNull(vegasDisplay) ? vegasDisplay : teamCity;
            this.teamCity = teamCity;
            this.teamName = teamName;
            this.teamAbbr = teamAbbr;
        }

        public TeamsWrapper(String vegasDisplay) {
            this.vegasDisplay = vegasDisplay;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TeamsWrapper that = (TeamsWrapper) o;

            return vegasDisplay != null ? vegasDisplay.equals(that.vegasDisplay) : that.vegasDisplay == null;

        }

        @Override
        public int hashCode() {
            return vegasDisplay != null ? vegasDisplay.hashCode() : 0;
        }

        @Override
        public String toString() {
            return "TeamsWrapper{" +
                    "vegasDisplay='" + vegasDisplay + '\'' +
                    ", teamCity='" + teamCity + '\'' +
                    ", teamName='" + teamName + '\'' +
                    ", teamAbbr='" + teamAbbr + '\'' +
                    '}';
        }
    }
}
