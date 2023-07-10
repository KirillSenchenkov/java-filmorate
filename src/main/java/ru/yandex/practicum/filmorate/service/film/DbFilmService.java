package ru.yandex.practicum.filmorate.service.film;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Primary
public class DbFilmService implements FilmService {

    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public String addLike(Integer filmId, Integer userId) {
        Film film = filmStorage.getTargetFilm(filmId);
        List<User> users = userStorage.allUsers();
        for (User user : users) {
            if (user.getId() == userId) {
                Integer count = jdbcTemplate
                        .queryForObject("SELECT COUNT(*) FROM film_like WHERE film_id = ? AND user_id = ?",
                                Integer.class, film.getId(), userId);
                if (count != null && count == 0) {
                    jdbcTemplate.update("INSERT INTO film_like (film_id, user_id) VALUES (?, ?)", filmId, userId);
                    return String.format("Пользователь с Id %s, поставил лайк фильму %s.", userId, film.getName());
                }

                return String.format("Пользователь с Id %s, уже ставил лайк фильму %s.", userId, film.getName());
            }
        }
        return String.format("Пользователь с Id %s отсутствует в списке", userId);
    }

    @Override
    public String deleteLike(Integer filmId, Integer userId) {
        Film film = filmStorage.getTargetFilm(filmId);
        if (film.getLikes().contains(userId)) {
            film.deleteLike(userId);
            jdbcTemplate.update("DELETE FROM film_like WHERE film_id =? AND user_id =?", filmId, userId);
            return String.format("Лайк пользователя c Id %s удален с фильма %s", userId, film.getName());
        }
        throw new IncorrectIdException(String.format("Лайк пользователя с Id %s не найден", userId));
    }

    @Override
    public List<Film> getFilmByPopularity(Integer count) {
        List<Film> films = filmStorage.allFilms();
        return films.stream().sorted((film1, film2) -> {
                    int comp = film1.getLikes().size() - film2.getLikes().size();
                    comp = -1 * comp;
                    return comp;
                })
                .limit(count).collect(Collectors.toList());
    }
}
