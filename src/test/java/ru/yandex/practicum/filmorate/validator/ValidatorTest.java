package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidatorTest {
    private static InMemoryUserStorage inMemoryUserStorage;
    private static InMemoryFilmStorage inMemoryFilmStorage;
    private static final LocalDate startDate = LocalDate.of(1895, 12, 28);
    private ValidationException exception;
    public static UserController userController;
    public static FilmController filmController;
    public User user = new User(1, "sunbaked@list.ru", "Maikoo", "Heikoo",
            LocalDate.of(1991, 11, 18));
    public Film film = new Film(1, "Film", "Film",
            LocalDate.of(2000, 11, 11), 100);

    @BeforeAll
    public static void beforeAll() {
        userController = new UserController(inMemoryUserStorage);
        filmController = new FilmController(inMemoryFilmStorage);
    }

    @Test
    void shouldThrownExceptionForIncorrectUSerEmail() {
        user.setEmail("");
        exception = assertThrows(ValidationException.class, (() -> userController.createUser(user)));
        assertEquals("Email адрес указан не верно", exception.getMessage());
        user.setEmail(null);
        exception = assertThrows(ValidationException.class, (() -> userController.createUser(user)));
        assertEquals("Email адрес указан не верно", exception.getMessage());
        user.setEmail("sunbaked");
        exception = assertThrows(ValidationException.class, (() -> userController.createUser(user)));
        assertEquals("Email адрес указан не верно", exception.getMessage());
    }

    @Test
    void shouldThrownExceptionForIncorrectUserLogin() {
        user.setLogin("");
        exception = assertThrows(ValidationException.class, (() -> userController.createUser(user)));
        assertEquals("Логин не может быть пустым или содержать в себе пробелы", exception.getMessage());
        user.setLogin(null);
        exception = assertThrows(ValidationException.class, (() -> userController.createUser(user)));
        assertEquals("Логин не может быть пустым или содержать в себе пробелы", exception.getMessage());
        user.setLogin("Maikoo Heikoo");
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

    @Test
    void shouldThrownExceptionForIncorrectFilmName() {
        film.setName("");
        exception = assertThrows(ValidationException.class, (() -> filmController.createFilm(film)));
        assertEquals("Название фильма не может быть пустым", exception.getMessage());
        film.setName(null);
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