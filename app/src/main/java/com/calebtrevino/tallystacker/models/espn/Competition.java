
package com.calebtrevino.tallystacker.models.espn;

import java.util.ArrayList;
import java.util.List;

public class Competition {

    public String uid;
    public String startDate;
    public Boolean neutralSite;
    public Status status;
    public Long attendance;
    public List<Competitor> competitors = null;
    public Boolean conferenceCompetition;
    public String date;
    public Venue venue;
    public String id;
    public List<GeoBroadcast> geoBroadcasts = null;
    public Boolean timeValid;
    public List<Note> notes = null;
    public List<Broadcast> broadcasts = null;

    public List<Competitor> getCompititors() {
        ArrayList<Competitor> list = new ArrayList<>();
        for (Competitor competitor : competitors) {
            list.add(competitor);
        }
        return list;
    }

    @Override
    public String toString() {
        return competitors.toString();
    }
}
