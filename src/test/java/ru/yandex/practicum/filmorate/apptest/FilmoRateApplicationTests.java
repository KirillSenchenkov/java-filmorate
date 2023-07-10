package ru.yandex.practicum.filmorate.apptest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {
    private final UserDbStorage userStorage;

    public User user = new User("Maikoo", "Heikoo", "sunbaked@list.ru",
            LocalDate.of(1991, 11, 18));

    @Test
    public void shouldFindUserById() {
        userStorage.create(user);
        Optional<User> userOptional = Optional.of(userStorage.getTargetUser(1));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                                .hasFieldOrPropertyWithValue("login", "Maikoo")
                                .hasFieldOrPropertyWithValue("name", "Heikoo")
                                .hasFieldOrPropertyWithValue("email", "sunbaked@list.ru")
                                .hasFieldOrPropertyWithValue("birthday",
                                        LocalDate.of(1991, 11, 18))
                );
    }
}