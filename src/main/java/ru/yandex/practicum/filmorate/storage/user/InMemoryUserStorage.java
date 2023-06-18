package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.validator.Validator.validate;
@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private Integer id=0;
    private final Map<Integer, User> users = new HashMap<>();
    @Override
    public User create(User user) {
        validate(user);
        id++;
        user.setId(id);
        if (user.getName() == null || user.getName().isBlank()){
            user.setName(user.getLogin());
        }
        users.put(id, user);
        return user;
    }

    @Override
    public User update(User user) {
        validate(user);
        if (users.containsKey(user.getId())){
            users.put(user.getId(), user);
            log.debug(String.format("Пользователь с адресом %s обновлен", user.getEmail()));
        } else {
            throw new ValidationException(String.format("Пользователь с адресом %s отсутствует в системе",
                    user.getEmail()));
        }
        return user;
    }

    @Override
    public String delete(Integer id) {
        if (users.containsKey(id)) {
            users.remove(id);
            log.debug(String.format("Пользователь с адресом %s удален", id));
            return String.format("Пользователь с ID %s удален", id);
        }
        return String.format("Пользователь с ID %s отсутствует", id);
    }

    @Override
    public List<User> allUsers() {
        return new ArrayList<>(users.values());
    }

    public User getTargetUser(Integer id){
        return users.get(id);
    }
}
