package com.calebtrevino.tallystacker.presenters;

import java.io.Serializable;

/**
 * @author Ritesh Shakya
 */
public interface GridNameChangeListener extends Serializable {
    void nameChanged(String gridId, String gridName);
}
