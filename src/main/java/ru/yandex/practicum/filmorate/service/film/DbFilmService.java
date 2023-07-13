package ru.yandex.practicum.filmorate.service.film;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DbFilmService implements FilmService {

    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public String addLike(Integer filmId, Integer userId) {
        Film film = filmStorage.getTargetFilm(filmId);
        User user = userStorage.getTargetUser(userId);
        Integer count = jdbcTemplate
                .queryForObject("SELECT COUNT(*) FROM film_like WHERE film_id = ? AND user_id = ?",
                        Integer.class, film.getId(), user.getId());
        if (count != null && count == 0) {
            jdbcTemplate.update("INSERT INTO film_like (film_id, user_id) VALUES (?, ?)", filmId, userId);
            return String.format("Пользователь с Id %s, поставил лайк фильму %s.", userId, film.getName());
        } else if (count != null && count >= 1) {
            return String.format("Пользователь с Id %s, уже ставил лайк фильму %s.", userId, film.getName());
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
        String sql = "select f.id, f.name, f.description, f.releaseDate, f.duration, m.id as mpa_id, " +
                "m.name as mpa_name, g.id as genre_id,g.name as genre_name, " +
                "fl.user_id as user_like_id " +
                "from films f  " +
                "left join mpa m on f.mpa_id = m.id  " +
                "left join film_genre fg ON f.id = fg.film_id " +
                "left join genre g on fg.genre_id = g.id " +
                "left join film_like fl on f.id = fl.film_id " +
                "WHERE f.id IN (SELECT f.id  AS likes_count FROM FILMS f  LEFT JOIN FILM_LIKE fl ON f.id = film_id " +
                "GROUP BY f.ID " +
                "ORDER BY COUNT(fl.USER_ID) DESC " +
                "LIMIT ?)";
        List<Film> films = jdbcTemplate.query(sql, new FilmSetExtractor(), count);
        return films.stream().sorted((film1, film2) -> {
                    int comp = film1.getLikes().size() - film2.getLikes().size();
                    comp = -1 * comp;
                    return comp;
                })
                .limit(count).collect(Collectors.toList());
    }

    @Override
    public Mpa getTargetMpa(Integer mpaId) {
        return filmStorage.getTargetMpa(mpaId);
    }

    @Override
    public List<Mpa> getAllMpa() {
        return filmStorage.getAllMpa();
    }

    @Override
    public Genre getTargetGenre(Integer genreId) {
        return filmStorage.getTargetGenre(genreId);
    }

    @Override
    public List<Genre> getAllGenres() {
        return filmStorage.getAllGenres();
    }

    @Override
    public Film create(Film film) {
        return filmStorage.create(film);
    }

    @Override
    public Film update(Film film) {
        return filmStorage.update(film);
    }

    @Override
    public String delete(Integer id) {
        return filmStorage.delete(id);
    }

    @Override
    public List<Film> allFilms() {
        return filmStorage.allFilms();
    }

    @Override
    public Film getTargetFilm(Integer id) {
        return filmStorage.getTargetFilm(id);
    }
}
