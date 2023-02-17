package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
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
    private static final String SQL_DELETE_USER
            = "DELETE FROM USERS WHERE USER_ID=?";

    @Override
    public List<User> findAllUsers() {
        return jdbcTemplate.query(SQL_FIND_ALL_USERS,
                (rs, rowNum) -> setUser(rs)
        );
    }

    @Override
    public User findUserById(Long userId) {
        return jdbcTemplate.queryForObject(SQL_FIND_USER_BY_ID,
                (rs, rowNum) -> setUser(rs),
                userId
        );
    }

    @Override
    public User addUser(User user) {
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(SQL_ADD_USER, new String[]{"user_id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);

        return findUserById(Objects.requireNonNull(keyHolder.getKey()).longValue());
    }

    @Override
    public User updateUser(User user) {
        jdbcTemplate.update(SQL_UPDATE_USER,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );

        return findUserById(user.getId());
    }
    @Override
    public void deleteUser(User user) {
        jdbcTemplate.update(SQL_DELETE_USER, user.getId());
    }

    private User setUser(ResultSet rs) throws SQLException {
        return User.builder()
                .id(rs.getLong("user_id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .friends(friendshipDbStorage.getFriends(rs.getLong("user_id")))
                .build();
    }

}
