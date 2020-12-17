package com.example.myapplication;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.myapplication.DAO.FilmsDao;

@Database(entities = {Film.class}, version = 1)
public abstract class FilmsDatabase extends RoomDatabase {
    public abstract FilmsDao getEducationDao();
}
