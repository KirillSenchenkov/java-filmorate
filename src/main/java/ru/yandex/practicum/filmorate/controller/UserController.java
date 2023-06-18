package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserController(InMemoryUserStorage inMemoryUserStorage){
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    @GetMapping
    public List<User> userList() {
        return inMemoryUserStorage.allUsers();

    }

    @PostMapping
    public User createUser(@RequestBody User user) {
       return inMemoryUserStorage.create(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        return inMemoryUserStorage.update(user);
    }

    @DeleteMapping("/{filmId}")
    public String deleteUser(@PathVariable Integer filmId){
        return inMemoryUserStorage.delete(filmId);
    }


}
