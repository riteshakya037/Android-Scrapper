package com.calebtrevino.tallystacker.models.enums;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

/**
 * @author Ritesh Shakya
 */
public enum GameStatus implements Parcelable {
    COMPLETE("complete"), CANCELLED("cancelled"), NEUTRAL("null");

    private final String value;

    @SuppressWarnings("unused")
    GameStatus(Parcel in) {
        value = in.readString();
    }


    GameStatus(String value) {
        this.value = value;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(value);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GameStatus> CREATOR = new Creator<GameStatus>() {
        @Override
        public GameStatus createFromParcel(Parcel in) {
            return match(in.readString());
        }

        @Override
        public GameStatus[] newArray(int size) {
            return new GameStatus[size];
        }
    };

    public String getValue() {
        return value;
    }


    public static GameStatus match(String s) {
        for (GameStatus gameStatus : Arrays.asList(GameStatus.values())) {
            if (gameStatus.getValue().equals(s)) {
                return gameStatus;
            }
        }
        return GameStatus.NEUTRAL;
    }
}
