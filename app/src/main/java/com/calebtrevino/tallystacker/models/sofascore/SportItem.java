
package com.calebtrevino.tallystacker.models.sofascore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SportItem {

    @SerializedName("sport")
    @Expose
    private Sport sport;
    @SerializedName("rows")
    @Expose
    private Integer rows;
    @SerializedName("tournaments")
    @Expose
    private List<Tournaments> tournaments = null;

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public List<Tournaments> getTournaments() {
        return tournaments;
    }

    public void setTournaments(List<Tournaments> tournaments) {
        this.tournaments = tournaments;
    }

    @Override
    public String toString() {
        return tournaments.toString();
    }
}
