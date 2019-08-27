package com.example.myapplication.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.example.myapplication.DatePickerFragment;
import com.example.myapplication.R;
import com.example.myapplication.presenters.NewEventPresenter;

import moxy.MvpAppCompatActivity;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;


public class NewEventActivity extends MvpAppCompatActivity implements DatePickerFragment.onFragmentDateListener, NewEventView {

    private EditText etHeader, etMainText, etDate;
    private ImageButton imageButtonOk;

    private DatePickerFragment datePickerFragment;
    private InputMethodManager imgr;

    @InjectPresenter
    NewEventPresenter newEventPresenter;

    @ProvidePresenter
    protected NewEventPresenter provideNewEventPresenter() {
        return new NewEventPresenter(getIntent());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        etHeader = findViewById(R.id.et_header);
        etMainText = findViewById(R.id.et_main_text);
        etDate = findViewById(R.id.et_date);
        imageButtonOk = findViewById(R.id.imageButtonOk);

        /*Showing keybord when editText focused*/
        imgr = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newEventPresenter.etDateOnClicked();
            }
        });

        imageButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newEventPresenter.okButtonClicked(etHeader.getText().toString(), etMainText.getText().toString());
            }
        });

        ImageButton back = findViewById(R.id.image_button_arrow_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                onBackPressed();
            }
        });
    }

    @Override
    public void showEditDate(@NonNull String text) {
        etDate.setText(text);
    }

    @Override
    public void hideKeyboard() {
        imgr.hideSoftInputFromWindow(NewEventActivity.this.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public void moveToDatePicker() {
        datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getSupportFragmentManager(), "DatePicker");
    }

    @Override
    public void showToast(int textId) {
        Toast.makeText(NewEventActivity.this, getString(textId), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finishView(Intent intent) {
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onDateSateChoisen(int year, int month, int day) {
        newEventPresenter.onCurrentDateChoisen(year, month, day);
    }

}
