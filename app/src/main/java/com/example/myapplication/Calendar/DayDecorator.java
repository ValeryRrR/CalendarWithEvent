package com.example.myapplication.Calendar;

import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class DayDecorator implements DayViewDecorator {

    private final int color;
    private final CalendarDay today;
    private final float proportion;

    public DayDecorator(int color, CalendarDay today, float proportion) {
        this.color = color;
        this.today = today;
        this.proportion = proportion;
    }

    @Override
    public boolean shouldDecorate(CalendarDay calendarDay) {
        return calendarDay.equals(today);
    }

    @Override
    public void decorate(DayViewFacade dayViewFacade) {
        dayViewFacade.addSpan(new RelativeSizeSpan(proportion));
        dayViewFacade.addSpan(new ForegroundColorSpan(color));

    }
}
