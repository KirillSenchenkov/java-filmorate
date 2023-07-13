package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserService {

    String addFriend(Integer id, Integer friendId);

    String deleteFriend(Integer id, Integer friendId);

    List<User> allUsersFriends(Integer id);

    List<User> allCommonFriends(Integer id, Integer otherId);

    User create(User user);

    User update(User user);

    String delete(Integer id);

    List<User> allUsers();

    User getTargetUser(Integer id);

    Map<Integer, User> getUsers();

}
