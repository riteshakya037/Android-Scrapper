package com.calebtrevino.tallystacker.models.enums;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

import static com.calebtrevino.tallystacker.controllers.factories.DefaultFactory.Grid.GRID_MODE;

/**
 * @author Ritesh Shakya
 */

public enum GridMode implements Parcelable {
    TALLY_COUNT("Tally Count"), GROUPED("Grouped");

    public static final Creator<GridMode> CREATOR = new Creator<GridMode>() {
        @Override
        public GridMode createFromParcel(Parcel in) {
            return match(in.readString());
        }

        @Override
        public GridMode[] newArray(int size) {
            return new GridMode[size];
        }
    };
    private final String value;


    @SuppressWarnings("unused")
    GridMode(Parcel in) {
        value = in.readString();
    }

    GridMode(String value) {
        this.value = value;
    }

    public static GridMode match(String s) {
        for (GridMode gameStatus : Arrays.asList(GridMode.values())) {
            if (gameStatus.getValue().equals(s)) {
                return gameStatus;
            }
        }
        return GRID_MODE;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(value);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getValue() {
        return value;
    }
}
