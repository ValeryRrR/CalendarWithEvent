package com.example.myapplication.presenters;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.example.myapplication.Calendar.ParseDate.DateParser;
import com.example.myapplication.models.entity.Event;
import com.example.myapplication.views.EditEventView;

import java.util.Objects;

import moxy.InjectViewState;
import moxy.MvpPresenter;

@InjectViewState
public class EditEventPresenter extends MvpPresenter<EditEventView> {

    @NonNull
    private Intent intent;

    @NonNull
    private String rawSelectedDay;

    @NonNull
    private Event event;

    public EditEventPresenter(@NonNull Intent intent){
        this.intent =  intent;
        event = (Event) intent.getSerializableExtra("EditEvent");
        rawSelectedDay = Objects.requireNonNull(intent.getStringExtra("SelectedDay"));
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        String currentDate = DateParser.formatDate(rawSelectedDay,"yyyy-MM-dd","EEEE, dd MMMM, yyyy");
        getViewState().fillEditDate(currentDate);

        if(event.getTitle() != null) getViewState().fillHeader(event.getTitle());
        if(event.getDescription() != null) getViewState().fillDescription(event.getDescription());

    }
}
