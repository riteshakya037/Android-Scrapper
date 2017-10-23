package com.calebtrevino.tallystacker.models.sofascore;

import com.google.gson.annotations.SerializedName;

/**
 * @author Ritesh Shakya
 */

public class GameScore {
    @SerializedName("event") private Event event;

    public Event getEvent() {
        return event;
    }
}
