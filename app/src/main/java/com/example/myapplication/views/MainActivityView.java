package com.example.myapplication.views;


import com.example.myapplication.models.entity.Event;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.HashSet;
import java.util.List;

import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface MainActivityView extends MvpView {

    void updateColorOfCalendarDays();

    void invalidateCalendarViewDecorators(HashSet<CalendarDay> calendarDaysList);

    void showEventList(List<Event> eventList); // ListEventsTask заменить на показать updateRecyclerView
    // удалить DayEventsTask

    void updateDecorator(int color, CalendarDay day, float proportion);

    void showCurrentDate();
}
