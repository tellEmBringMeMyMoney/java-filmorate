package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new ConcurrentHashMap<>();//мапа с многопоточностью (подсказка гугла)
    private final Map<Integer, Set<Integer>> likes = new ConcurrentHashMap<>();
    private final AtomicInteger nextId = new AtomicInteger(1);//лучше чем nextId++ по той же причине

    @Override
    public Film create(Film film) {
        film.setId(nextId.getAndIncrement());
        films.put(film.getId(), film);
        likes.putIfAbsent(film.getId(), ConcurrentHashMap.newKeySet());
        return film;
    }

    @Override
    public void update(Film film) {
        films.put(film.getId(), film);
        likes.putIfAbsent(film.getId(), ConcurrentHashMap.newKeySet());
    }

    @Override
    public void delete(Integer id) {
        films.remove(id);
        likes.remove(id);
    }

    @Override
    public Optional<Film> getById(Integer id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        likes.computeIfAbsent(filmId, k -> ConcurrentHashMap.newKeySet()).add(userId);
    }

    @Override
    public void removeLike(Integer filmId, Integer userId) {
        Set<Integer> filmLikes = likes.get(filmId);
        if (filmLikes != null) {
            filmLikes.remove(userId);
        }
    }

    @Override
    public long getLikeCount(Integer filmId) {
        Set<Integer> filmLikes = likes.get(filmId);
        return filmLikes == null ? 0 : filmLikes.size();
    }

    @Override
    public List<Film> getPopular(int count) {
        return films.values().stream()
                .sorted(Comparator
                        .comparingLong((Film f) -> getLikeCount(f.getId())).reversed()
                        .thenComparingInt(Film::getId))
                .limit(count)
                .toList();
    }
}
