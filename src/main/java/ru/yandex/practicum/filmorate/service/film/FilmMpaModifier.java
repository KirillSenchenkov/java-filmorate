package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

public class FilmMpaModifier {

    private static final String G = "G";
    private static final String PG = "PG";
    private static final String PG13 = "PG-13";
    private static final String R = "R";
    private static final String NC17 = "NC-17";

    public static void modFilmMpa(Film film) {
        if (film.getMpa() != null) {
            if (film.getMpa().getId() == 1) {
                film.setMpa(new Mpa(1, G));
            } else if (film.getMpa().getId() == 2) {
                film.setMpa(new Mpa(2, PG));
            } else if (film.getMpa().getId() == 3) {
                film.setMpa(new Mpa(3, PG13));
            } else if (film.getMpa().getId() == 4) {
                film.setMpa(new Mpa(4, R));
            } else if (film.getMpa().getId() == 5) {
                film.setMpa(new Mpa(5, NC17));
            }
        }
    }
}
