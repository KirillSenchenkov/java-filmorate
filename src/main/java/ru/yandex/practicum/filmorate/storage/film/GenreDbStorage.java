package ru.yandex.practicum.filmorate.storage.film;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Component
@AllArgsConstructor
public class GenreDbStorage {

    private final JdbcTemplate jdbcTemplate;

    public List<Genre> getAllGenre() {
        return jdbcTemplate.query("SELECT * FROM genre", (rs, rowNum) -> new Genre (rs.getInt("id"),
                rs.getString("name") ));
    }

    public Genre getTargetGenre(Integer genreId){
        List<Genre> genres = jdbcTemplate.query("SELECT * FROM genre WHERE id=?", (rs, rowNum) -> new Genre (rs.getInt("id"),
                rs.getString("name") ), genreId);
        if (genres.size() != 1) {
            throw new IncorrectIdException("Не верный идентификатор Жанра");
        }
        return genres.get(0);
    }
}
