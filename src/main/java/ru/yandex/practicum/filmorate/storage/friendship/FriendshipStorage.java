package ru.yandex.practicum.filmorate.storage.friendship;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendshipStorage {
    boolean addToFriend(Long userId, Long friendsId);
    boolean addFriendsConfirmation(Long userId, Long friendsId);
    List<Long> getFriendsIds(Long userId);
    List<Long> getConfirmedFriends(Long userId);
    List<Long> getNonConfirmedFriends(Long userId);
    boolean deleteFromFriends(Long userId, Long friendsId);
    List<User> findFriends(Long userId);
    List<User> findCommonFriends(Long userId, Long otherId);
}
