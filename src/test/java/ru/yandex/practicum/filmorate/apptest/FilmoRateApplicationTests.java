package ru.yandex.practicum.filmorate.apptest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {
    private final UserService userService;
    private final FilmService filmService;

    public User user = new User("Maikoo", "Heikoo", "sunbaked@list.ru",
            LocalDate.of(1991, 11, 18));
    public Film film = new Film("Film", "Film",
            LocalDate.of(2000, 11, 11), 100);


    @Test
    @DirtiesContext
    public void shouldFindUserById() {
        userService.create(user);
        Optional<User> userOptional = Optional.of(userService.getTargetUser(1));

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

    @Test
    @DirtiesContext
    public void shouldShowAllUsers() {
        userService.create(user);
        user.setName("MaikooNext");
        userService.create(user);
        Optional<List<User>> usersList = Optional.of(userService.allUsers());
        assertThat(usersList)
                .isPresent()
                .hasValueSatisfying(users ->
                        assertThat(users).hasFieldOrPropertyWithValue("size", 2)
                );

    }

    @Test
    @DirtiesContext
    public void shouldUpdateUser() {
        userService.create(user);
        User updateUser = userService.getTargetUser(1);
        updateUser.setLogin("UpdatedLogin");
        updateUser.setName("UpdatedName");
        updateUser.setEmail("update@update");
        updateUser.setBirthday(LocalDate.of(2000, 11, 18));
        userService.update(updateUser);
        Optional<User> updatedUser = Optional.of(userService.getTargetUser(1));
        assertThat(updatedUser)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("login", "UpdatedLogin")
                                .hasFieldOrPropertyWithValue("name", "UpdatedName")
                                .hasFieldOrPropertyWithValue("email", "update@update")
                                .hasFieldOrPropertyWithValue("birthday",
                                        LocalDate.of(2000, 11, 18))
                );
    }

    @Test
    @DirtiesContext
    public void shouldDeleteUser() {
        userService.create(user);
        Optional<User> userOptional = Optional.of(userService.getTargetUser(1));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
        userService.delete(user.getId());
        Optional<List<User>> usersList = Optional.of(userService.allUsers());
        assertThat(usersList)
                .isPresent()
                .hasValueSatisfying(users ->
                        assertThat(users).hasFieldOrPropertyWithValue("size", 0)
                );
    }

    @Test
    @DirtiesContext
    public void shouldAddAndDeleteFriend() {
        userService.create(user);
        User friend = user;
        friend.setName("Friend");
        friend.setLogin("Friend");
        friend.setEmail("friend@yandex.ru");
        friend.setBirthday(LocalDate.of(2000, 11, 18));
        userService.create(friend);
        userService.addFriend(1, 2);
        Set<Integer> userFriend = new HashSet<>(List.of(2));
        Set<Integer> emptyList = new HashSet<>();
        assertThat(Optional.of(userService.getTargetUser(1)))
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("friends", userFriend));
        assertThat(Optional.of(userService.getTargetUser(2)))
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("friends", emptyList));
        userService.deleteFriend(1, 2);
        assertTrue("список друзей не пустой", userService.getTargetUser(1).getFriends().isEmpty());
    }

    @Test
    @DirtiesContext
    public void shouldReturnAllFriends() {
        userService.create(user);
        userService.create(user);
        userService.create(user);
        userService.create(user);
        userService.create(user);
        userService.addFriend(1, 2);
        userService.addFriend(1, 3);
        userService.addFriend(1, 4);
        userService.addFriend(2, 1);
        userService.addFriend(2, 3);
        userService.addFriend(3, 4);
        Set<Integer> userFriend1 = new HashSet<>(List.of(2, 3, 4));
        Set<Integer> userFriend2 = new HashSet<>(List.of(1, 3));
        Set<Integer> userFriend3 = new HashSet<>(List.of(4));
        Set<Integer> userFriend4 = new HashSet<>();

        assertThat(Optional.of(userService.getTargetUser(1)))
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("friends", userFriend1));
        assertThat(Optional.of(userService.getTargetUser(2)))
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("friends", userFriend2));
        assertThat(Optional.of(userService.getTargetUser(3)))
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("friends", userFriend3));
        assertThat(Optional.of(userService.getTargetUser(4)))
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("friends", userFriend4));
    }

    @Test
    @DirtiesContext
    public void shouldReturnCommonFriendList() {
        userService.create(user);
        userService.create(user);
        userService.create(user);
        userService.create(user);
        userService.addFriend(1, 2);
        userService.addFriend(1, 3);
        userService.addFriend(1, 4);
        userService.addFriend(2, 1);
        userService.addFriend(2, 3);
        userService.addFriend(3, 2);
        userService.addFriend(3, 4);
        List<User> userList1 = userService.allCommonFriends(1, 2);
        List<User> userList2 = userService.allCommonFriends(1, 3);
        assertThat(Optional.of(userService.getTargetUser(3)))
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", userList1.get(0).getId()));
        assertThat(Optional.of(userService.getTargetUser(2)))
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", userList2.get(0).getId()));
        assertThat(Optional.of(userService.getTargetUser(4)))
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", userList2.get(1).getId()));


    }

    @Test
    @DirtiesContext
    public void shouldFindFilmById() {
        filmService.create(film);
        Optional<Film> filmOptional = Optional.of(filmService.getTargetFilm(1));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1)
                                .hasFieldOrPropertyWithValue("name", "Film")
                                .hasFieldOrPropertyWithValue("description", "Film")
                                .hasFieldOrPropertyWithValue("releaseDate",
                                        LocalDate.of(2000, 11, 11))
                                .hasFieldOrPropertyWithValue("duration", 100)
                );
    }

    @Test
    @DirtiesContext
    public void shouldFindAllFilm() {
        filmService.create(film);
        filmService.create(film);
        filmService.create(film);
        Optional<List<Film>> optionalFilms = Optional.of(filmService.allFilms());
        assertThat(optionalFilms)
                .isPresent()
                .hasValueSatisfying(films ->
                        assertThat(films).hasFieldOrPropertyWithValue("size", 3)
                );
    }

    @Test
    @DirtiesContext
    public void shouldUpdateFilm() {
        filmService.create(film);
        Film updateFilm = film;
        updateFilm.setName("Updated");
        updateFilm.setDescription("Updated");
        updateFilm.setReleaseDate(LocalDate.of(2020, 11, 11));
        updateFilm.setDuration(200);
        updateFilm.setMpa(new Mpa(1, "G"));
        filmService.update(updateFilm);
        Optional<Film> updatedFilm = Optional.of(filmService.getTargetFilm(1));
        assertThat(updatedFilm)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "Updated")
                                .hasFieldOrPropertyWithValue("description", "Updated")
                                .hasFieldOrPropertyWithValue("releaseDate",
                                        LocalDate.of(2020, 11, 11))
                                .hasFieldOrPropertyWithValue("duration", 200)
                );
    }

    @Test
    @DirtiesContext
    public void shouldDeleteFilm() {
        filmService.create(film);
        filmService.create(film);
        filmService.create(film);
        Optional<List<Film>> optionalFilms = Optional.of(filmService.allFilms());
        assertThat(optionalFilms)
                .isPresent()
                .hasValueSatisfying(films ->
                        assertThat(films).hasFieldOrPropertyWithValue("size", 3));
        filmService.delete(2);
        Optional<List<Film>> optionalFilmsWithoutDeleted = Optional.of(filmService.allFilms());
        assertThat(optionalFilmsWithoutDeleted)
                .isPresent()
                .hasValueSatisfying(films ->
                        assertThat(films).hasFieldOrPropertyWithValue("size", 2));
    }

    @Test
    @DirtiesContext
    public void shouldAddAndDeleteLikeToFilm() {
        userService.create(user);
        filmService.create(film);
        filmService.addLike(1, 1);
        Set<Integer> likes = new HashSet<>(List.of(1));
        assertThat(Optional.of(filmService.getTargetFilm(1)))
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("likes", likes));
        filmService.deleteLike(1, 1);
        assertTrue("Присутствуют лайки", filmService.getTargetFilm(1).getLikes().isEmpty());

    }

    @Test
    @DirtiesContext
    public void shouldReturnMostPopularFilm() {
        userService.create(user);
        userService.create(user);
        userService.create(user);
        filmService.create(film);
        filmService.create(film);
        Film mostPopular = film;
        mostPopular.setName("Popular");
        mostPopular.setDescription("Most Popular");
        filmService.create(mostPopular);
        filmService.addLike(1, 1);
        filmService.addLike(2, 1);
        filmService.addLike(2, 2);
        filmService.addLike(3, 1);
        filmService.addLike(3, 2);
        filmService.addLike(3, 3);
        List<Film> films = filmService.getFilmByPopularity(1);
        assertThat(films.get(0)).hasFieldOrPropertyWithValue("name", "Popular")
                .hasFieldOrPropertyWithValue("description", "Most Popular");
    }

    @Test
    public void shouldReturnMpa() {
        Optional<Mpa> optionalMpa = Optional.of(filmService.getTargetMpa(2));
        assertThat(optionalMpa)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa).hasFieldOrPropertyWithValue("id", 2)
                                .hasFieldOrPropertyWithValue("name", "PG")
                );
    }

    @Test
    public void shouldReturnAllMpa() {
        Optional<List<Mpa>> optionalMpas = Optional.of(filmService.getAllMpa());
        assertThat(optionalMpas)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa).hasFieldOrPropertyWithValue("size", 5)
                );
    }

    @Test
    public void shouldReturnGenre() {
        Optional<Genre> optionalGenre = Optional.of(filmService.getTargetGenre(2));
        assertThat(optionalGenre)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("id", 2)
                                .hasFieldOrPropertyWithValue("name", "Драма")
                );
    }

    @Test
    public void shouldReturnAllGenres() {
        Optional<List<Genre>> optionalGenres = Optional.of(filmService.getAllGenres());
        assertThat(optionalGenres)
                .isPresent()
                .hasValueSatisfying(genres ->
                        assertThat(genres).hasFieldOrPropertyWithValue("size", 6)
                );
    }

}