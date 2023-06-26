package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserStorage {
    User create(User user);

    User update(User user);

    String delete(Integer id);

    List<User> allUsers();

    User getTargetUser(Integer id);

    Map<Integer, User> getUsers();
}
