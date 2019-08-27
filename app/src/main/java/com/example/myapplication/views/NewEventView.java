package com.example.myapplication.views;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.OneExecutionStateStrategy;
import moxy.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface NewEventView extends MvpView {

    void showEditDate(@NonNull String text);

    void showToast(@StringRes int textId);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void hideKeyboard();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void moveToDatePicker();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void finishView(Intent intent);
}
