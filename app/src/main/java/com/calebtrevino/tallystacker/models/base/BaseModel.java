package com.calebtrevino.tallystacker.models.base;

/**
 * @author Ritesh Shakya
 */

public abstract class BaseModel {
    public abstract void createID();

    protected abstract String toJSON();

    @Override
    public String toString() {
        return toJSON();
    }

}
