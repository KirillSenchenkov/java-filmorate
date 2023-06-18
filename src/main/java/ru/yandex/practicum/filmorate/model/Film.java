package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

@Data
@AllArgsConstructor
@Builder
public class Film {
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;

    @Builder.Default
    private Set<String> likes = new LinkedHashSet<>();

    public void addLike(User user){
        likes.add(user.getName());
    }

}
