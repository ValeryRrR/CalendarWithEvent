package com.example.myapplication.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.myapplication.DatePickerFragment;
import com.example.myapplication.R;
import com.example.myapplication.presenters.EditEventPresenter;

import moxy.MvpAppCompatActivity;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;


public class EditEventActivity extends MvpAppCompatActivity implements DatePickerFragment.onFragmentDateListener, EditEventView {

    private EditText etHeader, etMainText, etDate;
    private DatePickerFragment datePickerFragment;
    public static final int EVENT_ADDED = 2;

    @InjectPresenter
    EditEventPresenter editEventPresenter;

    @ProvidePresenter
    protected EditEventPresenter provideEditEventPresenter() {
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

        /*Showing keybord when editText focused*/
        // imgr = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        // imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editEventPresenter.okButtonClicked(etHeader.getText().toString(), etMainText.getText().toString());
                hideKeyboard();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
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
    public void fillEditDate(@NonNull String date) {
        etDate.setText(date);
    }

    @Override
    public void fillHeader(@NonNull String header) {
        etHeader.setText(header);
    }

    @Override
    public void fillDescription(@NonNull String description) {
        etMainText.setText(description);
    }

    @Override
    public void showToast(int textId) {
        Toast.makeText(EditEventActivity.this, getString(textId), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideKeyboard() {

    }

    @Override
    public void moveToDatePicker() {
        datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getSupportFragmentManager(), "DatePicker");
    }

    @Override
    public void finishView(Intent intent) {
        setResult(EVENT_ADDED, intent);
        finish();
    }

    @Override
    public void onDateSateChoisen(int year, int month, int day) {
        editEventPresenter.onCurrentDateChoisen(year, month, day);
    }
}
