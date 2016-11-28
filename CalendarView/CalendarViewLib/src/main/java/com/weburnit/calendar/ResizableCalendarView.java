package com.weburnit.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weburnit.calendar.manager.CalendarManager;
import com.weburnit.calendar.manager.Month;
import com.weburnit.calendar.manager.Week;
import com.weburnit.calendar.widget.IndicatorView;
import com.weburnit.calendar.widget.WeekBar;
import com.weburnit.calendar.widget.WeekView;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ResizableCalendarView extends LinearLayout implements View.OnClickListener {

    private static final String TAG = "ResizableCalendarView";

    @Nullable
    private CalendarManager mManager;

    @NonNull
    private TextView mTitleView;
    @NonNull
    private ImageButton mPrev;
    @NonNull
    private ImageButton mNext;
    @NonNull
    private WeekBar mWeeksView;

    @NonNull
    private LinearLayout mIndicatorView;

    @NonNull
    private final LayoutInflater mInflater;
    @NonNull
    private final RecycleBin mRecycleBin = new RecycleBin();

    @Nullable
    private OnDateSelect mListener;

    @NonNull
    private TextView mSelectionText;
    @NonNull
    private LinearLayout mHeader;

    /*Temporary close Resize for show only Week
     */
//    @NonNull private ResizeManager mResizeManager;

    private boolean initialized;

    public ResizableCalendarView(Context context) {
        this(context, null);
    }

    public ResizableCalendarView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.calendarViewStyle);
    }

    public ResizableCalendarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mInflater = LayoutInflater.from(context);

//        mResizeManager = new ResizeManager(this);

        inflate(context, R.layout.calendar_layout, this);

        setOrientation(VERTICAL);
    }

    public void init(@NonNull CalendarManager manager) {
        if (manager != null) {
            mManager = manager;
            mIndicatorView = (LinearLayout) findViewById(R.id.indicators);
            mWeeksView.initBy(mManager, (IndicatorView) mIndicatorView.getChildAt(0));
            populateLayout();
            if (mListener != null) {
                mListener.onDateSelected(mManager.getSelectedDay());
            }
        }
    }

    @Nullable
    public CalendarManager getManager() {
        return mManager;
    }

    @Override
    public void onClick(View v) {
        if (mManager != null) {
            int id = v.getId();
            if (id == R.id.prev) {
                if (mManager.prev()) {
                    populateLayout();
                }
            } else if (id == R.id.next) {
                if (mManager.next()) {
                    populateLayout();
                }
            }

        }
    }

    public void selectDate(LocalDate date) {
        mManager.selectDay(date);
        populateLayout();
    }

    @Override
    protected void dispatchDraw(@NonNull Canvas canvas) {
//        mResizeManager.onDraw();

        super.dispatchDraw(canvas);
    }

    @Nullable
    public CalendarManager.State getState() {
        if (mManager != null) {
            return mManager.getState();
        } else {
            return null;
        }
    }

    public void setListener(@Nullable OnDateSelect listener) {
        mListener = listener;
    }

    /**
     * @deprecated This will be removed
     */
    public void setTitle(@Nullable String text) {
        if (StringUtils.isEmpty(text)) {
            mHeader.setVisibility(View.VISIBLE);
            mSelectionText.setVisibility(View.GONE);
        } else {
            mHeader.setVisibility(View.GONE);
            mSelectionText.setVisibility(View.VISIBLE);
            mSelectionText.setText(text);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mTitleView = (TextView) findViewById(R.id.title);
        mPrev = (ImageButton) findViewById(R.id.prev);
        mNext = (ImageButton) findViewById(R.id.next);
        mWeeksView = (WeekBar) findViewById(R.id.weeks);

        mWeeksView.setListener(new WeekBar.WeekBarListener() {
            @Override
            public void onStopped(LocalDate date) {

            }
        });

        mHeader = (LinearLayout) findViewById(R.id.header);
        mSelectionText = (TextView) findViewById(R.id.selection_title);

//        mPrev.setOnClickListener(this);
//        mNext.setOnClickListener(this);

        populateLayout();
    }

    public void populateLayout() {
        if (mManager != null) {
            mPrev.setEnabled(mManager.hasPrev());
            mNext.setEnabled(mManager.hasNext());

            mTitleView.setText(mManager.getHeaderText());
        }
    }

    private void populateMonthLayout(Month month) {
        List<Week> weeks = month.getWeeks();
        int cnt = weeks.size();
        for (int i = 0; i < cnt; i++) {
            WeekView weekView = getWeekView(i);
        }

        int childCnt = mWeeksView.getChildCount();
        if (cnt < childCnt) {
            for (int i = cnt; i < childCnt; i++) {
                cacheView(i);
            }
        }
    }

    @NonNull
    public RecyclerView getWeeksView() {
        return mWeeksView;
    }

    @NonNull
    private WeekView getWeekView(int index) {
        int cnt = mWeeksView.getChildCount();

        if (cnt < index + 1) {
            for (int i = cnt; i < index + 1; i++) {
                View view = getView();
                mWeeksView.addView(view);
            }
        }

        return (WeekView) mWeeksView.getChildAt(index);
    }

    private View getView() {
        View view = mRecycleBin.recycleView();
        if (view == null) {
            view = mInflater.inflate(R.layout.week_layout, this, false);
        } else {
            view.setVisibility(View.VISIBLE);
        }
        return view;
    }

    private void cacheView(int index) {
        View view = mWeeksView.getChildAt(index);
        if (view != null) {
            mWeeksView.removeViewAt(index);
            mRecycleBin.addView(view);
        }
    }

    public LocalDate getSelectedDate() {
        return mManager.getSelectedDay();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

//        mResizeManager.recycle();
    }

    private class RecycleBin {

        private final Queue<View> mViews = new LinkedList<>();

        @Nullable
        public View recycleView() {
            return mViews.poll();
        }

        public void addView(@NonNull View view) {
            mViews.add(view);
        }

    }

    public interface OnDateSelect {
        public void onDateSelected(LocalDate date);
    }

}
