package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidatorTest {
    private static final LocalDate startDate = LocalDate.of(1895, 12, 28);
    private ValidationException exception;

    private static final UserStorage userStorage = new InMemoryUserStorage();
    private static final UserService userService = new UserService(userStorage);
    private static final FilmStorage filmStorage = new InMemoryFilmStorage();
    private static final FilmService filmService = new FilmService(filmStorage, userStorage);

    public static UserController userController;
    public static FilmController filmController;
    public User user = new User("sunbaked@list.ru", "Maikoo", "Heikoo",
            LocalDate.of(1991, 11, 18));
    public Film film = new Film("Film", "Film",
            LocalDate.of(2000, 11, 11), 100);

    @BeforeAll
    public static void beforeAll() {
        userController = new UserController(userStorage, userService);
        filmController = new FilmController(filmStorage, filmService);
    }

    @ParameterizedTest(name = "параметризованный тест c Email {index} - {0}")
    @ValueSource(strings = "sunbaked")
    @NullAndEmptySource
    void shouldThrownExceptionForIncorrectUSerEmail(String arg) {
        user.setEmail(arg);
        exception = assertThrows(ValidationException.class, (() -> userController.createUser(user)));
        assertEquals("Email адрес указан не верно", exception.getMessage());
    }

    @ParameterizedTest(name = "параметризованный тест c Login {index} - {0}")
    @ValueSource(strings = "Maikoo Heikoo")
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
        assertEquals("Дата создания не может быть раньше " + startDate, exception.getMessage());
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