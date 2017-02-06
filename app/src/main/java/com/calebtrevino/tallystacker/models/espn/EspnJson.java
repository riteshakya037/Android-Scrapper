
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

    public Map<String, List<String>> getTeams() {
        Map<String, List<String>> listMap = new HashMap<>();
        for (Event event : events) {
            listMap.put(event.toString(), event.getEventTeams());
            System.out.println(event.toString() + " " + event.competitions.get(0).toString());
        }
        return listMap;
    }
}
