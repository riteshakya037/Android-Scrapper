package com.calebtrevino.tallystacker.models.sofascore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Changes {

    @SerializedName("changes") @Expose private List<Object> changes = null;
    @SerializedName("changeTimestamp") @Expose private Integer changeTimestamp;
    @SerializedName("hasExpired") @Expose private Boolean hasExpired;
    @SerializedName("hasHomeChanges") @Expose private Boolean hasHomeChanges;
    @SerializedName("hasAwayChanges") @Expose private Boolean hasAwayChanges;

    public List<Object> getChanges() {
        return changes;
    }

    public void setChanges(List<Object> changes) {
        this.changes = changes;
    }

    public Integer getChangeTimestamp() {
        return changeTimestamp;
    }

    public void setChangeTimestamp(Integer changeTimestamp) {
        this.changeTimestamp = changeTimestamp;
    }

    public Boolean getHasExpired() {
        return hasExpired;
    }

    public void setHasExpired(Boolean hasExpired) {
        this.hasExpired = hasExpired;
    }

    public Boolean getHasHomeChanges() {
        return hasHomeChanges;
    }

    public void setHasHomeChanges(Boolean hasHomeChanges) {
        this.hasHomeChanges = hasHomeChanges;
    }

    public Boolean getHasAwayChanges() {
        return hasAwayChanges;
    }

    public void setHasAwayChanges(Boolean hasAwayChanges) {
        this.hasAwayChanges = hasAwayChanges;
    }
}
