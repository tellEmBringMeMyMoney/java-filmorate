package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Genre {

    @Min(value = 1, message = "ID жанра должен быть положительным числом")
    private int id;

    private String name;
}
