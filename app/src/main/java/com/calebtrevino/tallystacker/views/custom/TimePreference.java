package com.calebtrevino.tallystacker.views.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;
import com.calebtrevino.tallystacker.utils.StringUtils;

/**
 * @author Ritesh Shakya
 */

public class TimePreference extends DialogPreference {

    private int lastHour;
    private int lastMinute;
    private TimePicker picker;

    public TimePreference(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        setPositiveButtonText(ctx.getString(android.R.string.ok));
        setNegativeButtonText(ctx.getString(android.R.string.cancel));
    }

    @Override protected View onCreateDialogView() {
        picker = new TimePicker(getContext());
        return picker;
    }

    @SuppressWarnings("deprecation") @Override protected void onBindDialogView(View v) {
        super.onBindDialogView(v);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            picker.setCurrentHour(lastHour);
            picker.setCurrentMinute(lastMinute);
        } else {
            picker.setHour(lastHour);
            picker.setMinute(lastMinute);
        }
    }

    @SuppressWarnings("deprecation") @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (positiveResult) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                lastHour = picker.getCurrentHour();
                lastMinute = picker.getCurrentMinute();
            } else {
                lastHour = picker.getHour();
                lastMinute = picker.getMinute();
            }
            String time = String.valueOf(lastHour) + ":" + String.valueOf(lastMinute);
            setSummary();
            if (callChangeListener(time)) {
                persistString(time);
            }
        }
    }

    private void setSummary() {
        String summary =
                String.valueOf(lastHour > 12 ? lastHour - 12 : lastHour) + ":" + StringUtils.right(
                        StringUtils.repeat("0", 2) + String.valueOf(lastMinute), 2) + (lastHour > 12
                        ? " PM" : " AM");
        super.setSummary(summary);
    }

    @Override protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    @Override protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        String time;
        if (restoreValue) {
            if (defaultValue == null) {
                time = getPersistedString("00:00");
            } else {
                time = getPersistedString(defaultValue.toString());
            }
        } else {
            time = defaultValue.toString();
        }
        lastHour = StringUtils.getHour(time);
        lastMinute = StringUtils.getMinute(time);
        setSummary();
    }
}