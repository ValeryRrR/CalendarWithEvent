package com.example.myapplication.presenters;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.myapplication.Calendar.ParseDate.DateParser;
import com.example.myapplication.R;
import com.example.myapplication.models.database.App;
import com.example.myapplication.models.database.EventDao;
import com.example.myapplication.models.entity.Event;
import com.example.myapplication.views.EditEventView;

import moxy.InjectViewState;
import moxy.MvpPresenter;

@InjectViewState
public class  EditEventPresenter extends MvpPresenter<EditEventView> {

    @NonNull
    private Intent intent;

    @NonNull
    private String rawSelectedDay;

    @NonNull
    private Event event;

    public EditEventPresenter(@NonNull Intent intent){
        this.intent =  intent;
        event = (Event) intent.getSerializableExtra("EditEvent");
        rawSelectedDay = event.getDate();
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        String currentDate = DateParser.formatDate(rawSelectedDay,"yyyy-MM-dd","EEEE, dd MMMM, yyyy");
        getViewState().fillEditDate(currentDate);

        if(event.getTitle() != null) getViewState().fillHeader(event.getTitle());
        if(event.getDescription() != null) getViewState().fillDescription(event.getDescription());

    }

    public void okButtonClicked(@NonNull String title, @NonNull String description) {
        /*if (title.equals("") && description.equals("")) {
            getViewState().showToast(R.string.fill_in_the_header);
        }
        else {*/

            getViewState().hideKeyboard();

            event.setTitle(title);
            event.setDescription(description);
            event.setDate(rawSelectedDay);

            intent.putExtra("Event", event);

            new UpdateEventTask().execute(event);
    }

    public void onCurrentDateChoisen(int year, int month, int day) {
        rawSelectedDay = DateParser.formatDate((Integer.toString(year) + "-" + Integer.toString(month + 1) + "-" + Integer.toString(day)),
                "yyyy-MM-dd", "yyyy-MM-dd");

        String currentDate = DateParser.formatDate(rawSelectedDay, "yyyy-MM-dd","EEEE, dd MMMM, yyyy");
        getViewState().fillEditDate(currentDate);
    }

    class UpdateEventTask extends AsyncTask<Event, Void, Void> {

        @Override
        protected void onPostExecute(Void result) {
            getViewState().showToast(R.string.event_updeted);
            getViewState().finishView(intent);
        }

        @Override
        protected Void doInBackground(Event... events) {

            EventDao dao = App.getInstance().getEventDatabase();
            if (events != null && events.length > 0) {
                dao.updateById(events[0].getUid(), events[0].getTitle(), events[0].getDescription(), events[0].getDate());
            }

            return null;
        }
    }
}
