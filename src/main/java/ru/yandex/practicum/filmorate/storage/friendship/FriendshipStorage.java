package ru.yandex.practicum.filmorate.storage.friendship;

import java.util.List;

public interface FriendshipStorage {
    void addToFriend(Long userId, Long friendsId);
    void addFriendsConfirmation(Long userId, Long friendsId);
    List<Long> getFriends(Long userId);
    List<Long> getConfirmedFriends(Long userId);
    List<Long> getNonConfirmedFriends(Long userId);
    void deleteFromFriends(Long userId, Long friendsId);
}
