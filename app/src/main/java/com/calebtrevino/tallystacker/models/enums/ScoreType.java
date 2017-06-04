package com.calebtrevino.tallystacker.models.enums;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

/**
 * @author Ritesh Shakya
 */
public enum ScoreType implements Parcelable {
    DEFAULT("default"), TOTAL("total"), SPREAD("spread");

    public static final Creator<ScoreType> CREATOR = new Creator<ScoreType>() {
        @Override
        public ScoreType createFromParcel(Parcel in) {
            return match(in.readString());
        }

        @Override
        public ScoreType[] newArray(int size) {
            return new ScoreType[size];
        }
    };
    private final String value;

    @SuppressWarnings("unused")
    ScoreType(Parcel in) {
        value = in.readString();
    }


    ScoreType(String value) {
        this.value = value;
    }

    public static ScoreType match(String s) {
        for (ScoreType scoreType : Arrays.asList(ScoreType.values())) {
            if (scoreType.getValue().equals(s)) {
                return scoreType;
            }
        }
        return ScoreType.DEFAULT;
    }

    public String getValue() {
        return value;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(value);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
