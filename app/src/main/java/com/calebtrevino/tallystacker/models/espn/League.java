package com.calebtrevino.tallystacker.models.espn;

import java.util.List;

public class League {

    public List<Event> events = null;
    public String abbreviation;

    @Override public String toString() {
        return "League{" + "events=" + events + ", abbreviation='" + abbreviation + '\'' + '}';
    }
}
