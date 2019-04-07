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
import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.threeten.bp.LocalDate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class NewEventActivity extends AppCompatActivity {

    EditText etHeader, etMainText, etDate;
    String title, description, date, currentDate;
    ImageButton ok, back;
    InputMethodManager imgr;
    NewEventTask newEventTask;
    String formattedDate;
    SimpleDateFormat dateFormat;
    SimpleDateFormat out;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        getSupportActionBar().hide();

        etHeader = findViewById(R.id.et_header);
        etMainText = findViewById(R.id.et_main_text);

        currentDate = getIntent().getStringExtra("SelectedDay");

        etDate = findViewById(R.id.et_date);
        etDate.setText(formatDate("yyyy-MM-dd","EEEE, dd MMMM, yyyy"));
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

                Event event = new Event();
                event.setTitle(title);
                event.setDescription(description);
                event.setDate(currentDate);

                newEventTask = new NewEventTask();
                newEventTask.execute(event);

                imgr.hideSoftInputFromWindow(NewEventActivity.this.getCurrentFocus().getWindowToken(), 0);

                Intent intent = new Intent();
                intent.putExtra("Event", event);
                setResult(RESULT_OK, intent);
                finish();

            }
        });

        back = findViewById(R.id.image_button_arrow_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgr.hideSoftInputFromWindow(NewEventActivity.this.getCurrentFocus().getWindowToken(), 0);
                onBackPressed();
            }
        });
    }

    private String formatDate(String fromPattern, String toPattern){
        dateFormat = new SimpleDateFormat(fromPattern, Locale.getDefault());
        out = new SimpleDateFormat(toPattern, Locale.getDefault());

        try {
            Date res = dateFormat.parse(currentDate);
            formattedDate = out.format(res);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }

    class NewEventTask extends AsyncTask<Event, Void, Integer> {

        private static final int RESULT_OK = 233;

        @Override
        protected void onPostExecute(Integer result) {
            if (result == RESULT_OK) {
                Toast.makeText(NewEventActivity.this, getString(R.string.event_aded), Toast.LENGTH_SHORT).show();
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
}
