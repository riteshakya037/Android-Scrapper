
package com.calebtrevino.tallystacker.models.espn;

import java.util.ArrayList;
import java.util.List;

public class Event {

    public String uid;
    public String id;
    public List<Competition> competitions = null;
    public Season season;
    public Status status;
    public List<Link> links = null;
    public String date;

    @Override
    public String toString() {
        return id;
    }

    protected List<Competitor> getEventTeams() {
        ArrayList<Competitor> list = new ArrayList<>();
        for (Competition competition : competitions) {
            list.addAll(competition.getCompititors());
        }
        return list;
    }
}