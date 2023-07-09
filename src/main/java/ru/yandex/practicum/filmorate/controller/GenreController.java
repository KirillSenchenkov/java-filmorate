package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.GenreDbStorage;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/genres")
public class GenreController {

    private final GenreDbStorage genreDbStorage;

    @GetMapping
    public List<Genre> getAllGenres() {
        return genreDbStorage.getAllGenre();
    }

    @GetMapping("/{genreId}")
    public Genre getTargetGenre(@PathVariable Integer genreId) {
        return genreDbStorage.getTargetGenre(genreId);
    }
}
