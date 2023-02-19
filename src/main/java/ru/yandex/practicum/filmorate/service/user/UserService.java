package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage storage;
    private final FriendshipStorage friendshipStorage;

    public List<User> findAllUsers() {
        return storage.findAllUsers();
    }

    public User addUser(User user) {
        return storage.addUser(user);
    }

    public User updateUser(User user) {
        return storage.updateUser(user);
    }

    public User findUserById(Long userId) {
        return storage.findUserById(userId);
    }

    public void addToFriend(Long userId, Long friendsId) {
        if (friendshipStorage.getNonConfirmedFriends(userId).contains(friendsId)) {
            friendshipStorage.addFriendsConfirmation(userId, friendsId);
            log.info("Friend: \"{}\", successfully added to User: \"{}\" list", friendsId, userId);
        } else {
            friendshipStorage.addToFriend(userId, friendsId);
            log.info("Friend: \"{}\", invited by User: \"{}\"", friendsId, userId);
        }
    }

    public void deleteFromFriends(Long userId, Long friendsId) {
        friendshipStorage.deleteFromFriends(userId, friendsId);
        log.info("Friend: \"{}\", successfully deleted from User: \"{}\" list", friendsId, userId);
    }

    public List<User> findFriends(Long userId) {
        return friendshipStorage.findFriends(userId);
    }

    public List<User> findCommonFriends(Long userId, Long otherId) {
        return friendshipStorage.findCommonFriends(userId, otherId);
    }

}
