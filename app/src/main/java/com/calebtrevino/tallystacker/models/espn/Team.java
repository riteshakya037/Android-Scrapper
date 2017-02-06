package com.calebtrevino.tallystacker.models.espn;

import java.util.List;

public class Team {

    public Boolean isActive;
    public String uid;
    public String id;
    public String logo;
    public String shortDisplayName;
    public String location;
    public String name;
    public List<Link> links = null;
    public String displayName;
    public String abbreviation;

    @Override
    public String toString() {
        return "Team{" +
                ", location='" + location + '\'' +
                ", name='" + name + '\'' +
                ", displayName='" + displayName + '\'' +
                ", abbreviation='" + abbreviation + '\'' +
                '}';
    }
}
