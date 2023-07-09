package ru.yandex.practicum.filmorate.storage.film;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.film.FilmSetExtractor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.service.film.FilmGenreModifier.modFilmGenres;
import static ru.yandex.practicum.filmorate.service.film.FilmMpaModifier.modFilmMpa;
import static ru.yandex.practicum.filmorate.validator.Validator.validate;

@Component
@Slf4j
@AllArgsConstructor
@Primary
public class FilmDbStorage implements FilmStorage {

    private static final String SQL_QUERY = "select f.id, f.name, f.description, f.releaseDate, f.duration, " +
            "m.id as mpa_id, m.name as mpa_name, g.id as genre_id, " +
            "g.name as genre_name, fl.user_id as user_like_id  " +
            "from films f " +
            "left join mpa m on f.mpa_id = m.id " +
            "left join film_genre fg ON f.id = fg.film_id " +
            "left join genre g on fg.genre_id = g.id " +
            "left join film_like fl on f.id = fl.film_id " +
            "WHERE f.id=?";
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film create(Film film) {
        validate(film);
        if (film.getMpa() != null) {
            modFilmGenres(film);
        }
        if (film.getGenres() != null) {
            modFilmMpa(film);
        }
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("films")
                .usingGeneratedKeyColumns("id");
        Number id;
        if (film.getMpa() != null) {
            Map<String, String> paramWithMpa = Map.of("name", film.getName(),
                    "description", film.getDescription(), "releaseDate", film.getReleaseDate().toString(),
                    "duration", film.getDuration().toString(), "mpa_id", film.getMpa().getId().toString());
            id = simpleJdbcInsert.executeAndReturnKey(paramWithMpa);
        } else {
            Map<String, String> paramWithoutMpa = Map.of("name", film.getName(),
                    "description", film.getDescription(), "releaseDate", film.getReleaseDate().toString(),
                    "duration", film.getDuration().toString());
            id = simpleJdbcInsert.executeAndReturnKey(paramWithoutMpa);
        }
        film.setId(id.intValue());
        Map<String, Integer> genreParam = new HashMap<>();
        List<Genre> genres = film.getGenres();
        for (Genre genre : genres) {
            genreParam.put("film_id", film.getId());
            genreParam.put("genre_id", genre.getId());
            SimpleJdbcInsert filmGenreTemplate = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                    .withTableName("film_genre");
            filmGenreTemplate.execute(genreParam);
        }
        return film;
    }

    @Override
    public Film update(Film film) {
        List<Film> films = jdbcTemplate.query(SQL_QUERY, new FilmSetExtractor(), film.getId());

        if (films.size() != 1) {
            throw new IncorrectIdException(String.format("Фильм с id %s отсутствует в системе",
                    film.getId()));
        }
        jdbcTemplate.update("update films set name=?, description=?, releaseDate=?, duration=?," +
                        " mpa_id=?, genres=? where id=?",
                film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getMpa().getId(), film.getId());
        return film;
    }
    @Override
    public String delete(Integer id) {
        jdbcTemplate.update("DELETE FROM films WHERE id=?", id);
        return String.format("Фильм с id %s удален", id);
    }

    @Override
    public List<Film> allFilms() {
        return jdbcTemplate.query("select f.id, f.name, f.description, f.releaseDate, f.duration, " +
                "m.id as mpa_id, m.name as mpa_name, g.id as genre_id," +
                "g.name as genre_name, fl.user_id as user_like_id " +
                "from films f " +
                "left join mpa m on f.mpa_id = m.id " +
                "left join  film_genre fg ON f.id = fg.film_id " +
                "left join genre g on fg.genre_id = g.id " +
                "left join film_like fl on f.id = fl.film_id  ;", new FilmSetExtractor());

    }

    @Override
    public Film getTargetFilm(Integer id) {
        List<Film> films = jdbcTemplate.query(SQL_QUERY, new FilmSetExtractor(), id);
        if (films.size() != 1) {
            throw new IncorrectIdException(String.format("Фильм с id %s отсутствует в системе", id));
        }
        return films.get(0);
    }

/*    public RowMapper<Film> filmRowMapper() {
        return (rs, rowNum) -> {
            Film film = new Film(
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDate("releaseDate").toLocalDate(),
                    rs.getInt("duration")
            );
            film.setId(rs.getInt("id"));
            film.setMpa(new Mpa(rs.getInt("mpa_id"), rs.getString("mpa_name")));
            do {
                Genre genre = new Genre(rs.getInt("genre_id"),
                        rs.getString("genre_name"));
                film.getGenres().add(genre);
            } while (rs.next());
            do {
                film.addLike(rs.getInt("user_like_id"));
            } while (rs.next());
            return film;
        };
    }*/
}
