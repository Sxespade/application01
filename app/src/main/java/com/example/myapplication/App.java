package com.example.myapplication;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.myapplication.DAO.FilmsDao;

public class App extends Application {

    private static App instance;

    // База данных
    private FilmsDatabase db;

    // Получаем объект приложения
    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Сохраняем объект приложения (для Singleton’а)
        instance = this;

        // Строим базу

                db = Room.databaseBuilder(
                        getApplicationContext(),
                        FilmsDatabase.class,
                        "education_database")
                        .allowMainThreadQueries()
                        .build();
    }

    // Получаем EducationDao для составления запросов
    public FilmsDao getEducationDao() {
        return db.getEducationDao();
    }
}
