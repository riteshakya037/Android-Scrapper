package com.calebtrevino.tallystacker.presenters.mapper;

import android.widget.ArrayAdapter;
import com.calebtrevino.tallystacker.presenters.bases.BasePageAdapterMapper;

/**
 * @author Ritesh Shakya
 */
public interface DashMapper extends BasePageAdapterMapper {
    void registerSpinner(ArrayAdapter<String> mSpinnerAdapter);

    void initializeSpinnerListener();
}
