package com.example.projektlodowka.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.projektlodowka.database.Historia;

import java.util.List;

@Dao
public interface HistoriaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Historia... histora);

    @Update
    void update(Historia... historia);

    @Delete
    void delete(Historia... historia);

    @Query("SELECT * FROM Historia")
    List<Historia> loadAll();

    @Query("SELECT * FROM Historia WHERE id = :id")
    Historia loadId(int id);

    @Query("SELECT * FROM Historia WHERE idPrzepisu = :idPrzepisu")
    List<Historia> loadPrzepis(int idPrzepisu);

    @Query("SELECT * FROM Historia WHERE data = :data")
    List<Historia> loadData(String data);
}