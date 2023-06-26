package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserStorage userStorage;
    private final UserService userService;

    @GetMapping
    public List<User> userList() {
        return userStorage.allUsers();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userStorage.create(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        return userStorage.update(user);
    }

    @GetMapping("{userId}")
    public User getUserById(@PathVariable Integer userId) {
        return userStorage.getTargetUser(userId);
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable Integer userId) {
        return userStorage.delete(userId);
    }

    @PutMapping("{userId}/friends/{friendId}")
    public String addFriend(@PathVariable Integer userId, @PathVariable Integer friendId) {
        return userService.addFriend(userId, friendId);
    }

    @DeleteMapping("{userId}/friends/{friendId}")
    public String deleteFriend(@PathVariable Integer userId, @PathVariable Integer friendId) {
        return userService.deleteFriend(userId, friendId);
    }

    @GetMapping("{userId}/friends")
    public List<User> allUsersFriends(@PathVariable Integer userId) {
        return userService.allUsersFriends(userId);
    }

    @GetMapping("{userId}/friends/common/{otherId}")
    public List<User> allCommonFriend(@PathVariable Integer userId, @PathVariable Integer otherId) {
        return userService.allCommonFriends(userId, otherId);
    }
}
