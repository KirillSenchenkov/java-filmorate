package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.validator.Validator.validate;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private Integer id = 0;
    private final Map<Integer, Film> films = new HashMap<>();
    @Override
    public Film create(Film film) {
        if (films.containsKey(film.getId())) {
            log.debug("Фильм уже добавлен - " + films.get(film.getId()));
            throw new ValidationException("Фильм с таким Id уже добавлен");
        } else {
            validate(film);
            id++;
            film.setId(id);
            films.put(film.getId(), film);
            log.debug("добавлен новый фильм" + film);
            return film;
        }
    }

    @Override
    public Film update(Film film) {
        if (films.containsKey(film.getId())) {
            validate(film);
            films.put(film.getId(), film);
            log.debug("Фильм обновлен: " + film);
            return film;
        } else {
            log.debug("Id отсутствует в списке - " + film.getId());
            throw new ValidationException("Фильм с ID " + film.getId() + " отсутствует");
        }
    }

    @Override
    public String delete(Integer id) {
        if (films.containsKey(id)) {
            films.remove(id);
            log.debug(String.format("Фильм с ID %s удален", id));
            return String.format("Фильм с ID %s удален", id);
        }
        return String.format("Фильм с ID %s отсутствует", id);
    }

    @Override
    public List<Film> allFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getTargetFilm(Integer id){
        return films.get(id);
    }
}
