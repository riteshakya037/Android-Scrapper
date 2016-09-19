package com.calebtrevino.tallystacker.views.adaptors;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.calebtrevino.tallystacker.R;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GridCalendarAdapter extends RecyclerView.Adapter<GridCalendarAdapter.GridCalendarHolder> {
    private final GregorianCalendar mCalendar;
    private final Calendar mCalendarToday;
    private final Context mContext;
    private List<String> mItems;
    private final int mMonth;
    private final int mYear;
    private int mDaysShown;
    private int mDaysLastMonth;
    private int mDaysNextMonth;
    private final String[] mDays = {"S", "M", "T", "W", "T", "F", "S"};
    private final int[] mDaysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    public GridCalendarAdapter(Context c, int month, int year) {
        mContext = c;
        mMonth = month;
        mYear = year;
        mCalendar = new GregorianCalendar(mYear, mMonth, 1);
        mCalendarToday = Calendar.getInstance();
        populateMonth();
    }
//
//    /**
//     * @param date     - null if day title (0 - dd / 1 - mm / 2 - yy)
//     * @param position - position in item list
//     * @param item     - view for date
//     */
//    protected abstract void onDate(int[] date, int position, View item);

    private void populateMonth() {
        mItems = new LinkedList<>();
        for (String day : mDays) {
            mItems.add(day);
            mDaysShown++;
        }

        int firstDay = getDay(mCalendar.get(Calendar.DAY_OF_WEEK));
        int prevDay;
        if (mMonth == 0)
            prevDay = daysInMonth(11) - firstDay + 1;
        else
            prevDay = daysInMonth(mMonth - 1) - firstDay + 1;
        for (int i = 0; i < firstDay; i++) {
            mItems.add(String.valueOf(prevDay + i));
            mDaysLastMonth++;
            mDaysShown++;
        }

        int daysInMonth = daysInMonth(mMonth);
        for (int i = 1; i <= daysInMonth; i++) {
            mItems.add(String.valueOf(i));
            mDaysShown++;
        }

        mDaysNextMonth = 1;
        while (mDaysShown % 7 != 0) {
            mItems.add(String.valueOf(mDaysNextMonth));
            mDaysShown++;
            mDaysNextMonth++;
        }

    }

    private int daysInMonth(int month) {
        int daysInMonth = mDaysInMonth[month];
        if (month == 1 && mCalendar.isLeapYear(mYear))
            daysInMonth++;
        return daysInMonth;
    }


    private int getDay(int day) {
        switch (day) {
            case Calendar.SUNDAY:
                return 0;
            case Calendar.MONDAY:
                return 1;
            case Calendar.TUESDAY:
                return 2;
            case Calendar.WEDNESDAY:
                return 3;
            case Calendar.THURSDAY:
                return 4;
            case Calendar.FRIDAY:
                return 5;
            case Calendar.SATURDAY:
                return 6;

            default:
                return 0;
        }
    }

    private boolean isToday(int day, int month, int year) {
        return mCalendarToday.get(Calendar.MONTH) == month
                && mCalendarToday.get(Calendar.YEAR) == year
                && mCalendarToday.get(Calendar.DAY_OF_MONTH) == day;
    }

    private int[] getDate(int position) {
        int date[] = new int[3];
        if (position <= 6) {
            return null; // day names
        } else if (position <= mDaysLastMonth + 6) {
            // previous month
            date[0] = Integer.parseInt(mItems.get(position));
            if (mMonth == 0) {
                date[1] = 11;
                date[2] = mYear - 1;
            } else {
                date[1] = mMonth - 1;
                date[2] = mYear;
            }
        } else if (position <= mDaysShown - mDaysNextMonth) {
            // current month
            date[0] = position - (mDaysLastMonth + 6);
            date[1] = mMonth;
            date[2] = mYear;
        } else {
            // next month
            date[0] = Integer.parseInt(mItems.get(position));
            if (mMonth == 11) {
                date[1] = 0;
                date[2] = mYear + 1;
            } else {
                date[1] = mMonth + 1;
                date[2] = mYear;
            }
        }
        return date;
    }

    @Override
    public GridCalendarHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        GridCalendarHolder viewHolder;
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.grd_calendar_item, parent, false);
        viewHolder = new GridCalendarHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(GridCalendarHolder holder, int position) {
        holder.textView.setText(mItems.get(position));
        int[] date = getDate(position);
        if (date != null) {
            if (date[1] != mMonth) {
                // previous or next month
                holder.textView.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            } else {
                // current month
                holder.textView.setTextColor(mContext.getResources().getColor(android.R.color.primary_text_dark));
                if (isToday(date[0], date[1], date[2])) {
                    holder.textView.setBackground(mContext.getResources().getDrawable(R.drawable.circle));
                }
            }
        } else {
            holder.textView.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
        }

//        onDate(date, position, holder.textView);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class GridCalendarHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_view)
        TextView textView;

        public GridCalendarHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        }
    }
}