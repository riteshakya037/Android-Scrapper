package com.calebtrevino.tallystacker.models.espn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EspnJson {

    Data data;

    public Map<String, List<Competitor>> getTeams(String teamAbbr) {
        Map<String, List<Competitor>> listMap = new HashMap<>();
        for (Event event : data.getSport(teamAbbr).leagues.get(0).events) {
            listMap.put(event.toString(), event.getEventTeams());
        }
        return listMap;
    }

    public Map<Status, List<Competitor>> getStatus(String teamAbbr) {
        Map<Status, List<Competitor>> listMap = new HashMap<>();
        for (Event event : data.getSport(teamAbbr).leagues.get(0).events) {
            listMap.put(event.fullStatus, event.getEventTeams());
        }
        return listMap;
    }
}
