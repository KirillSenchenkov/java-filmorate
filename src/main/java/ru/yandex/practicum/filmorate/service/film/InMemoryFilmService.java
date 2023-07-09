package ru.yandex.practicum.filmorate.service.film;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InMemoryFilmService implements FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Override
    public String addLike(Integer filmId, Integer userId) {
        Film film = filmStorage.getTargetFilm(filmId);
        List<User> users = userStorage.allUsers();
        for (User user : users) {
            if (user.getId() == userId) {
                film.addLike(userId);
                return String.format("Пользователь с Id %s, поставил лайк фильму %s.", userId, film.getName());
            }
        }
        return String.format("Пользователь с Id %s отсутствует в списке", userId);
    }

    @Override
    public String deleteLike(Integer filmId, Integer userId) {
        Film film = filmStorage.getTargetFilm(filmId);
        if (film.getLikes().contains(userId)) {
            film.deleteLike(userId);
            return String.format("Лайк пользователя c Id %s удален с фильма %s", userId, film.getName());
        }
        throw new IncorrectIdException(String.format("Лайк пользователя с Id %s не найден", userId));
    }

    @Override
    public List<Film> getFilmByPopularity(Integer count) {
        List<Film> films = filmStorage.allFilms();
        return films.stream().sorted((film1, film2) -> {
                    int comp = film1.getLikes().size() - film2.getLikes().size();
                    comp = -1 * comp;
                    return comp;
                })
                .limit(count).collect(Collectors.toList());
    }

}
