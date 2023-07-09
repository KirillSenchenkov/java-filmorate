package ru.yandex.practicum.filmorate.service.film;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilmSetExtractor implements ResultSetExtractor<List<Film>> {
    @Override
    public List<Film> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Integer, Film> filmMap = new HashMap<>();

        while (rs.next()) {
            Film film = new Film(
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDate("releaseDate").toLocalDate(),
                    rs.getInt("duration")
            );
            film.setId(rs.getInt("id"));
            film.setMpa(new Mpa(rs.getInt("mpa_id"), rs.getString("mpa_name")));
            film.getGenres().add(new Genre(rs.getInt("genre_id"), rs.getString("genre_name")));
            if (filmMap.containsKey(rs.getInt("id"))) {
                filmMap.get(rs.getInt("id")).getGenres()
                        .add(new Genre(rs.getInt("genre_id"), rs.getString("genre_name")));
            } else if (!filmMap.containsKey(rs.getInt("id"))){
                filmMap.put(rs.getInt("id"), film);
            }
        }
        return new ArrayList<>(filmMap.values());
    }
}
