package com.example.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.myapplication.Calendar.ParseDate.DateParser;
import com.example.myapplication.model.database.App;
import com.example.myapplication.model.database.EventDao;
import com.example.myapplication.model.entity.Event;



public class EditEventActivity extends AppCompatActivity {

    private EditText etHeader;
    private EditText etMainText;
    private String title, description;
    //private InputMethodManager imgr;
    private UpdateEventTask updateEventTask;
    private Event event;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        getSupportActionBar().hide();

        event = (Event) getIntent().getSerializableExtra("EditEvent");

        etHeader = findViewById(R.id.et_header);
        etMainText = findViewById(R.id.et_main_text);
        EditText etDate = findViewById(R.id.et_date);
        ImageButton ok = findViewById(R.id.imageButtonOk);

        etHeader.setText(event.getTitle());
        etMainText.setText(event.getDescription());
        etDate.setText(DateParser.formatDate(event.getDate(),"yyyy-MM-dd", "EEEE, dd MMMM, yyyy" ));
        etDate.setEnabled(false);

        /*Showing keybord when editText focused*/
       // imgr = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
       // imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                title = etHeader.getText().toString();
                description = etMainText.getText().toString();

                if(title.equals("") && description.equals("")){
                    Toast.makeText(EditEventActivity.this, getString(R.string.fill_in_the_header), Toast.LENGTH_SHORT).show();
                    return;
                }

                event.setTitle(title);
                event.setDescription(description);

                updateEventTask = new UpdateEventTask();
                updateEventTask.execute(event);

              //  imgr.hideSoftInputFromWindow(EditEventActivity.this.getCurrentFocus().getWindowToken(), 0);

                Intent intent = new Intent();
                setResult(2, intent);
                finish();

            }
        });

        ImageButton back = findViewById(R.id.image_button_arrow_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  imgr.hideSoftInputFromWindow(EditEventActivity.this.getCurrentFocus().getWindowToken(), 0);
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
                dao.updateById(events[0].getUid(), events[0].getTitle(), events[0].getDescription());
            }

            return RESULT_OK;
        }
    }
}
