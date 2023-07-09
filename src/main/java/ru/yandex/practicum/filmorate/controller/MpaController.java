package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.MpaDbStorage;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/mpa")
public class MpaController {

    private final MpaDbStorage mpaDbStorage;
    @GetMapping
    public List<Mpa> getAllMpa() {
        return mpaDbStorage.getAllMpa();
    }

    @GetMapping("/{mpaId}")
    public Mpa getTargetMpa (@PathVariable Integer mpaId) {
         return mpaDbStorage.getTargetMpa(mpaId);
    }
}
