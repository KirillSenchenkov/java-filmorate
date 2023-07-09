package ru.yandex.practicum.filmorate.service.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InMemoryUserService implements UserService {

    private final UserStorage userStorage;

    @Override
    public String addFriend(Integer id, Integer friendId) {
        if (userStorage.getUsers().containsKey(id) && userStorage.getUsers().containsKey(friendId)) {
            userStorage.getTargetUser(id).addFriend(friendId);
            return String.format("Пользователь %s теперь дружит с пользователем %s ",
                    userStorage.getTargetUser(id).getName(),
                    userStorage.getTargetUser(friendId).getName());
        }
        throw new IncorrectIdException("Один из идентификаторов не верный");
    }

    @Override
    public String deleteFriend(Integer id, Integer friendId) {
        if (userStorage.getTargetUser(id).getFriends().contains(friendId)
                && userStorage.getTargetUser(friendId).getFriends().contains(id)) {
            userStorage.getTargetUser(id).deleteFriend(friendId);
            return String.format("Пользователи %s и %s больше не друзья",
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
