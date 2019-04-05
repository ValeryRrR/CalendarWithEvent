package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.myapplication.model.entity.Event;

public class NewEventActivity extends AppCompatActivity {

    EditText etHeader, etMainText, etDate;
    String title, description, date;
    ImageButton ok, back;
    InputMethodManager imgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        getSupportActionBar().hide();

        etHeader = findViewById(R.id.et_header);
        etMainText = findViewById(R.id.et_main_text);

        etDate = findViewById(R.id.et_date);
        etDate.setText(getIntent().getStringExtra("SELECTDAY"));
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
                event.title = title;
                event.description = description;
                event.date = date;

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
}
