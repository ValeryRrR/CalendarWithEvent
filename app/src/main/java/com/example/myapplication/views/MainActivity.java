package com.example.myapplication.views;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Calendar.DayDecorator;
import com.example.myapplication.Calendar.GroupOfDaysDecorator;
import com.example.myapplication.Calendar.ParseDate.DateParser;
import com.example.myapplication.R;
import com.example.myapplication.models.entity.Event;
import com.example.myapplication.presenters.MainActivityPresenter;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.HashSet;
import java.util.List;

import moxy.MvpAppCompatActivity;
import moxy.presenter.InjectPresenter;


public class MainActivity extends MvpAppCompatActivity implements MainActivityView {

    private MaterialCalendarView calendarView;
    private EventsRecyclerAdapter eventsRecyclerAdapter;
    private EventsRecyclerAdapter.IItemClickListener listener;
    private TextView currentMonth;
    private RecyclerView recyclerView;
    public static final int EVENT_ADDED = 2;

    @InjectPresenter
    MainActivityPresenter mainActivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendarView = findViewById(R.id.calendarView);
        calendarView.setTopbarVisible(false);
        currentMonth = findViewById(R.id.current_month);
        recyclerView = findViewById(R.id.recyclerView);
        FloatingActionButton fab = findViewById(R.id.fab);
        Button btnShowAll = findViewById(R.id.btn_show_all);

        updateColorOfCalendarDays();
        mainActivityPresenter.updateEventList();
        updateDecorator(getResources().getColor(R.color.colorCurentDay), CalendarDay.today(), 1.8f);
        showCurrentDate();

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay calendarDay, boolean b) {
                mainActivityPresenter.showEventsOfChoisenDay(calendarDay.getDate().toString());
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (calendarView.getSelectedDate() == null) {
                    Toast.makeText(MainActivity.this, getString(R.string.choise_date), Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(view.getContext(), NewEventActivity.class);
                intent.putExtra("SelectedDay", calendarView.getSelectedDate().getDate().toString());
                startActivityForResult(intent, 1);
            }
        });

        btnShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarView.clearSelection();
                mainActivityPresenter.updateEventList();
            }
        });

        listener = new EventsRecyclerAdapter.IItemClickListener() {
            @Override
            public void onItemClick(Event event) {
                Intent intent = new Intent(MainActivity.this, EditEventActivity.class);
                intent.putExtra("EditEvent", event);
                startActivityForResult(intent, 2);
            }
        };

        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView materialCalendarView, CalendarDay calendarDay) {
                currentMonth.setText(DateParser.formatDate(calendarDay.getDate().toString(), "yyyy-MM-dd", "yyyy, LLLL"));
            }
        });
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {

        if (data == null) {
            return;
        }
        Event event = (Event) data.getSerializableExtra("Event");

        calendarView.clearSelection();
        calendarView.removeDecorators();
        mainActivityPresenter.updateEventList();
        updateColorOfCalendarDays();
        updateDecorator(getResources().getColor(R.color.colorDaysWithEvent), mainActivityPresenter.toCalendarDay(event.getDate()), 1);
        eventsRecyclerAdapter.updateEvent(event);
    }

    @Override
    public void updateColorOfCalendarDays() {
        mainActivityPresenter.updateColorOfCalendar();
    }

    @Override
    public void invalidateCalendarViewDecorators(HashSet<CalendarDay> calendarDaysList) {
        calendarView.addDecorator(new GroupOfDaysDecorator(getResources().getColor(R.color.colorDaysWithEvent), calendarDaysList));
        calendarView.invalidateDecorators();
    }

    @Override
    public void showEventList(List<Event> eventList) {
        eventsRecyclerAdapter = new EventsRecyclerAdapter(eventList);
        eventsRecyclerAdapter.setOnClickListener(listener);
        recyclerView.setAdapter(eventsRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void updateDecorator(int color, CalendarDay day, float proportion) {
        calendarView.addDecorator(new DayDecorator(color, day, proportion));
        calendarView.invalidateDecorators();
    }

    @Override
    public void showCurrentDate() {
        currentMonth.setText(DateParser.formatDate(calendarView.getCurrentDate().getDate().toString(), "yyyy-MM-d", "yyyy, LLLL"));
    }
}


