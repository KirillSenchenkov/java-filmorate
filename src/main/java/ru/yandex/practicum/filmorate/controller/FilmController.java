package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@AllArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public List<Film> filmList() {
        return filmService.allFilms();
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        return filmService.update(film);
    }

    @GetMapping("/{filmId}")
    public Film getFilmById(@PathVariable Integer filmId) {
        return filmService.getTargetFilm(filmId);
    }

    @DeleteMapping("/{filmId}")
    public String deleteFilm(@PathVariable Integer filmId) {
        return filmService.delete(filmId);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public String likeFilmByUser(@PathVariable Integer filmId, @PathVariable Integer userId) {
        return filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public String deleteLike(@PathVariable Integer filmId, @PathVariable Integer userId) {
        return filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getFilmByPopularity(
            @RequestParam(value = "count", defaultValue = "10", required = false) Integer count) {
        return filmService.getFilmByPopularity(count);
    }


}
