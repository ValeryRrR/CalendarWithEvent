package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();


        final MaterialCalendarView calendarView = (MaterialCalendarView) findViewById(R.id.calendarView);

        calendarView.setDateSelected(CalendarDay.today(), true);
        calendarView.setTopbarVisible(false);

        DayDecorator dayDecorator = new DayDecorator(Color.parseColor("#F2C200"), CalendarDay.today());
        calendarView.addDecorator(dayDecorator);
        calendarView.invalidateDecorators();

        HashSet<CalendarDay> calendarDaysList = new HashSet<>();

       CalendarDay two =   CalendarDay.from(2019, 4,9);
       CalendarDay three =   CalendarDay.from(2019, 4,11);
       calendarDaysList.add(two);
       calendarDaysList.add(three);

        GroupOfDaysDecorator groupOfDaysDecorator = new GroupOfDaysDecorator(Color.parseColor("#F00A6B"), calendarDaysList );
        calendarView.addDecorator(groupOfDaysDecorator);
        calendarView.invalidateDecorators();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), NewEventActivity.class);
                intent.putExtra("SELECTDAY", calendarView.getSelectedDate().getDate().toString());
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Event event = (Event) data.getSerializableExtra("Event");
        EventTask eventTask = new EventTask();
        eventTask.execute(event);

    }


    class EventTask extends AsyncTask<Event, Void, List<Event>> {

        private static final int RESULT_OK = 233;

        @Override
        protected void onPostExecute(List<Event> eventList) {

            TextView textViewEvents = (TextView) findViewById(R.id.text_view);
            textViewEvents.setText("");

            for (Event event : eventList
            ) {
                textViewEvents.append("\n" + event.title + " " + event.description + " " + event.date);
            }

            Toast.makeText(MainActivity.this, "Событие создано", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected List<Event> doInBackground(Event... events) {
            EventDao dao = App.getInstance().getEventDatabase();
            if (events != null && events.length > 0) {
                dao.insertAll(events);
            }

            return App.getInstance().getEventDatabase().getAll();
        }
    }
}
