package com.calebtrevino.tallystacker.views.adaptors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.Grid;
import com.calebtrevino.tallystacker.models.enums.GridMode;
import com.calebtrevino.tallystacker.utils.Constants;
import com.calebtrevino.tallystacker.views.activities.GridCalendarActivity;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.joda.time.DateTime;

public class GridCalendarAdapter
        extends RecyclerView.Adapter<GridCalendarAdapter.GridCalendarHolder> {
    private final GregorianCalendar mCalendar;
    private final Calendar mCalendarToday;
    private final Context mContext;
    private final int mMonth;
    private final int mYear;
    private final String[] mDays = { "S", "M", "T", "W", "T", "F", "S" };
    private final int[] mDaysInMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
    private List<String> mItems;
    private int mDaysShown;
    private int mDaysLastMonth;
    private int mDaysNextMonth;
    private HashMap<Long, ArrayList<Game>> listHashMap;
    private HashMap<Integer, Long> countMapping;

    @SuppressLint("UseSparseArrays")
    public GridCalendarAdapter(Context c, int month, int year, Grid currentGrid) {
        listHashMap = new HashMap<>();
        countMapping = new HashMap<>();
        mContext = c;
        mMonth = month;
        mYear = year;
        mCalendar = new GregorianCalendar(mYear, mMonth, 1);
        mCalendarToday = Calendar.getInstance();
        populateMonth();
        int count = 0;
        // Cycle through all the games in grid and create Map with date and game list.
        for (final Game game : currentGrid.getGameList()) {
            // Filter games that don't satisfy the grids configuration
            if ((currentGrid.getGridMode() == GridMode.TALLY_COUNT
                    && game.getGridCount() >= currentGrid.getGridTotalCount())
                    // For TALLY_COUNT check grid count before adding
                    // For Grouped just add them directly.
                    || currentGrid.getGridMode() == GridMode.GROUPED) {
                if (listHashMap.containsKey(game.getGameAddDate())) {
                    listHashMap.get(game.getGameAddDate()).add(game);
                } else {
                    // Implementing a vount map for use later on.
                    listHashMap.put(game.getGameAddDate(), new ArrayList<Game>() {{
                        add(game);
                    }});
                    countMapping.put(count++, game.getGameAddDate());
                }
            }
        }
    }

    private void populateMonth() {
        mItems = new LinkedList<>();
        for (String day : mDays) {
            mItems.add(day);
            mDaysShown++;
        }

        int firstDay = getDay(mCalendar.get(Calendar.DAY_OF_WEEK));
        int prevDay;
        if (mMonth == 0) {
            prevDay = daysInMonth(11) - firstDay + 1;
        } else {
            prevDay = daysInMonth(mMonth - 1) - firstDay + 1;
        }
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
        if (month == 1 && mCalendar.isLeapYear(mYear)) daysInMonth++;
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

    @Override public GridCalendarHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        GridCalendarHolder viewHolder;
        View v;
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grd_calendar_item, parent, false);
        viewHolder = new GridCalendarHolder(v);
        return viewHolder;
    }

    @Override public void onBindViewHolder(GridCalendarHolder holder, int position) {
        holder.textView.setText(mItems.get(position));
        int[] date = getDate(position);
        if (date != null) {
            if (date[1] != mMonth) {
                // previous or next month
                holder.textView.setTextColor(
                        ContextCompat.getColor(mContext, R.color.colorPrimary));
            } else {
                // current month
                holder.textView.setTextColor(
                        ContextCompat.getColor(mContext, android.R.color.primary_text_dark));
                if (isToday(date[0], date[1], date[2])) {
                    holder.textView.setBackground(
                            ContextCompat.getDrawable(mContext, R.drawable.circle));
                }
            }
            holder.findGamesOn(date, Integer.parseInt(mItems.get(position)));
        } else {
            holder.textView.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));
        }
        //        onDate(date, position, holder.textView);
    }

    @Override public long getItemId(int position) {
        return position;
    }

    @Override public int getItemCount() {
        return mItems.size();
    }

    class GridCalendarHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_view) protected TextView textView;

        private GridCalendarHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        }

        private void findGamesOn(final int[] date, final int day) {
            textView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    long dateLong = new DateTime(date[2], date[1] + 1, day, 0, 0,
                            Constants.DATE.VEGAS_TIME_ZONE).withTimeAtStartOfDay().getMillis();
                    if (listHashMap.containsKey(dateLong)) {
                        Intent intent = new Intent(mContext, GridCalendarActivity.class);
                        intent.putExtra(GridCalendarActivity.POSITION, dateLong);
                        intent.putExtra(GridCalendarActivity.DATA_MAP, listHashMap);
                        intent.putExtra(GridCalendarActivity.COUNT_MAP, countMapping);
                        mContext.startActivity(intent);
                    }
                }
            });
        }
    }
}