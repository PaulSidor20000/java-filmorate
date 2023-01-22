package ru.yandex.practicum.filmorate.servise.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage storage;

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

    public User addToFriends(Long userId, Long friendsId) {
        User user = storage.findUserById(userId);
        User friend = storage.findUserById(friendsId);

        user.getFriends().add(friend.getId());
        friend.getFriends().add(user.getId());
        log.info("Friend: \"{}\", successfully added to User: \"{}\" list", friend.getName(), user.getName());
        return user;
    }

    public User deleteFromFriends(Long userId, Long friendsId) {
        User user = storage.findUserById(userId);
        User friend = storage.findUserById(friendsId);

        user.getFriends().remove(friend.getId());
        friend.getFriends().remove(user.getId());
        log.info("Friend: \"{}\", successfully deleted from User: \"{}\" list", friend.getName(), user.getName());
        return user;
    }

    public List<User> findFriends(Long userId) {
        User user = storage.findUserById(userId);

        return user.getFriends().stream()
                .map(storage::findUserById)
                .collect(Collectors.toList());
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
