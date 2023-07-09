package ru.yandex.practicum.filmorate.service.user;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Primary
public class DbUserService implements UserService {

    private final FilmStorage filmStorage;
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
        Set<Integer> friends = userStorage.getTargetUser(id).getFriends();
        return friends.stream().map(userStorage::getTargetUser)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> allCommonFriends(Integer id, Integer otherId) {
        Set<Integer> userFriends = userStorage.getTargetUser(id).getFriends();
        Set<Integer> targetFriends = userStorage.getTargetUser(otherId).getFriends();
        return userFriends.stream().filter(targetFriends::contains)
                .map(userStorage::getTargetUser)
                .collect(Collectors.toList());
    }
}
