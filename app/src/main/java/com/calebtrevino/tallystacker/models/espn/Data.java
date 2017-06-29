package com.calebtrevino.tallystacker.models.espn;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * @author Ritesh Shakya
 */

class Data {
    @SerializedName("sports") List<Sports> sports;

    public Sports getSport(String teamAbbr) {
        for (Sports sport : sports) {
            for (League league : sport.leagues)
                if (league.abbreviation.equalsIgnoreCase(teamAbbr)) return sport;
        }
        return null;
    }

    @Override public String toString() {
        return "Data{" + "sports=" + sports + '}';
    }
}
