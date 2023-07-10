package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.film.InMemoryFilmService;
import ru.yandex.practicum.filmorate.service.user.InMemoryUserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidatorTest {
    private static final LocalDate START_DATE = LocalDate.of(1895, 12, 28);
    private ValidationException exception;

    public UserStorage userStorage = new InMemoryUserStorage();
    private final InMemoryUserService userService = new InMemoryUserService(userStorage);
    private final FilmStorage filmStorage = new InMemoryFilmStorage();
    private final InMemoryFilmService filmService = new InMemoryFilmService(filmStorage, userStorage);

    public final UserController userController = new UserController(userStorage, userService);
    public final FilmController filmController = new FilmController(filmStorage, filmService);
    public User user = new User("Maikoo", "Heikoo", "sunbaked@list.ru",
            LocalDate.of(1991, 11, 18));
    public Film film = new Film("Film", "Film",
            LocalDate.of(2000, 11, 11), 100);

    @ParameterizedTest(name = "параметризованный тест c Email {index} - {0}")
    @ValueSource(strings = "not_valid_email")
    @NullAndEmptySource
    void shouldThrownExceptionForIncorrectUSerEmail(String arg) {
        user.setEmail(arg);
        exception = assertThrows(ValidationException.class, (() -> userController.createUser(user)));
        assertEquals("Email адрес указан не верно", exception.getMessage());
    }

    @ParameterizedTest(name = "параметризованный тест c Login {index} - {0}")
    @ValueSource(strings = "name with space")
    @NullAndEmptySource
    void shouldThrownExceptionForIncorrectUserLogin(String arg) {
        user.setLogin(arg);
        exception = assertThrows(ValidationException.class, (() -> userController.createUser(user)));
        assertEquals("Логин не может быть пустым или содержать в себе пробелы", exception.getMessage());
    }

    @Test
    void shouldThrownExceptionForIncorrectUserBirthDate() {
        user.setBirthday(null);
        exception = assertThrows(ValidationException.class, (() -> userController.createUser(user)));
        assertEquals("Не указана дата рождения", exception.getMessage());
        user.setBirthday(LocalDate.of(2100, 11, 11));
        exception = assertThrows(ValidationException.class, (() -> userController.createUser(user)));
        assertEquals("Дата рождения не может быть в будущем", exception.getMessage());
    }

    @ParameterizedTest(name = "параметризованный тест c Name {index} - {0}")
    @NullAndEmptySource
    void shouldThrownExceptionForIncorrectFilmName(String arg) {
        film.setName(arg);
        exception = assertThrows(ValidationException.class, (() -> filmController.createFilm(film)));
        assertEquals("Название фильма не может быть пустым", exception.getMessage());
    }

    @Test
    void shouldThrownExceptionForIncorrectFilmReleaseDate() {
        film.setReleaseDate(null);
        exception = assertThrows(ValidationException.class, (() -> filmController.createFilm(film)));
        assertEquals("Дата создания не может пустой", exception.getMessage());
        film.setReleaseDate(LocalDate.of(1000, 11, 11));
        exception = assertThrows(ValidationException.class, (() -> filmController.createFilm(film)));
        assertEquals("Дата создания не может быть раньше " + START_DATE, exception.getMessage());
    }

    @Test
    void shouldThrownExceptionForIncorrectFilmDescription() {
        film.setDescription("a".repeat(201));
        exception = assertThrows(ValidationException.class, (() -> filmController.createFilm(film)));
        assertEquals("Описание не может превышать 200 символов", exception.getMessage());
    }

    @Test
    void shouldThrownExceptionForIncorrectFilmDuration() {
        film.setDuration(-1);
        exception = assertThrows(ValidationException.class, (() -> filmController.createFilm(film)));
        assertEquals("Продолжительность фильма не может быть отрицательной", exception.getMessage());
        film.setDuration(null);
        exception = assertThrows(ValidationException.class, (() -> filmController.createFilm(film)));
        assertEquals("Продолжительность фильма должна быть задана", exception.getMessage());
    }
}