package com.example.myapplication.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.example.myapplication.Film;

import java.util.List;

@Dao
public interface FilmsDao {
    // Метод для добавления студента в базу данных
    // @Insert - признак добавления
    // onConflict - что делать, если такая запись уже есть
    // В данном случае просто заменим её
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertStudent(Film film);

    // Метод для замены данных студента
    @Update
    void updateStudent(Film film);

    // Удаляем данные студента
    @Delete
    void deleteStudent(Film film);

    // Удаляем данные студента, зная ключ
    @Query("DELETE FROM film WHERE id = :id")
    void deteleFilmById(long id);

    @Query("DELETE FROM film WHERE title = :title")
    void deteleFilmByTitle(String title);

    // Забираем данные по всем студентам
    @Query("SELECT * FROM film")
    List<Film> getAllFilms();

    // Получаем данные одного студента по id
    @Query("SELECT * FROM film WHERE id = :id")
    Film getFilmById(long id);

    //Получаем количество записей в таблице
    @Query("SELECT COUNT() FROM film")
    long getCountFilms();

    @Query("SELECT * FROM film WHERE title = :title")
    Film selectByName(String title);
}