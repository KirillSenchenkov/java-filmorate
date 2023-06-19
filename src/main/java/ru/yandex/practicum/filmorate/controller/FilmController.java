package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final FilmService filmService;
    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public FilmController(InMemoryFilmStorage inMemoryFilmStorage, FilmService filmService,
                          InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.filmService = filmService;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    @GetMapping
    public List<Film> filmList() {
        return inMemoryFilmStorage.allFilms();
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        return inMemoryFilmStorage.create(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        return inMemoryFilmStorage.update(film);
    }

    @DeleteMapping("/{filmId}")
    public String deleteFilm(@PathVariable Integer filmId) {
        return inMemoryFilmStorage.delete(filmId);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public String likeFilmByUser(@PathVariable Integer filmId, @PathVariable Integer userId) {
       return filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public String deleteLike(@PathVariable Integer filmId, @PathVariable Integer userId){
      return filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/popular")
    public Set<Film> getFilmByPopularity(
            @RequestParam(value = "count", defaultValue = "10", required = false) Integer count){
        return filmService.getFilmByPopularity(count);
    }
}
