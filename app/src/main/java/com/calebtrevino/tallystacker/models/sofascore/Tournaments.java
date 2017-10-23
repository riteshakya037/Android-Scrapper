package com.calebtrevino.tallystacker.models.sofascore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Tournaments {

    @SerializedName("tournament") @Expose private Tournament tournament;
    @SerializedName("category") @Expose private Category category;
    @SerializedName("season") @Expose private Season season;
    @SerializedName("hasEventPlayerStatistics") @Expose private Boolean hasEventPlayerStatistics;
    @SerializedName("hasEventPlayerHeatMap") @Expose private Boolean hasEventPlayerHeatMap;
    @SerializedName("events") @Expose private List<Event> events = null;

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public Boolean getHasEventPlayerStatistics() {
        return hasEventPlayerStatistics;
    }

    public void setHasEventPlayerStatistics(Boolean hasEventPlayerStatistics) {
        this.hasEventPlayerStatistics = hasEventPlayerStatistics;
    }

    public Boolean getHasEventPlayerHeatMap() {
        return hasEventPlayerHeatMap;
    }

    public void setHasEventPlayerHeatMap(Boolean hasEventPlayerHeatMap) {
        this.hasEventPlayerHeatMap = hasEventPlayerHeatMap;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    @Override public String toString() {
        return events.toString();
    }
}
