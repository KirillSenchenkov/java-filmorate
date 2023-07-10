package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public class FilmGenreModifier {
    private static final String COMEDY = "Комедия";
    private static final String DRAMA = "Драма";
    private static final String CARTOON = "Мультфильм";
    private static final String THRILLER = "Триллер";
    private static final String DOCUMENTARY = "Документальный";
    private static final String FIGHTER = "Боевик";

    public static void modFilmGenres(Film film) {
        if (film.getGenres().size() != 0) {
            List<Genre> genres = film.getGenres();
            for (Genre genre : genres) {
                if (genre.getId() == 1) {
                    genre.setName(COMEDY);
                } else if (genre.getId() == 2) {
                    genre.setName(DRAMA);
                } else if (genre.getId() == 3) {
                    genre.setName(CARTOON);
                } else if (genre.getId() == 4) {
                    genre.setName(THRILLER);
                } else if (genre.getId() == 5) {
                    genre.setName(DOCUMENTARY);
                } else if (genre.getId() == 6) {
                    genre.setName(FIGHTER);
                } else {
                    throw new IncorrectIdException("Идентификатор жанра не верный: " + genre.getId());
                }
            }
            film.setGenres(genres);
        }
    }
}
