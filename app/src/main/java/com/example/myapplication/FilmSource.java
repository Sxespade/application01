package com.example.myapplication;

import com.example.myapplication.DAO.FilmsDao;

import java.util.List;

public class FilmSource {

    private final FilmsDao filmsDao;
    private static Thread thread;

    // Буфер с данными: сюда будем подкачивать данные из БД
    private List<Film> films;

    public FilmSource(FilmsDao educationDao) {
        this.filmsDao = educationDao;
    }

    // Получить всех студентов
    public List<Film> getFilms() {
        // Если объекты еще не загружены, загружаем их.
        // Это сделано для того, чтобы не делать запросы к БД каждый раз
        if (films == null) {
            LoadFilms();
        }
        return films;
    }

    // Загружаем студентов в буфер
    public void LoadFilms() {
        films = filmsDao.getAllFilms();

    }

    // Получаем количество записей
    public long getCountFilms() {
        return filmsDao.getCountFilms();
    }

    // Добавляем студента
    public void addFilm(Film film) {
        filmsDao.insertStudent(film);
        // После изменения БД надо повторно прочесть данные из буфера
        LoadFilms();
    }

    // Заменяем студента
    public void updateFilm(Film film) {
        filmsDao.updateStudent(film);
        LoadFilms();
    }

    // Удаляем студента из базы
    public void removeFilm(long id) {
        filmsDao.deteleFilmById(id);
        LoadFilms();
    }

    public void removeFilmByTitle(String title) {
        filmsDao.deteleFilmByTitle(title);
        LoadFilms();
    }

    public Film selectByTitle(String title) {
        return  filmsDao.selectByName(title);
    }

}