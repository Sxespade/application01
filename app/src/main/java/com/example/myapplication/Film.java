package com.example.myapplication;

import android.graphics.drawable.Drawable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"title", "year","image"})})
public class Film {

    // @PrimaryKey - указывает на ключевую запись,
    // autoGenerate = true - автоматическая генерация ключа
    @PrimaryKey(autoGenerate = true)
    public long id;

    // Имя студента
    // @ColumnInfo позволяет задавать параметры колонки в БД
    // name = "first_name" - имя колонки
    @ColumnInfo(name = "title")
    public String title;

    // Фамилия студента
    @ColumnInfo(name = "year")
    public String year;

    @ColumnInfo(name = "image")
    public byte[] image;
}