package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MpaRating {
    @Min(value = 1, message = "ID рейтинга MPA должен быть положительным числом")
    private int id;

    private String name;
}
