package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final InMemoryFilmStorage inMemoryFilmStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage){
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    public String addLike(Integer filmId, Integer userId){
        Film film = inMemoryFilmStorage.getTargetFilm(filmId);
        film.addLike(userId);
        return String.format("Пользователь с Id %s, поставил лайк фильму %s.", userId, film.getName());
    }

    public String deleteLike(Integer filmId, Integer userId){
        Film film = inMemoryFilmStorage.getTargetFilm(filmId);
        if (film.getLikes().contains(userId)){
            film.deleteLike(userId);
            return String.format("Лайк пользователя c Id %s удален с фильма %s", userId, film.getName());
        }
        return String.format("Пользователь с Id %s не ставил лайк фильму %s", userId, film.getName());
    }

    public Set<Film> getFilmByPopularity(Integer count){
        List<Film> films = inMemoryFilmStorage.allFilms();
        return films.stream().sorted(Comparator.comparingInt(film -> film.getLikes().size()))
                .limit(count).collect(Collectors.toSet());
    }

}
