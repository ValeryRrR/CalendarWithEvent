package com.example.myapplication.model.database;

import android.app.Application;
import android.arch.persistence.room.Room;

public class App extends Application {

    public static App instance;

    private EventDao database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, AppDatabase.class, "database")
                .build().eventDao();
    }

    public static App getInstance() {
        return instance;
    }

    public EventDao getEventDatabase() {
        return database;
    }
}