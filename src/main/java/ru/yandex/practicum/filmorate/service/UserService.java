package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public String addFriend(Integer id, Integer friendId) {
        if (userStorage.getUsers().containsKey(id) && userStorage.getUsers().containsKey(friendId)) {
            userStorage.getTargetUser(id).addFriend(friendId);
            userStorage.getTargetUser(friendId).addFriend(id);
            return String.format("Пользователи %s и %s теперь друзья",
                    userStorage.getTargetUser(id).getName(),
                    userStorage.getTargetUser(friendId).getName());
        }
        return "Один из пользователей отсутствует в списке";
    }

    public String deleteFriend(Integer id, Integer friendId) {
        if (userStorage.getTargetUser(id).getFriends().contains(friendId)
                && userStorage.getTargetUser(friendId).getFriends().contains(id)) {
            userStorage.getTargetUser(id).deleteFriend(friendId);
            userStorage.getTargetUser(friendId).deleteFriend(id);
            return String.format("Пользователи %s и %s больше не друзья",
                    userStorage.getTargetUser(id).getName(),
                    userStorage.getTargetUser(friendId).getName());
        }
        return "Пользователи не друзья";
    }

    public List<String> allUsersFriends(Integer id) {
        Set<Integer> friends = userStorage.getTargetUser(id).getFriends();
        return friends.stream().map(friendId -> userStorage.getTargetUser(friendId).getName())
                .collect(Collectors.toList());

    }

    public List<String> allCommonFriends(Integer id, Integer otherId) {
        Set<Integer> userFriends = userStorage.getTargetUser(id).getFriends();
        Set<Integer> targetFriends = userStorage.getTargetUser(otherId).getFriends();
        return userFriends.stream().filter(targetFriends::contains)
                .map(friendId -> userStorage.getTargetUser(friendId).getName())
                .collect(Collectors.toList());
    }
}
