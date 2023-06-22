package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film create(Film film);

    Film update(Film film);

    String delete(Integer id);

    List<Film> allFilms();

    Film getTargetFilm(Integer id);
}
