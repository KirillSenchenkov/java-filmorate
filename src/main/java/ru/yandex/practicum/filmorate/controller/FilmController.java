package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> filmList() {
        return filmStorage.allFilms();
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        return filmStorage.create(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        return filmStorage.update(film);
    }

    @DeleteMapping("/{filmId}")
    public String deleteFilm(@PathVariable Integer filmId) {
        return filmStorage.delete(filmId);
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
    public List<Film> getFilmByPopularity(
            @RequestParam(value = "count", defaultValue = "10", required = false) Integer count){
        return filmService.getFilmByPopularity(count);
    }
}
