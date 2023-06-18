package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

@Service
public class FilmService {

    private InMemoryFilmStorage inMemoryFilmStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage){
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    public void addLike(Integer filmId, User user){
        Film film = inMemoryFilmStorage.getTargetFilm(filmId);
        film.addLike(user);
    }
}
