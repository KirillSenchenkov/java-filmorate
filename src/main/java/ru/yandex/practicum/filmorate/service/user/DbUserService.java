package ru.yandex.practicum.filmorate.service.user;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Primary
public class DbUserService implements UserService {

    private final UserStorage userStorage;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public String addFriend(Integer id, Integer friendId) {
        if (userStorage.getUsers().containsKey(id) && userStorage.getUsers().containsKey(friendId)) {
            userStorage.getTargetUser(id).addFriend(friendId);
            jdbcTemplate.update("INSERT INTO USER_FRIEND (USER_ID, FRIEND_ID) VALUES (?, ?)",
                    id, friendId);
            return String.format("Пользователь %s теперь дружит с пользователем %s ",
                    userStorage.getTargetUser(id).getName(),
                    userStorage.getTargetUser(friendId).getName());
        }
        throw new IncorrectIdException("Один из идентификаторов не верный");
    }

    @Override
    public String deleteFriend(Integer id, Integer friendId) {
        if (userStorage.getTargetUser(id).getFriends().contains(friendId)) {
            jdbcTemplate.update("DELETE FROM user_friend WHERE user_id=? AND friend_id=?", id, friendId);

            return String.format("Пользователь %s больше не дружит с пользователем %s",
                    userStorage.getTargetUser(id).getName(),
                    userStorage.getTargetUser(friendId).getName());
        }
        return "Пользователи не друзья";
    }

    @Override
    public List<User> allUsersFriends(Integer id) {
        String sql = "SELECT u.id, u.login, u.name, u.email, u.birthday, uf.friend_id FROM users u " +
                "LEFT JOIN user_friend uf ON u.id = uf.user_id " +
                "WHERE u.id IN (SELECT friend_id FROM USER_FRIEND WHERE user_id = ?)";
        return jdbcTemplate.query(sql, new UserSetExtractor(), id);
    }

    @Override
    public List<User> allCommonFriends(Integer id, Integer otherId) {
        String sql = "SELECT u.id, u.login, u.name, u.email, u.birthday, uf.friend_id FROM users u " +
                "LEFT JOIN user_friend uf ON u.id = uf.user_id " +
                "WHERE u.ID IN (SELECT friend_id FROM USER_FRIEND " +
                "WHERE user_id = ? AND friend_id IN (SELECT friend_id FROM USER_FRIEND WHERE user_id = ?))";
        return jdbcTemplate.query(sql, new UserSetExtractor(), id, otherId);
    }

    @Override
    public User create(User user) {
        return userStorage.create(user);
    }

    @Override
    public User update(User user) {
        return userStorage.update(user);
    }

    @Override
    public String delete(Integer id) {
        return userStorage.delete(id);
    }

    @Override
    public List<User> allUsers() {
        return userStorage.allUsers();
    }

    @Override
    public User getTargetUser(Integer id) {
        return userStorage.getTargetUser(id);
    }

    @Override
    public Map<Integer, User> getUsers() {
        return userStorage.getUsers();
    }
}
