package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
public class Validator {
    private static final LocalDate startDate = LocalDate.of(1895, 12, 28);

    public static void validate(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.debug("Email адрес указан не верно: " + user.getEmail());
            throw new ValidationException("Email адрес указан не верно");
        } else if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.debug("Не верное написание логина: " + user.getLogin());
            throw new ValidationException("Логин не может быть пустым или содержать в себе пробелы");
        } else if (user.getBirthday() == null) {
            log.debug("Пользователь не указал дату рождения");
            throw new ValidationException("Не указана дата рождения");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("Не верная дата рождения: " + user.getBirthday());
            throw new ValidationException("Дата рождения не может быть в будущем");
        } else {
            log.debug("Проверка пользователя пройдена");
        }
    }

    public static void validate(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.debug("Пустое название фильма");
            throw new ValidationException("Название фильма не может быть пустым");
        } else if (film.getReleaseDate() == null) {
            log.debug("Не задана дата выпуска фильма");
            throw new ValidationException("Дата создания не может пустой");
        } else if (film.getReleaseDate().isBefore(startDate)) {
            log.debug("Дата фильма " + film.getReleaseDate() + " раньше чем " + startDate);
            throw new ValidationException("Дата создания не может быть раньше " + startDate);
        } else if (film.getDescription() == null || film.getDescription().length() > 200) {
            log.debug("Описание превышает максимальное количество символов: " + film.getDescription());
            throw new ValidationException("Описание не может превышать 200 символов");
        } else if (film.getDuration() == null) {
            log.debug("Продолжительность фильма не задана");
            throw new ValidationException("Продолжительность фильма должна быть задана");
        } else if (film.getDuration() <= 0) {
            log.debug("Продолжительность фильма отрицательная: " + film.getDuration());
            throw new ValidationException("Продолжительность фильма не может быть отрицательной");
        } else {
            log.debug("Проверка фильма пройдена");

        }
    }

}
