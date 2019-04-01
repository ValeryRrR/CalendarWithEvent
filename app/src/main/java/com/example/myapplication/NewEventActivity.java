package com.example.myapplication;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.myapplication.model.database.App;
import com.example.myapplication.model.database.AppDatabase;
import com.example.myapplication.model.database.EventDao;
import com.example.myapplication.model.entity.Event;

public class NewEventActivity extends AppCompatActivity {

    EditText etHeader, etMainText, etDate;
    String title, description, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        getSupportActionBar().hide();

        etHeader = (EditText) findViewById(R.id.et_header);
        etMainText = (EditText) findViewById(R.id.et_main_text);
        etDate = (EditText) findViewById(R.id.et_date);



        /*Showing keybord when editText focused*/
        InputMethodManager imgr = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);


        ImageButton ok = (ImageButton) findViewById(R.id.imageButtonOk);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                title = etHeader.getText().toString();
                description = etMainText.getText().toString();
                date = etDate.getText().toString();

                Event event = new Event();
                event.title = title;
                event.description = description;
                event.date = date;

                EventTask eventTask = new EventTask();
                eventTask.execute(event);

                //startActivity(intent);


            }
        });
    }

    class EventTask extends AsyncTask<Event, Void, Integer> {

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer == RESULT_OK) {
                Toast.makeText(NewEventActivity.this, "Событие создано", Toast.LENGTH_SHORT);
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

        private static final int RESULT_OK = 233;
    }
}
