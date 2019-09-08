package com.example.myapplication.presenters;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.myapplication.Calendar.ParseDate.DateParser;
import com.example.myapplication.R;
import com.example.myapplication.models.database.App;
import com.example.myapplication.models.database.EventDao;
import com.example.myapplication.models.entity.Event;
import com.example.myapplication.views.NewEventView;

import java.util.Objects;

import moxy.InjectViewState;
import moxy.MvpPresenter;

@InjectViewState
public class NewEventPresenter extends MvpPresenter<NewEventView> {

    @NonNull
    private Intent intent;

    @NonNull
    private String rawSelectedDay;

    public NewEventPresenter(@NonNull Intent intent) {
        this.intent = intent;
        rawSelectedDay = Objects.requireNonNull(intent.getStringExtra("SelectedDay"));
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        String currentDate = DateParser.formatDate(rawSelectedDay,"yyyy-MM-dd","EEEE, dd MMMM, yyyy");
        getViewState().fillEditDate(currentDate);
    }

    public void etDateOnClicked() {
        getViewState().hideKeyboard();
        getViewState().moveToDatePicker();
    }

    public void okButtonClicked(@NonNull String title, @NonNull String description) {
        if (title.equals("") && description.equals("")) {
            getViewState().showToast(R.string.fill_in_the_header);
        }
        else {

            getViewState().hideKeyboard();

            Event event = new Event();
            event.setTitle(title);
            event.setDescription(description);
            event.setDate(rawSelectedDay);

            Intent intent = new Intent();
            intent.putExtra("Event", event);

            new NewEventTask(intent).execute(event);
            getViewState().finishView(intent);
        }
    }

    public void onCurrentDateChoisen(int year, int month, int day) {
        rawSelectedDay = DateParser.formatDate((Integer.toString(year) + "-" + Integer.toString(month + 1) + "-" + Integer.toString(day)),
                "yyyy-MM-dd", "yyyy-MM-dd");

        String currentDate = DateParser.formatDate(rawSelectedDay, "yyyy-MM-dd","EEEE, dd MMMM, yyyy");
        getViewState().fillEditDate(currentDate);
    }


    class NewEventTask extends AsyncTask<Event, Void, Void> {

        private Intent finishIntent;

        public NewEventTask(Intent finishIntent) {
            this.finishIntent = finishIntent;
        }

        @Override
        protected void onPostExecute(Void result) {
            getViewState().showToast(R.string.event_aded);
        }

        @Override
        protected Void doInBackground(Event... events) {

            EventDao dao = App.getInstance().getEventDatabase();
            if (events != null && events.length > 0) {
                dao.insertAll(events);
            }

            return null;
        }
    }
}
