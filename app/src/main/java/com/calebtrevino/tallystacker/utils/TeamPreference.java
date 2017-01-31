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
import java.util.Map;

/**
 * @author Ritesh Shakya
 */
public class TeamPreference {
    private static Map<LeagueBase, TeamPreference> instanceMap = new HashMap<>();
    private ArrayList<TeamsWrapper> teamList;

    private TeamPreference(Context context, LeagueBase leagueBase) throws IOException {
        teamList = new ArrayList<>();
        String line;
        try (InputStream inputStream = context.getResources().openRawResource(leagueBase.getTeamResource());
             InputStreamReader isr = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
             BufferedReader br = new BufferedReader(isr)) {
            while ((line = br.readLine()) != null) {
                String[] lineMap = line.split(",");
                if (lineMap.length == 3)
                    teamList.add(new TeamPreference.TeamsWrapper(lineMap[0], lineMap[1], lineMap[2]));
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
        if (teamList.contains(new TeamsWrapper(game.getFirstTeam().getCity()))) {
            game.getFirstTeam().setName(teamList.get(teamList.indexOf(new TeamsWrapper(game.getFirstTeam().getCity()))).teamName);
            game.getFirstTeam().setCity(teamList.get(teamList.indexOf(new TeamsWrapper(game.getFirstTeam().getCity()))).teamCity);
        }
        if (teamList.contains(new TeamsWrapper(game.getSecondTeam().getCity()))) {
            game.getSecondTeam().setName(teamList.get(teamList.indexOf(new TeamsWrapper(game.getSecondTeam().getCity()))).teamName);
            game.getSecondTeam().setCity(teamList.get(teamList.indexOf(new TeamsWrapper(game.getSecondTeam().getCity()))).teamCity);
        }
    }


    public static class TeamsWrapper {
        private String vegasDisplay;
        private String teamCity;
        private String teamName;

        public TeamsWrapper(String vegasDisplay, String teamCity, String teamName) {
            this.vegasDisplay = StringUtils.isNotNull(vegasDisplay) ? vegasDisplay : teamCity;
            this.teamCity = teamCity;
            this.teamName = teamName;
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
                    '}';
        }
    }
}
