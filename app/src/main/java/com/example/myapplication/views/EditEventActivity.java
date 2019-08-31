package com.example.myapplication.views;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.myapplication.Calendar.ParseDate.DateParser;
import com.example.myapplication.DatePickerFragment;
import com.example.myapplication.R;
import com.example.myapplication.models.database.App;
import com.example.myapplication.models.database.EventDao;
import com.example.myapplication.models.entity.Event;
import com.example.myapplication.presenters.EditEventPresenter;
import com.example.myapplication.presenters.NewEventPresenter;

import moxy.MvpAppCompatActivity;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;


public class EditEventActivity extends MvpAppCompatActivity implements DatePickerFragment.onFragmentDateListener {

    private EditText etHeader, etMainText, etDate;

    private DatePickerFragment datePickerFragment;
    //private InputMethodManager imgr;
    private UpdateEventTask updateEventTask;


    @InjectPresenter
    EditEventPresenter newEditEventPresenter;

    @ProvidePresenter
    protected EditEventPresenter provideNewEventPresenter() {
        return new EditEventPresenter(getIntent());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);



        etHeader = findViewById(R.id.et_header);
        etMainText = findViewById(R.id.et_main_text);
        etDate = findViewById(R.id.et_date);
        ImageButton ok = findViewById(R.id.imageButtonOk);
        ImageButton back = findViewById(R.id.image_button_arrow_back);

        etHeader.setText(event.getTitle());
        etMainText.setText(event.getDescription());
        etDate.setText(DateParser.formatDate(event.getDate(),"yyyy-MM-dd", "EEEE, dd MMMM, yyyy" ));

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
                event.setDate(currentDate);

                updateEventTask = new UpdateEventTask();
                updateEventTask.execute(event);

              //  imgr.hideSoftInputFromWindow(EditEventActivity.this.getCurrentFocus().getWindowToken(), 0);

                Intent intent = new Intent();
                setResult(2, intent);
                finish();

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  imgr.hideSoftInputFromWindow(EditEventActivity.this.getCurrentFocus().getWindowToken(), 0);
                onBackPressed();
            }
        });

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //etDate.setInputType(InputType.TYPE_NULL);
                //imgr.hideSoftInputFromWindow(EditEventActivity.this.getCurrentFocus().getWindowToken(), 0);
                datePickerFragment = new DatePickerFragment();
                datePickerFragment.show(getSupportFragmentManager(), "DatePicker");
            }
        });
    }

    @Override
    public void onDateSateChoisen(int year, int month, int day) {

        currentDate = DateParser.formatDate((Integer.toString(year) + "-" + Integer.toString(month + 1) + "-" + Integer.toString(day)),
                "yyyy-MM-dd", "yyyy-MM-dd");
        etDate.setText(DateParser.formatDate(currentDate, "yyyy-MM-dd","EEEE, dd MMMM, yyyy"));
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
                dao.updateById(events[0].getUid(), events[0].getTitle(), events[0].getDescription(), events[0].getDate());
            }

            return RESULT_OK;
        }
    }
}
