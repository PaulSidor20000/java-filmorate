package ru.yandex.practicum.filmorate.storage.friendship;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Primary
@RequiredArgsConstructor
@Component("friendshipDbStorage")
public class FriendshipDbStorage implements FriendshipStorage{
    private final JdbcTemplate jdbcTemplate;
    private static final String SQL_ADD_FRIEND_TO_USER
            = "INSERT INTO friendship (user1_id, user2_id) VALUES(?, ?)";
    private static final String SQL_ADD_FRIENDS_CONFIRMATION
            = "MERGE INTO friendship (user1_id, user2_id, friendship_status)"
            + " KEY (user1_id, user2_id)"
            + " VALUES(?, ?, true)";
    private static final String SQL_GET_COFNIRMED_FRIENDS_OF_USER
            = "SELECT user_id FROM users"
            + " WHERE user_id IN (SELECT user2_id FROM friendship WHERE user1_id = ? AND friendship_status = true)"
            + " OR user_id IN (SELECT user1_id FROM friendship WHERE user2_id = ? AND friendship_status = true)";
    private static final String SQL_GET_NON_CONFIRMED_FRIENDS_OF_USER
            = "SELECT user_id FROM users"
            + " WHERE user_id IN (SELECT user2_id FROM friendship WHERE user1_id = ? AND friendship_status = false)"
            + " OR user_id IN (SELECT user1_id FROM friendship WHERE user2_id = ? AND friendship_status = false)";
    private static final String SQL_DELETE_FROM_FRIEND
            = "DELETE FROM friendship WHERE USER1_ID=? AND USER2_ID=?";
    private static final String SQL_GET_FRIENDS_OF_USER
            = "SELECT user_id FROM users"
            + " WHERE user_id IN (SELECT user2_id FROM friendship WHERE user1_id = ? OR friendship_status = true)";
    private static final String SQL_GET_COMMON_FRIENDS
            = "SELECT user_id FROM users"
            + " WHERE user_id IN (SELECT user2_id FROM friendship WHERE user1_id = ? OR friendship_status = true)"
            + " INTERSECT"
            + " SELECT * FROM users"
            + " WHERE user_id IN (SELECT user2_id FROM friendship WHERE user1_id = ? OR friendship_status = true)";

    @Override
    public void addToFriend(Long userId, Long friendsId) {
        jdbcTemplate.update(SQL_ADD_FRIEND_TO_USER,
                userId,
                friendsId
        );
    }

    @Override
    public void addFriendsConfirmation(Long userId, Long friendsId) {
        jdbcTemplate.update(SQL_ADD_FRIENDS_CONFIRMATION,
                userId,
                friendsId
        );
    }

    @Override
    public List<Long> getConfirmedFriends(Long userId) {
        return jdbcTemplate.query(SQL_GET_COFNIRMED_FRIENDS_OF_USER,
                (rs, rowNum) -> setFriendsIds(rs),
                userId,
                userId
        );
    }

    @Override
    public List<Long> getFriends(Long userId) {
        return jdbcTemplate.query(SQL_GET_FRIENDS_OF_USER,
                (rs, rowNum) -> setFriendsIds(rs),
                userId
        );
    }

    @Override
    public List<Long> getNonConfirmedFriends(Long userId) {
        return jdbcTemplate.query(SQL_GET_NON_CONFIRMED_FRIENDS_OF_USER,
                (rs, rowNum) -> setFriendsIds(rs),
                userId,
                userId
        );
    }

    @Override
    public void deleteFromFriends(Long userId, Long friendsId) {
        jdbcTemplate.update(SQL_DELETE_FROM_FRIEND,
                userId,
                friendsId
        );
    }

    public List<Long> getCommonFriends(Long userId, Long otherId) {
        return jdbcTemplate.query(SQL_GET_COMMON_FRIENDS,
                (rs, rowNum) -> setFriendsIds(rs),
                userId,
                otherId
        );
    }

    private Long setFriendsIds(ResultSet rs) throws SQLException {
        return rs.getLong("user_id");
    }

}
