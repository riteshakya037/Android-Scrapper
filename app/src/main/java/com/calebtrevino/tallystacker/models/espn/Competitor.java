
package com.calebtrevino.tallystacker.models.espn;


import java.util.List;

public class Competitor {

    public String uid;
    public String id;
    public Long order;
    public List<LineScore> linescores = null;
    public String score;
    public Boolean winner;
    public Team team;
    public List<Record> records = null;
    public String type;
    public List<Leader> leaders = null;
    public String homeAway;
    public List<Statistic> statistics = null;


    String getLocation() {
        return team.location;
    }
}
