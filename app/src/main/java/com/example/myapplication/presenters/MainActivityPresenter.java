package com.example.myapplication.presenters;

import android.os.AsyncTask;

import com.example.myapplication.models.database.App;
import com.example.myapplication.models.entity.Event;
import com.example.myapplication.views.MainActivityView;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.HashSet;
import java.util.List;

import moxy.InjectViewState;
import moxy.MvpPresenter;

@InjectViewState
public class MainActivityPresenter extends MvpPresenter<MainActivityView> {


    public void updateColorOfCalendar() {
        UpdateColorOfCalendarDaysTask updateColorOfCalendarDaysTask = new UpdateColorOfCalendarDaysTask();
        updateColorOfCalendarDaysTask.execute();
    }

    public void updateEventList() {
        UpdateListEventsTask updateListEventsTask = new UpdateListEventsTask();
        updateListEventsTask.execute();
    }

    public void showEventsOfChoisenDay(String day) {
        ShowEventsOfChoisenDayTask showEventsOfChoisenDayTask = new ShowEventsOfChoisenDayTask();
        showEventsOfChoisenDayTask.execute(day);
    }


    //R.color.colorDaysWithEvent, не закрашиваются цвета


    private class UpdateColorOfCalendarDaysTask extends AsyncTask<String, Void, HashSet<CalendarDay>> {

        @Override
        protected void onPostExecute(HashSet<CalendarDay> calendarDaysList) {
            if (!calendarDaysList.isEmpty()) {
                getViewState().invalidateCalendarViewDecorators(calendarDaysList);
            }
        }

        @Override
        protected HashSet<CalendarDay> doInBackground(String... currentMonth) {

            HashSet<CalendarDay> calendarDaysList = new HashSet<>();
            List<Event> eventList = App.getInstance().getEventDatabase().getAll();

            for (Event event : eventList) {
                calendarDaysList.add(toCalendarDay(event.getDate()));
            }

            return calendarDaysList;
        }
    }

    private class UpdateListEventsTask extends AsyncTask<Event, Void, List<Event>> {

        @Override
        protected void onPostExecute(List<Event> eventList) {
            getViewState().showEventList(eventList);
        }

        @Override
        protected List<Event> doInBackground(Event... events) {
            return App.getInstance().getEventDatabase().getAll();
        }
    }

    private class ShowEventsOfChoisenDayTask extends AsyncTask<String, Void, List<Event>> {

        @Override
        protected void onPostExecute(List<Event> eventList) {
            getViewState().showEventList(eventList);
        }

        @Override
        protected List<Event> doInBackground(String... date) {
            return App.getInstance().getEventDatabase().getByDate(date[0]);
        }
    }

    public CalendarDay toCalendarDay(String date) {
        int year, month, day;

        year = Integer.parseInt(date.substring(0, date.indexOf('-')));
        month = Integer.parseInt(date.substring(date.indexOf('-') + 1, date.lastIndexOf('-')));
        day = Integer.parseInt(date.substring(date.lastIndexOf('-') + 1));

        return CalendarDay.from(year, month, day);
    }
}
