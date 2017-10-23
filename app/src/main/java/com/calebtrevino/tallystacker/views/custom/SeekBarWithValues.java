package com.calebtrevino.tallystacker.views.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.calebtrevino.tallystacker.R;

/**
 * @author Ritesh Shakya
 */

public class SeekBarWithValues extends RelativeLayout {

    private TextView mMinText;
    private TextView mMaxText;
    private TextView mCurrentText;
    private SeekBar mSeek;

    public SeekBarWithValues(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(getContext()).inflate(R.layout.custom_seek_bar, this);
        // the minimum value is always 0
        mMinText = (TextView) findViewById(R.id.minValue);
        mMinText.setText("0");
        mMaxText = (TextView) findViewById(R.id.maxValue);
        mCurrentText = (TextView) findViewById(R.id.curentValue);
        mSeek = (SeekBar) findViewById(R.id.seekBar);
        mSeek.setMax(100);
        mMaxText.setText(String.valueOf(mSeek.getMax()));
        mSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateCurrentValue(progress);
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    /**
     * This needs additional work to make the current progress text stay
     * right under the thumb drawable.
     *
     * @param newProgress the new progress for which to place the text
     */
    private void updateCurrentValue(int newProgress) {
        mCurrentText.setText(String.valueOf(newProgress));
    }

    public SeekBar getSeekBar() {
        return mSeek;
    }

    public void updateSeekMaxValue(int newValue) {
        mMaxText.setText(String.valueOf(newValue));
        mSeek.setMax(newValue);
    }

    public int getProgress() {
        return mSeek.getProgress();
    }

    void setProgress(int progess) {
        mSeek.setProgress(progess);
    }

    @Override public void setEnabled(boolean enabled) {
        mSeek.setEnabled(enabled);
        mMinText.setEnabled(enabled);
        mMaxText.setEnabled(enabled);
        mCurrentText.setEnabled(enabled);
    }
}