package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {

    String addFriend(Integer id, Integer friendId);

    String deleteFriend(Integer id, Integer friendId);

    List<User> allUsersFriends(Integer id);

    List<User> allCommonFriends(Integer id, Integer otherId);

}
