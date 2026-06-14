package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserStorage {

    User create(User user);

    void update(User user);

    void delete(Integer id);

    Optional<User> getById(Integer id);

    List<User> getAll();

    void addFriend(Integer userId, Integer friendId);

    void removeFriend(Integer userId, Integer friendId);

    Set<Integer> getFriendIds(Integer userId);
}
