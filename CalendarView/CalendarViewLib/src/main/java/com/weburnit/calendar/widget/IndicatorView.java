package com.weburnit.calendar.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import org.joda.time.LocalDate;


public class IndicatorView extends WeekView implements IndicatorInterface {

    private int position = 0;

    public IndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onScreenItem(LocalDate date, int position, boolean forced) {
        int childCount = this.getChildCount();
        if (this.position != position || forced) {
            for (int i = 0; i < childCount; i++) {
                TextView textView = (TextView) this.getChildAt(i);
                textView.setVisibility((i == position) ? View.VISIBLE : View.GONE);
            }
            this.position = position;
        }
    }

    @Override
    public int getCurrentPosition() {
        return this.position;
    }
}
