package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User create(User user) {
        normalizeName(user);
        User created = userStorage.create(user);
        log.info("Создан пользователь: id={}, login={}", created.getId(), created.getLogin());
        return created;
    }

    public User update(User user) {
        if (user.getId() == null) {
            throw new ValidationException("ID пользователя не может быть пустой");
        }
        getUserOrThrow(user.getId());

        normalizeName(user);
        userStorage.update(user);
        log.info("Обновлён пользователь: id={}, login={}", user.getId(), user.getLogin());
        return user;
    }

    public List<User> findAll() {
        return userStorage.getAll();
    }

    public User findById(Integer id) {
        return getUserOrThrow(id);
    }

    public void addFriend(Integer userId, Integer friendId) {
        ensureDifferentUsers(userId, friendId);
        getUserOrThrow(userId);
        getUserOrThrow(friendId);

        userStorage.addFriend(userId, friendId);
        log.info("Пользователь id={} добавил в друзья id={}", userId, friendId);
    }

    public void removeFriend(Integer userId, Integer friendId) {
        ensureDifferentUsers(userId, friendId);
        getUserOrThrow(userId);
        getUserOrThrow(friendId);

        userStorage.removeFriend(userId, friendId);
        log.info("Пользователь id={} удалил из друзей id={}", userId, friendId);
    }

    public List<User> getFriends(Integer userId) {
        getUserOrThrow(userId);

        return userStorage.getFriendIds(userId).stream()
                .sorted()
                .map(this::getUserOrThrow)
                .toList();
    }

    public List<User> getCommonFriends(Integer userId, Integer otherId) {
        ensureDifferentUsers(userId, otherId);
        getUserOrThrow(userId);
        getUserOrThrow(otherId);

        Set<Integer> a = userStorage.getFriendIds(userId);
        Set<Integer> b = userStorage.getFriendIds(otherId);

        return a.stream()
                .filter(b::contains)
                .sorted()
                .map(this::getUserOrThrow)
                .toList();
    }

    private void ensureDifferentUsers(Integer userId, Integer otherUserId) {
        if (Objects.equals(userId, otherUserId)) {
            throw new ValidationException("Операция с одним и тем же пользователем недопустима");
        }
    }

    private void normalizeName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    private User getUserOrThrow(Integer userId) {
        return userStorage.getById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + userId + " не найден"));
    }
}