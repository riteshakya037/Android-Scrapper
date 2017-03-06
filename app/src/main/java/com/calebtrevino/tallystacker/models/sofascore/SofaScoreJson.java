
package com.calebtrevino.tallystacker.models.sofascore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SofaScoreJson {

    @SerializedName("sportItem")
    @Expose
    private SportItem sportItem;
    @SerializedName("params")
    @Expose
    private Params params;
    @SerializedName("isShortDate")
    @Expose
    private Boolean isShortDate;

    public SportItem getSportItem() {
        return sportItem;
    }

    public void setSportItem(SportItem sportItem) {
        this.sportItem = sportItem;
    }

    public Params getParams() {
        return params;
    }

    public void setParams(Params params) {
        this.params = params;
    }

    public Boolean getIsShortDate() {
        return isShortDate;
    }

    public void setIsShortDate(Boolean isShortDate) {
        this.isShortDate = isShortDate;
    }


    @Override
    public String toString() {
        return sportItem.toString();
    }

    public void printTeams() {
        for (Tournaments tournaments : sportItem.getTournaments()) {
            for (Event event : tournaments.getEvents()) {
                System.out.println(event.getHomeTeam().getName() + " " + event.getHomeTeam().getId());
                System.out.println(event.getAwayTeam().getName() + " " + event.getAwayTeam().getId());
            }
        }
    }

    public List<Event> getEvents() {
        List<Event> events = new ArrayList<>();

        for (Tournaments tournaments : sportItem.getTournaments()) {
            for (Event event : tournaments.getEvents()) {
                events.add(event);
            }
        }
        return events;
    }
}
