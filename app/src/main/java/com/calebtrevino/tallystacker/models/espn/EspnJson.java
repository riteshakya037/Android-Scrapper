package com.calebtrevino.tallystacker.models.espn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EspnJson {

    public List<League> leagues = null;
    public Season season;
    public List<Event> events = null;
    public Week week;
    public Day day;

    public Map<String, List<Competitor>> getTeams() {
        Map<String, List<Competitor>> listMap = new HashMap<>();
        for (Event event : events) {
            listMap.put(event.toString(), event.getEventTeams());
        }
        return listMap;
    }

    public Map<Status, List<Competitor>> getStatus() {
        Map<Status, List<Competitor>> listMap = new HashMap<>();
        for (Event event : events) {
            listMap.put(event.status, event.getEventTeams());
        }
        return listMap;
    }
}
