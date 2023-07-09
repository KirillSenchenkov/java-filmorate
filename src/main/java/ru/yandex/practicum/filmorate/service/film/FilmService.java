package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    String addLike(Integer filmId, Integer userId);

    String deleteLike(Integer filmId, Integer userId);

    List<Film> getFilmByPopularity(Integer count);
}
