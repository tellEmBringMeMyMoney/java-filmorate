package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Film.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Film {

    private static final LocalDate FIRST_FILM_DATE = LocalDate.of(1895, 12, 28);

    private Integer id;

    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;

    @Size(max = 200, message = "Описание не может превышать 200 символов")
    private String description;

    @NotNull(message = "Дата релиза обязательна")
    private LocalDate releaseDate;

    @NotNull(message = "Продолжительность обязательна")
    @Positive(message = "Продолжительность должна быть положительным числом")
    private Integer duration;

    @Valid // Заставляет Spring валидировать внутренние поля MpaRating
    @NotNull(message = "Рейтинг MPA обязателен")
    private MpaRating mpa;

    @AssertTrue(message = "Дата релиза не может быть раньше 28 декабря 1895 года")
    @JsonIgnore
    public boolean isReleaseDateValid() {
        if (releaseDate == null) {
            return true;
        }
        return !releaseDate.isBefore(FIRST_FILM_DATE);
    }

    private Set<Integer> likes = new HashSet<>();

    @Valid
    private Set<Genre> genres = new LinkedHashSet<>();


}
