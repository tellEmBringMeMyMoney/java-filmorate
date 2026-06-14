package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    User create(User user);

    void update(User user);

    void delete(Integer id);

    Optional<User> getById(Integer id);

    List<User> getAll();

    void addFriend(Integer userId, Integer friendId, FriendshipStatus status);

    void removeFriend(Integer userId, Integer friendId);

    void updateFriendshipStatus(Integer userId, Integer friendId, FriendshipStatus status);

    boolean friendshipExists(Integer userId, Integer friendId);

    List<User> getFriends(Integer userId);

    List<User> getCommonFriends(Integer userId, Integer otherId);
}
