package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<String, User> users = new HashMap<>();
    @Override
    public User create(User user) {
        users.put(user.getEmail(), user);
        return user;
    }

    @Override
    public User update(User user) {
        if (users.containsKey(user.getEmail())){
            users.put(user.getEmail(), user);
            log.debug(String.format("Пользователь с адресом %s обновлен", user.getEmail()));
        } else {
            throw new ValidationException(String.format("Пользователь с адресом %s отсутствует в системе",
                    user.getEmail()));
        }
        return user;
    }

    @Override
    public void delete(String email) {
            users.remove(email);
            log.debug(String.format("Пользователь с адресом %s удален", email));
    }
}
