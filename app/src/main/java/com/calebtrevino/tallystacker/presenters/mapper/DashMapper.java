package com.calebtrevino.tallystacker.presenters.mapper;

import android.widget.ArrayAdapter;

import com.calebtrevino.tallystacker.presenters.bases.BasePositionStateMapper;
import com.calebtrevino.tallystacker.presenters.bases.BaseRecycleAdapterMapper;

/**
 * @author Ritesh Shakya
 */
public interface DashMapper extends BaseRecycleAdapterMapper, BasePositionStateMapper {
    void registerSpinner(ArrayAdapter<String> mSpinnerAdapter);

    void initializeSpinnerListener();
}
