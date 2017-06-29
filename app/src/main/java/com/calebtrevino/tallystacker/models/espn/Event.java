package com.calebtrevino.tallystacker.models.espn;

import java.util.List;

public class Event {

    public String id;
    public List<Competitor> competitors = null;
    public Status fullStatus;

    @Override public String toString() {
        return id;
    }

    protected List<Competitor> getEventTeams() {
        return competitors;
    }
}