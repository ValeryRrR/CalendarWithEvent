package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.myapplication.model.database.App;
import com.example.myapplication.model.database.EventDao;
import com.example.myapplication.model.entity.Event;

public class EditEventActivity extends AppCompatActivity {

    EditText etHeader, etMainText, etDate;
    String title, description, date;
    ImageButton ok, back;
    InputMethodManager imgr;
    UpdateEventTask updateEventTask;
    Event event;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        getSupportActionBar().hide();

        event = (Event) getIntent().getSerializableExtra("EditEvent");

        etHeader = findViewById(R.id.et_header);
        etMainText = findViewById(R.id.et_main_text);
        etDate = findViewById(R.id.et_date);

        etHeader.setText(event.title);
        etMainText.setText(event.description);
        etDate.setText(event.date);
        etDate.setEnabled(false);

        /*Showing keybord when editText focused*/
        imgr = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);


        ok = findViewById(R.id.imageButtonOk);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                title = etHeader.getText().toString();
                description = etMainText.getText().toString();
                date = etDate.getText().toString();


                event.title = title;
                event.description = description;
                event.date = date;

                updateEventTask = new UpdateEventTask();
                updateEventTask.execute(event);

                imgr.hideSoftInputFromWindow(EditEventActivity.this.getCurrentFocus().getWindowToken(), 0);

                Intent intent = new Intent();
                setResult(2, intent);
                finish();

            }
        });

        back = findViewById(R.id.image_button_arrow_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgr.hideSoftInputFromWindow(EditEventActivity.this.getCurrentFocus().getWindowToken(), 0);
                onBackPressed();
            }
        });
    }

    class UpdateEventTask extends AsyncTask<Event, Void, Integer> {

        private static final int RESULT_OK = 233;

        @Override
        protected void onPostExecute(Integer result) {
            if (result == RESULT_OK) {
                Toast.makeText(EditEventActivity.this, getString(R.string.event_updeted), Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected Integer doInBackground(Event... events) {

            EventDao dao = App.getInstance().getEventDatabase();
            if (events != null && events.length > 0) {
                dao.updateById(events[0].uid, events[0].title, events[0].description);
            }

            return RESULT_OK;
        }
    }
}
