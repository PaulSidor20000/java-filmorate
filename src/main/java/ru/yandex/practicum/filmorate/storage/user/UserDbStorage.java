package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipDbStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Primary
@RequiredArgsConstructor
@Component("userDbStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FriendshipDbStorage friendshipDbStorage;
    private static final String SQL_FIND_ALL_USERS
            = "SELECT * FROM users";
    private static final String SQL_FIND_USER_BY_ID
            = "SELECT * FROM users WHERE user_id = ?";
    private static final String SQL_ADD_USER
            = "INSERT INTO USERS (EMAIL, LOGIN, NAME, BIRTHDAY) VALUES(?, ?, ?, ?)";
    private static final String SQL_UPDATE_USER
            = "UPDATE USERS SET EMAIL=?, LOGIN=?, NAME=?, BIRTHDAY=? WHERE USER_ID=?";

    @Override
    public List<User> findAllUsers() {
        return jdbcTemplate.query(SQL_FIND_ALL_USERS,
                (rs, rowNum) -> setUser(rs)
        );
    }

    @Override
    public User findUserById(Long userId) {
        User user = jdbcTemplate.queryForObject(SQL_FIND_USER_BY_ID,
                (rs, rowNum) -> setUser(rs),
                userId
        );
        if (user != null) {
            return user;
        }
        String errorMessage = String.format("User ID: %s, not exist", userId);
        log.error(errorMessage);
        throw new IllegalArgumentException(errorMessage);
    }

    @Override
    public User addUser(User user) {
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        User newUser;
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(SQL_ADD_USER, new String[]{"user_id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);

        newUser = findUserById(Objects.requireNonNull(keyHolder.getKey()).longValue());
        if (newUser != null) {
            log.info(String.format("New User: %s, was successfully added", user.getLogin()));
            return newUser;
        }
        String errorMessage = String.format("User: %s, is not valid", user.getName());
        log.error(errorMessage);
        throw new ValidationException(errorMessage);
    }

    @Override
    public User updateUser(User user) {
        User updateUser;

        jdbcTemplate.update(SQL_UPDATE_USER,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );

        updateUser = findUserById(user.getId());
        if (updateUser != null) {
            log.info(String.format("User: %s, was successfully updated", user.getName()));
            return updateUser;
        }
        String errorMessage = String.format("User: %s, is not exist", user.getName());
        log.error(errorMessage);
        throw new IllegalArgumentException(errorMessage);
    }

    private User setUser(ResultSet rs) throws SQLException {
        return User.builder()
                .id(rs.getLong("user_id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .friends(friendshipDbStorage.getFriendsIds(rs.getLong("user_id")))
                .build();
    }

}
