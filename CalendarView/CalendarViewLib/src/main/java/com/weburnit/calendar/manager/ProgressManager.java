package com.weburnit.calendar.manager;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.weburnit.calendar.ResizableCalendarView;
import com.weburnit.calendar.models.AbstractViewHolder;
import com.weburnit.calendar.models.SizeViewHolder;

public abstract class ProgressManager {

    private static final String TAG = "ProgressManager";

    @NonNull
    protected ResizableCalendarView mCalendarView;

    protected RecyclerView mWeeksView;
    protected AbstractViewHolder[] mViews;

    protected SizeViewHolder mCalendarHolder;
    protected SizeViewHolder mWeeksHolder;

    final int mActiveIndex;

    private boolean mInitialized = false;

    final boolean mFromMonth;

    protected ProgressManager(@NonNull ResizableCalendarView calendarView, int activeWeek, boolean fromMonth) {
        mCalendarView = calendarView;
        mWeeksView = calendarView.getWeeksView();
        mActiveIndex = activeWeek;
        mFromMonth = fromMonth;
    }

    public void applyDelta(float delta) {
        float progress = getProgress(getDeltaInBounds(delta));
        apply(progress);
    }

    public void apply(float progress) {

        mCalendarHolder.animate(progress);
        mWeeksHolder.animate(progress);

        // animate views if necessary
        if (mViews != null) {
            for (AbstractViewHolder view : mViews) {
                view.animate(progress);
            }
        }

        // request layout
        mCalendarView.requestLayout();

    }

    public boolean isInitialized() {
        return mInitialized;
    }

    void setInitialized(boolean initialized) {
        mInitialized = initialized;
    }

    public int getCurrentHeight() {
        return mCalendarView.getLayoutParams().height - mCalendarHolder.getMinHeight();
    }

    public int getStartSize() {
        return 0;
    }

    public int getEndSize() {
        return mCalendarHolder.getHeight();
    }

    public abstract void finish(boolean expanded);

    public float getProgress(int distance) {
        return Math.max(0, Math.min(distance * 1f / mCalendarHolder.getHeight(), 1));
    }

    protected int getActiveIndex() {
        return mActiveIndex;
    }

    private int getDeltaInBounds(float delta) {

        if (mFromMonth) {
            return (int) Math.max(-mCalendarHolder.getHeight(), Math.min(0, delta)) + mCalendarHolder.getHeight();
        } else {
            return (int) Math.max(0, Math.min(mCalendarHolder.getHeight(), delta));
        }

    }

}
