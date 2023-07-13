package ru.yandex.practicum.filmorate.validator;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ValidatorTest {
    private static final LocalDate START_DATE = LocalDate.of(1895, 12, 28);
    public final UserService userService;
    public final FilmService filmService;
    public User user = new User("Maikoo", "Heikoo", "sunbaked@list.ru",
            LocalDate.of(1991, 11, 18));
    public Film film = new Film("Film", "Film",
            LocalDate.of(2000, 11, 11), 100);
    private ValidationException exception;

    @ParameterizedTest(name = "параметризованный тест c Email {index} - {0}")
    @ValueSource(strings = "not_valid_email")
    @NullAndEmptySource
    void shouldThrownExceptionForIncorrectUSerEmail(String arg) {
        user.setEmail(arg);
        exception = assertThrows(ValidationException.class, (() -> userService.create(user)));
        assertEquals("Email адрес указан не верно", exception.getMessage());
    }

    @ParameterizedTest(name = "параметризованный тест c Login {index} - {0}")
    @ValueSource(strings = "name with space")
    @NullAndEmptySource
    void shouldThrownExceptionForIncorrectUserLogin(String arg) {
        user.setLogin(arg);
        exception = assertThrows(ValidationException.class, (() -> userService.create(user)));
        assertEquals("Логин не может быть пустым или содержать в себе пробелы", exception.getMessage());
    }

    @Test
    void shouldThrownExceptionForIncorrectUserBirthDate() {
        user.setBirthday(null);
        exception = assertThrows(ValidationException.class, (() -> userService.create(user)));
        assertEquals("Не указана дата рождения", exception.getMessage());
        user.setBirthday(LocalDate.of(2100, 11, 11));
        exception = assertThrows(ValidationException.class, (() -> userService.create(user)));
        assertEquals("Дата рождения не может быть в будущем", exception.getMessage());
    }

    @ParameterizedTest(name = "параметризованный тест c Name {index} - {0}")
    @NullAndEmptySource
    void shouldThrownExceptionForIncorrectFilmName(String arg) {
        film.setName(arg);
        exception = assertThrows(ValidationException.class, (() -> filmService.create(film)));
        assertEquals("Название фильма не может быть пустым", exception.getMessage());
    }

    @Test
    void shouldThrownExceptionForIncorrectFilmReleaseDate() {
        film.setReleaseDate(null);
        exception = assertThrows(ValidationException.class, (() -> filmService.create(film)));
        assertEquals("Дата создания не может пустой", exception.getMessage());
        film.setReleaseDate(LocalDate.of(1000, 11, 11));
        exception = assertThrows(ValidationException.class, (() -> filmService.create(film)));
        assertEquals("Дата создания не может быть раньше " + START_DATE, exception.getMessage());
    }

    @Test
    void shouldThrownExceptionForIncorrectFilmDescription() {
        film.setDescription("a".repeat(201));
        exception = assertThrows(ValidationException.class, (() -> filmService.create(film)));
        assertEquals("Описание не может превышать 200 символов", exception.getMessage());
    }

    @Test
    void shouldThrownExceptionForIncorrectFilmDuration() {
        film.setDuration(-1);
        exception = assertThrows(ValidationException.class, (() -> filmService.create(film)));
        assertEquals("Продолжительность фильма не может быть отрицательной", exception.getMessage());
        film.setDuration(null);
        exception = assertThrows(ValidationException.class, (() -> filmService.create(film)));
        assertEquals("Продолжительность фильма должна быть задана", exception.getMessage());
    }
}