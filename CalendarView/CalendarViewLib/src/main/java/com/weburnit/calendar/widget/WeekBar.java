package com.weburnit.calendar.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

import com.weburnit.calendar.manager.CalendarManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.util.LinkedList;

/**
 * Created by paulnguyen on 11/24/16.
 */

public class WeekBar extends RecyclerView {

    private int unitPositionInterval;
    private int currentPosition;
    private boolean forcedScroll;
    private CalendarManager manager;

    private LinkedList<DayView> linkedListDayView;

    public WeekBar(Context context) {
        super(context);
    }

    public WeekBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                updateScrollingBar();
                if (RecyclerView.SCROLL_STATE_IDLE == newState) {
                    if (!forcedScroll)
                        smoothScroll();
                    else {
                        broadcastScroller();
                    }
                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentPosition += dx;
            }
        });


        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);

        this.setLayoutManager(gridLayoutManager);
        this.setItemAnimator(new DefaultItemAnimator());
        linkedListDayView = new LinkedList<>();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getDefault().unregister(this);
    }

    private void broadcastScroller() {
        forcedScroll = false;
        if (listener != null) {
            int hideItems = currentPosition / unitPositionInterval;

//                            listener.onStopped();
        }
    }

    public WeekBar(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int maxSize = widthSize / 7;

        this.unitPositionInterval = maxSize;
    }

    protected void smoothScroll() {
        this.forcedScroll = true;
        if (currentPosition == 0) {
            return;
        }
        int overlap = currentPosition % unitPositionInterval;
        if (overlap > unitPositionInterval)
            this.smoothScrollBy(-(overlap - unitPositionInterval), 0);
        else {
            this.smoothScrollBy(-overlap, 0);
        }
        if (indicatorInterface != null) {
            int hideItems = currentPosition / unitPositionInterval;
            int dayIndex = hideItems + indicatorInterface.getCurrentPosition();
            indicatorInterface.onScreenItem(manager.getMinDate().plusDays(dayIndex), indicatorInterface.getCurrentPosition(), false);
        }
    }

    private IndicatorInterface indicatorInterface;

    private WeekBarListener listener;

    public void initBy(final CalendarManager manager, final IndicatorInterface indicatorInterface) {
        this.manager = manager;
        this.setIndicator(indicatorInterface);

        WeekAdapter adapter = new WeekAdapter(manager, new OnDateSelect() {
            @Override
            public void onDateSelected(LocalDate date) {
                int intervaleDay = Days.daysBetween(manager.getMinDate(), date).getDays();
                int hideItems = currentPosition / unitPositionInterval;
                if (indicatorInterface != null)
                    indicatorInterface.onScreenItem(date, intervaleDay - hideItems, false);
            }
        });
        this.setAdapter(adapter);
        this.post(new Runnable() {
            @Override
            public void run() {
                int intervaleDay = Days.daysBetween(manager.getMinDate(), LocalDate.now().withDayOfWeek(1)).getDays();
                forcedScroll = true;
                smoothScrollBy(intervaleDay * unitPositionInterval, 0);
                int indicatorIndex = Days.daysBetween(LocalDate.now().withDayOfWeek(1), LocalDate.now()).getDays();
                indicatorInterface.onScreenItem(LocalDate.now(), indicatorIndex, true);
            }
        });
    }

    public void setListener(WeekBarListener listener) {
        this.listener = listener;
    }

    public WeekBar setIndicator(IndicatorInterface indicatorInterface) {
        this.indicatorInterface = indicatorInterface;
        return this;
    }

    public interface WeekBarListener {
        public void onStopped(LocalDate date);
    }

    public interface OnDateSelect {
        public void onDateSelected(LocalDate date);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void listenDayViewItem(DayView dayView) {
        this.linkedListDayView.add(dayView);
    }

    protected void updateScrollingBar() {
        int hideItems = currentPosition / unitPositionInterval;
        int dayIndex = hideItems + indicatorInterface.getCurrentPosition();
        DayView dayView = this.linkedListDayView.get(dayIndex);
        ((WeekAdapter) this.getAdapter()).setCurrentDayView(dayView);
    }
}
