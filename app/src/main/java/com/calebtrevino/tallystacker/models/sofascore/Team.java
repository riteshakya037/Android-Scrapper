package com.calebtrevino.tallystacker.models.sofascore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Team {

    @SerializedName("id") @Expose private String id;
    @SerializedName("name") @Expose private String name;
    @SerializedName("slug") @Expose private String slug;
    @SerializedName("gender") @Expose private String gender;
    @SerializedName("subTeams") @Expose private List<Object> subTeams = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<Object> getSubTeams() {
        return subTeams;
    }

    public void setSubTeams(List<Object> subTeams) {
        this.subTeams = subTeams;
    }

    @Override public String toString() {
        return name;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Team team = (Team) o;

        return id != null ? id.equals(team.id)
                : team.id == null && (name != null ? name.equals(team.name) : team.name == null);
    }

    @Override public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
