package com.example.myapplication.models.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.myapplication.models.entity.Event;

@Database(entities = {Event.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract EventDao eventDao();

}
