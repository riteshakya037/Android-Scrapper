package com.calebtrevino.tallystacker.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.calebtrevino.tallystacker.controllers.sources.League;

/**
 * Created by fatal on 9/3/2016.
 */
public class Team implements Parcelable {
    private Long _id;
    private String City;
    private String Name;
    private String acronym;
    private League leagueType;

    public Team() {
    }

    protected Team(Parcel in) {
        City = in.readString();
        Name = in.readString();
        acronym = in.readString();
        leagueType = in.readParcelable(League.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
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

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
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
        Name = name.trim();
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
    public String toString() {
        return "Team{" +
                "_id=" + _id +
                ", City='" + City + '\'' +
                ", Name='" + Name + '\'' +
                ", acronym='" + acronym + '\'' +
                ", leagueType=" + leagueType +
                '}';
    }
}
