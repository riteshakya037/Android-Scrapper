package com.calebtrevino.tallystacker.models.base;

/**
 * Created by fatal on 9/8/2016.
 */

public abstract class BaseModel {
    public abstract void createID();

    public abstract String toJSON();

    @Override
    public String toString() {
        return toJSON();
    }

}
