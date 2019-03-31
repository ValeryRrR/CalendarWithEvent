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

//    @Query("SELECT * FROM event d WHERE EXISTS (SELECT * FROM employees e WHERE d.department_id = e.department_id);")
//    boolean eventIsExists();

    @Query("SELECT EXISTS(SELECT * FROM event e WHERE (:date)=e.date)")
    boolean eventIsExists2(String date);
   /*
   @Query("SELECT * FROM event WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    User findByName(String first, String last);
*/
    @Insert
    void insertAll(Event... events);

    @Delete
    void delete(Event event);

    @Update
    void updateEvent(Event... users);

}

