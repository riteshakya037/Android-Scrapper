package com.calebtrevino.tallystacker.presenters.bases;

import android.os.Parcelable;

/**
 * @author Ritesh Shakya
 */
public interface BasePositionStateMapper {

    Parcelable getPositionState();

    void setPositionState(Parcelable state);
}
