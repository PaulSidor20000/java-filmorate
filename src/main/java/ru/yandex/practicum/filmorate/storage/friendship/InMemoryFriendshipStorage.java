package ru.yandex.practicum.filmorate.storage.friendship;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class InMemoryFriendshipStorage implements FriendshipStorage {
    private final InMemoryUserStorage storage;
    @Override
    public boolean addToFriend(Long userId, Long friendsId) {
        User user = storage.findUserById(userId);
        if (user != null) {
            log.info("Friend: \"{}\", successfully added to User: \"{}\" list", friendsId, userId);
            return user.getFriends().add(friendsId);
        }
        log.error("Failed adding Friend: \"{}\", to User: \"{}\" list", friendsId, userId);

        return false;
    }

    @Override
    public boolean addFriendsConfirmation(Long userId, Long friendsId) {
        User friend = storage.findUserById(friendsId);

        if (friend != null) {
            log.info("Friend: \"{}\", confirmed friendship to User: \"{}\"", friendsId, userId);
            return friend.getFriends().add(friendsId);
        }
        log.error("Failed adding Friend: \"{}\", to User: \"{}\" list", friendsId, userId);

        return false;
    }

    @Override
    public List<Long> getFriendsIds(Long userId) {
        User user = storage.findUserById(userId);

        return user.getFriends();
    }

    @Override
    public List<Long> getConfirmedFriends(Long userId) {
        return null;
    }

    @Override
    public List<Long> getNonConfirmedFriends(Long userId) {
        return null;
    }

    @Override
    public List<User> findFriends(Long userId) {
        User user = storage.findUserById(userId);

        return user.getFriends().stream()
                .map(storage::findUserById)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteFromFriends(Long userId, Long friendsId) {
        User user = storage.findUserById(userId);
        User friend = storage.findUserById(friendsId);

        if (user.getFriends().remove(friend.getId())
        && friend.getFriends().remove(user.getId())) {
            log.info("Friend: \"{}\", successfully deleted from User: \"{}\" list", friend.getName(), user.getName());
            return true;
        }
        log.error("Failed deleting Friend: \"{}\", from User: \"{}\" list", friend.getName(), user.getName());

        return false;
    }

    public List<User> findCommonFriends(Long userId, Long otherId) {
        User user1 = storage.findUserById(userId);
        User user2 = storage.findUserById(otherId);

        return user1.getFriends().stream()
                .filter(user2.getFriends()::contains)
                .map(storage::findUserById)
                .collect(Collectors.toList());
    }

}
