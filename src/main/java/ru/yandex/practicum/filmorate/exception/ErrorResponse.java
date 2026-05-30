package ru.yandex.practicum.filmorate.exception;

import lombok.Builder;
import lombok.Value;


import java.util.List;

@Value
@Builder
public class ErrorResponse {

    String error;
    String message;
    List<String> errors;

    public static ErrorResponse of(String error, String message) {
        return ErrorResponse.builder()
                .error(error)
                .message(message)
                .build();
    }

    public static ErrorResponse of(String error, List<String> errors) {
        return ErrorResponse.builder()
                .error(error)
                .errors(errors)
                .build();
    }
}
