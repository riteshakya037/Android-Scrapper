package com.calebtrevino.tallystacker.models.base;

/**
 * @author Ritesh Shakya
 */

@SuppressWarnings("unused")
public abstract class BaseModel {
    public abstract void createID();

    protected abstract String toJSON();

    @Override
    public String toString() {
        return toJSON();
    }

}
