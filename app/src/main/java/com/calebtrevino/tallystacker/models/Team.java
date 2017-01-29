package com.calebtrevino.tallystacker.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.calebtrevino.tallystacker.controllers.factories.DefaultFactory;
import com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.bases.League;
import com.calebtrevino.tallystacker.models.base.BaseModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Ritesh Shakya
 */
public class Team extends BaseModel implements Parcelable {
    private long _id;
    private long teamID;
    private String City;
    private String Name;
    private String acronym;
    private League leagueType;

    public Team() {
    }

    private Team(Parcel in) {
        _id = in.readLong();
        teamID = in.readLong();
        City = in.readString();
        Name = in.readString();
        acronym = in.readString();
        leagueType = in.readParcelable(League.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(_id);
        dest.writeLong(teamID);
        dest.writeString(City);
        dest.writeString(Name);
        dest.writeString(acronym);
        dest.writeParcelable(leagueType, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Team> CREATOR = new Creator<Team>() {
        @Override
        public Team createFromParcel(Parcel in) {
            return new Team(in);
        }

        @Override
        public Team[] newArray(int size) {
            return new Team[size];
        }
    };

    public long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    @Override
    public void createID() {
        this._id = (long) hashCode();
    }

    public long get_teamID() {
        return teamID;
    }

    public void set_teamId(Long teamID) {
        this.teamID = teamID;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city.trim();
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name.trim().replaceAll(" ", "");
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym.trim();
    }

    public League getLeagueType() {
        return leagueType;
    }

    public void setLeagueType(League leagueType) {
        this.leagueType = leagueType;
    }

    @Override
    public String toJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", get_id());
            jsonObject.put("team_id", get_teamID());
            jsonObject.put("city", getCity());
            jsonObject.put("name", getName());
            jsonObject.put("acronym", getAcronym());
            jsonObject.put("league_type", getLeagueType().getPackageName());
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    @SuppressWarnings("unused")
    public static Team getFromJson(String jsonString) {
        Team team = DefaultFactory.Team.constructDefault();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            team.set_id(jsonObject.getLong("id"));
            team.set_teamId(jsonObject.getLong("team_id"));
            team.setCity(jsonObject.getString("city"));
            team.setName(jsonObject.getString("name"));
            team.setAcronym(jsonObject.getString("acronym"));
            team.setLeagueType((League) Class.forName(jsonObject.getString("league_type")).newInstance());
        } catch (JSONException | IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return team;
    }
}
