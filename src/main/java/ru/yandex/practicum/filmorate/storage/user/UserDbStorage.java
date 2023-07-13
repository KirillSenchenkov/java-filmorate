package ru.yandex.practicum.filmorate.storage.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserSetExtractor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static ru.yandex.practicum.filmorate.validator.Validator.validate;

@Component
@Slf4j
@AllArgsConstructor
@Primary
public class UserDbStorage implements UserStorage {
    private static final String SQL_QUERY = "SELECT u.id, u.login, u.name, u.email, u.birthday, uf.friend_id " +
            "FROM users u Left JOIN user_friend uf ON u.id = uf.user_id";
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User create(User user) {
        validate(user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        Map<String, String> param = Map.of("login", user.getLogin(), "name", user.getName(),
                "email", user.getEmail(), "birthday", user.getBirthday().toString());
        Number id = simpleJdbcInsert.executeAndReturnKey(param);
        user.setId(id.intValue());
        return user;
    }

    @Override
    public User update(User user) {
        List<User> users = jdbcTemplate
                .query(SQL_QUERY + " where id=?", new UserSetExtractor(), user.getId());
        if (users != null && users.size() != 1) {
            throw new IncorrectIdException(String.format("Пользователь с id %s отсутствует в системе",
                    user.getId()));
        }
        jdbcTemplate.update("update users set login=?, name=?, email=?, birthday=? where id=?",
                user.getLogin(), user.getName(), user.getEmail(), user.getBirthday(), user.getId());
        Set<Integer> friendsId = user.getFriends();
        for (Integer friendId : friendsId) {
            Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM user_friend WHERE user_id=? AND friend_id=?",
                    Integer.class, user.getId(), friendId);
            if (count != null && count == 0) {
                jdbcTemplate.update("insert into user_friend (user_id, friend_id) VALUES (?, ?)",
                        user.getId(), friendId);
            }
        }
        return user;
    }

    @Override
    public String delete(Integer id) {
        jdbcTemplate.update("DELETE FROM users WHERE id=?", id);
        return String.format("Пользователь с id %s удален", id);
    }

    @Override
    public List<User> allUsers() {
        return jdbcTemplate.query(SQL_QUERY, new UserSetExtractor());
    }

    @Override
    public User getTargetUser(Integer id) {
        List<User> users = jdbcTemplate.query(SQL_QUERY + " WHERE u.id =?", new UserSetExtractor(), id);
        if (users != null && users.size() != 1) {
            throw new IncorrectIdException(String.format("Пользователь с id %s отсутствует в системе",
                    id));
        }
        return users.get(0);
    }

    @Override
    public Map<Integer, User> getUsers() {
        Map<Integer, User> users = new HashMap<>();
        List<User> usersList = allUsers();
        for (User user : usersList) {
            users.put(user.getId(), user);
        }
        return users;
    }


}
