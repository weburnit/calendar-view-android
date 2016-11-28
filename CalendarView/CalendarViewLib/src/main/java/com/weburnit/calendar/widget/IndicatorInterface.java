package com.weburnit.calendar.widget;

import org.joda.time.LocalDate;

/**
 * Created by paulnguyen on 11/26/16.
 */

public interface IndicatorInterface {
    public void onScreenItem(LocalDate date, int position, boolean forced);

    public int getCurrentPosition();
}
