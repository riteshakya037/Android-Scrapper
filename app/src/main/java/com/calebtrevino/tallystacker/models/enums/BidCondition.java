package com.calebtrevino.tallystacker.models.enums;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Arrays;

/**
 * @author Ritesh Shakya
 */
public enum BidCondition implements Parcelable {
    OVER("o"), UNDER("u"), DEFAULT("s"), SPREAD("spread");

    public static final Creator<BidCondition> CREATOR = new Creator<BidCondition>() {
        @Override public BidCondition createFromParcel(Parcel in) {
            return match(in.readString());
        }

        @Override public BidCondition[] newArray(int size) {
            return new BidCondition[size];
        }
    };
    private final String value;

    @SuppressWarnings("unused") BidCondition(Parcel in) {
        value = in.readString();
    }

    BidCondition(String value) {
        this.value = value;
    }

    public static BidCondition match(String s) {
        for (BidCondition scoreType : Arrays.asList(BidCondition.values())) {
            if (scoreType.getValue().equals(s)) {
                return scoreType;
            }
        }
        return BidCondition.DEFAULT;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(value);
    }

    @Override public int describeContents() {
        return 0;
    }

    public String getValue() {
        return value;
    }
}
