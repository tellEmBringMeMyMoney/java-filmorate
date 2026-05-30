package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.util.List;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film create(Film film) {
        Film created = filmStorage.create(film);
        log.info("Добавлен фильм: id={}, name={}", created.getId(), created.getName());
        return created;
    }

    public Film update(Film film) {
        if (film.getId() == null || filmStorage.getById(film.getId()).isEmpty()) {
            throw new NotFoundException("Фильм с id=" + film.getId() + " не найден");
        }
        filmStorage.update(film);
        log.info("Обновлён фильм: id={}, name={}", film.getId(), film.getName());
        return film;
    }

    public List<Film> findAll() {
        return filmStorage.getAll();
    }

    public Film findById(Integer id) {
        return filmStorage.getById(id)
                .orElseThrow(() -> new NotFoundException("Фильм с id=" + id + " не найден"));
    }

    public void addLike(Integer filmId, Integer userId) {
        filmStorage.getById(filmId).orElseThrow(() -> new NotFoundException("Фильм с id=" + filmId + " не найден"));
        userStorage.getById(userId).orElseThrow(() -> new NotFoundException("Пользователь с id=" + userId + " не найден"));

        filmStorage.addLike(filmId, userId);
        log.info("Пользователь id={} поставил лайк фильму id={}", userId, filmId);
    }

    public void removeLike(Integer filmId, Integer userId) {
        filmStorage.getById(filmId).orElseThrow(() -> new NotFoundException("Фильм с id=" + filmId + " не найден"));
        userStorage.getById(userId).orElseThrow(() -> new NotFoundException("Пользователь с id=" + userId + " не найден"));

        filmStorage.removeLike(filmId, userId);
        log.info("Пользователь id={} убрал лайк с фильма id={}", userId, filmId);
    }

    public List<Film> getPopular(int count) {
        if (count < 0) {
            throw new ValidationException("Параметр count не может быть отрицательным");
        }
        if (count == 0) {
            return List.of();
        }
        return filmStorage.getPopular(count);
    }

}
