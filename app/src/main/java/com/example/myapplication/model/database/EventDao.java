package com.example.myapplication.model.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.myapplication.model.entity.Event;

import java.util.List;

@Dao
public interface EventDao {

    @Query("SELECT * FROM event")
    List<Event> getAll();

    @Query("SELECT * FROM event WHERE uid IN (:eventIds)")
    List<Event> loadAllByIds(int[] eventIds);

    @Query("SELECT EXISTS(SELECT * FROM event e WHERE (:date)=e.date)")
    boolean eventIsExists(String date);

    @Query("SELECT * FROM event WHERE date LIKE (:date)")
    List<Event> getByDate(String date);

    @Query("UPDATE event SET title = :title, description = :description WHERE uid = :id")
    void updateById(int id, String title, String description);

    @Insert
    void insertAll(Event... events);

    @Delete
    void delete(Event event);

    @Update
    void updateEvent(Event... users);

}

