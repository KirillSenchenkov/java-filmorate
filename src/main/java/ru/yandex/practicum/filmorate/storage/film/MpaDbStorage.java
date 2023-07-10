package ru.yandex.practicum.filmorate.storage.film;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Component
@AllArgsConstructor
public class MpaDbStorage {

    private final JdbcTemplate jdbcTemplate;

    public List<Mpa> getAllMpa() {
        return jdbcTemplate.query("SELECT * FROM mpa;", (rs, rowNum) -> new Mpa(
                rs.getInt("id"),
                rs.getString("name")
        ));
    }

    public Mpa getTargetMpa(Integer mpaId) {
        List<Mpa> mpa = jdbcTemplate.query("SELECT * FROM mpa WHERE id=?", (rs, rowNum) -> new Mpa(
                rs.getInt("id"),
                rs.getString("name")
        ), mpaId);
        if (mpa.size() != 1) {
            throw new IncorrectIdException("Не верный идентификатор Mpa");
        }
        return mpa.get(0);
    }
}
