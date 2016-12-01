package com.weburnit.calendar.widget;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.weburnit.calendar.R;
import com.weburnit.calendar.manager.CalendarManager;
import com.weburnit.calendar.manager.Day;
import com.weburnit.calendar.manager.Week;

import org.greenrobot.eventbus.EventBus;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

/**
 * Created by paulnguyen on 11/23/16.
 */

public class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.CellHolder> {

    private CalendarManager manager;
    private OnDateSelect mListener;
    private TextView currentWeekDay;

    public WeekAdapter(CalendarManager manager, OnDateSelect mListener) {
        this.manager = manager;
        this.mListener = mListener;
    }

    @Override
    public WeekAdapter.CellHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        WeekView view = (WeekView) LayoutInflater.from(parent.getContext()).inflate(R.layout.week_layout, parent, false);

        return new CellHolder(view);
    }

    @Override
    public void onBindViewHolder(WeekAdapter.CellHolder holder, int position) {
        Week week = this.getWeek(position);
        List<Day> days = week.getDays();
        for (int i = 0; i < 7; i++) {
            final Day day = days.get(i);
            final DayView dayView = (DayView) holder.getView().getChildAt(i);
            final TextView title = (TextView) dayView.findViewById(R.id.week_title);
            DateTimeFormatter formatter = DateTimeFormat.forPattern("E");
            title.setText(day.getDate().toString(formatter));

            final TextView weekDay = (TextView) dayView.findViewById(R.id.week_day);
            weekDay.setText(day.getText());

            EventBus.getDefault().post(dayView);
            if (day.isCurrent()) {
                title.setSelected(true);
                weekDay.setSelected(true);
                currentWeekDay = weekDay;
            }

            dayView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentWeekDay != null) {
                        currentWeekDay.setSelected(false);
                    }
                    currentWeekDay = weekDay;
                    LocalDate date = day.getDate();
                    weekDay.setSelected(true);
                    title.setSelected(true);
                    if (mListener != null) {
                        mListener.onDateSelected(date);
                    }
                }
            });
        }
    }

    public void setCurrentDayView(DayView dayView) {
        if (currentWeekDay != null) {
            currentWeekDay.setSelected(false);
            final TextView weekDay = (TextView) dayView.findViewById(R.id.week_day);
            currentWeekDay = weekDay;
            weekDay.setSelected(true);
        }
    }

    private Week getWeek(int position) {
        return manager.getWeeks().get(position);
    }

    @Override
    public int getItemCount() {
        return manager.getWeeks().size();
    }

    public class CellHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private WeekView view;
        private Resources resources;

        public CellHolder(WeekView itemView) {
            super(itemView);
            this.view = itemView;
            resources = itemView.getResources();
        }

        public WeekView getView() {
            return view;
        }

        @Override
        public void onClick(View view) {

        }
    }

    public interface OnDateSelect {
        public void onDateSelected(LocalDate date);
    }
}
