package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();
    @Override
    public Film create(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (films.containsKey(film.getId())){
            films.put(film.getId(), film);
            log.debug(String.format("Фильм с идентификатором %s обновлен", film.getId()));
        } else {
            throw new ValidationException(String.format("Фильм с ID %s не найден", film.getId()));
        }
        return film;
    }

    @Override
    public void delete(Integer id) {
        films.remove(id);
        log.debug(String.format("Фильм с ID %s удален", id));
    }
}
