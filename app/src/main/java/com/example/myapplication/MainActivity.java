package com.example.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Calendar.DayDecorator;
import com.example.myapplication.Calendar.GroupOfDaysDecorator;
import com.example.myapplication.model.database.App;
import com.example.myapplication.model.database.EventDao;
import com.example.myapplication.model.entity.Event;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.HashSet;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    MaterialCalendarView calendarView;
    UpdateColorTask updateColorTask;
    NewEventTask newEventTask;
    DayDecorator dayDecorator;
    GroupOfDaysDecorator groupOfDaysDecorator;
    FloatingActionButton fab;
    EventsRecyclerAdapter eventsRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        calendarView = findViewById(R.id.calendarView);

        calendarView.setTopbarVisible(false);

        updateColorTask = new UpdateColorTask();
        updateColorTask.execute();

        ListEventsTask listEventsTask = new ListEventsTask();
        listEventsTask.execute();

        dayDecorator = new DayDecorator(getResources().getColor(R.color.colorCurentDay), CalendarDay.today(), 1.8f);
        calendarView.addDecorator(dayDecorator);
        calendarView.invalidateDecorators();

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (calendarView.getSelectedDate() == null) {
                    Toast.makeText(MainActivity.this, getString(R.string.choise_date), Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(view.getContext(), NewEventActivity.class);
                intent.putExtra("SELECTDAY", calendarView.getSelectedDate().getDate().toString());
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data == null) {
            return;
        }
        Event event = (Event) data.getSerializableExtra("Event");
        newEventTask = new NewEventTask();
        newEventTask.execute(event);

        DayDecorator dayDecoratorEvent = new DayDecorator(getResources().getColor(R.color.colorDaysWithEvent), toCalendarDay(event.date), 1);
        calendarView.addDecorator(dayDecoratorEvent);
        calendarView.invalidateDecorators();

        eventsRecyclerAdapter.updateEvent(event);

    }


    class NewEventTask extends AsyncTask<Event, Void, Integer> {

        private static final int RESULT_OK = 233;

        @Override
        protected void onPostExecute(Integer result) {
            if (result == RESULT_OK) {
                Toast.makeText(MainActivity.this, getString(R.string.event_aded), Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected Integer doInBackground(Event... events) {

            EventDao dao = App.getInstance().getEventDatabase();
            if (events != null && events.length > 0) {
                dao.insertAll(events);
            }

            return RESULT_OK;
        }
    }


    class UpdateColorTask extends AsyncTask<String, Void, HashSet<CalendarDay>> {

        @Override
        protected void onPostExecute(HashSet<CalendarDay> calendarDaysList) {
            if (!calendarDaysList.isEmpty()) {
                groupOfDaysDecorator = new GroupOfDaysDecorator(getResources().getColor(R.color.colorDaysWithEvent), calendarDaysList);
                calendarView.addDecorator(groupOfDaysDecorator);
                calendarView.invalidateDecorators();
            }

        }

        @Override
        protected HashSet<CalendarDay> doInBackground(String... currentMonth) {

            HashSet<CalendarDay> calendarDaysList = new HashSet<>();
            List<Event> eventList = App.getInstance().getEventDatabase().getAll();

            for (Event event : eventList
            ) {
                calendarDaysList.add(toCalendarDay(event.date));
            }

            return calendarDaysList;
        }
    }

    public CalendarDay toCalendarDay(String date) {
        int year, month, day;

        year = Integer.parseInt(date.substring(0, date.indexOf('-')));
        month = Integer.parseInt(date.substring(date.indexOf('-') + 1, date.lastIndexOf('-')));
        day = Integer.parseInt(date.substring(date.lastIndexOf('-') + 1));

        return CalendarDay.from(year, month, day);
    }


    class ListEventsTask extends AsyncTask<Event, Void, List<Event>> {


        @Override
        protected void onPostExecute(List<Event> eventList) {

            eventsRecyclerAdapter = new EventsRecyclerAdapter(eventList);
            RecyclerView recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setAdapter(eventsRecyclerAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        }

        @Override
        protected List<Event> doInBackground(Event... events) {

            return App.getInstance().getEventDatabase().getAll();
        }
    }

}


