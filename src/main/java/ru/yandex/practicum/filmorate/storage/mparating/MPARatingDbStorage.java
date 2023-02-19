package ru.yandex.practicum.filmorate.storage.mparating;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MPARating;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Primary
@RequiredArgsConstructor
@Component("mpaRatingDbStorage")
public class MPARatingDbStorage implements MPARatingStorage{
    private final JdbcTemplate jdbcTemplate;

    private static final String SQL_FIND_ALL_RATINGS
            = "SELECT *"
            + " FROM mpa_rating mpa";
    private static final String SQL_FIND_RATING_BY_ID
            = SQL_FIND_ALL_RATINGS + " WHERE mpa.mpa_rating_id = ?";
    private static final String SQL_ADD_RATING
            = "INSERT INTO mpa_rating (MPA_RATING_NAME) VALUES(?)";
    private static final String SQL_UPDATE_RATING
            = "UPDATE mpa_rating SET MPA_RATING_NAME=? WHERE MPA_RATING_ID=?";
    private static final String SQL_DELETE_RATING
            = "DELETE FROM mpa_rating WHERE MPA_RATING_ID=?";

    @Override
    public List<MPARating> findAllRatings() {
        return jdbcTemplate.query(SQL_FIND_ALL_RATINGS,
                (rs, rowNum) -> setMPARating(rs)
        );
    }

    @Override
    public MPARating findRatingById(Integer mpaRatingId) {
        return jdbcTemplate.queryForObject(SQL_FIND_RATING_BY_ID,
                (rs, rowNum) -> setMPARating(rs),
                mpaRatingId
        );
    }

    @Override
    public MPARating addRating(MPARating mpaRating) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
                    PreparedStatement stmt = connection.prepareStatement(SQL_ADD_RATING, new String[]{"mpa_rating_id"});
                    stmt.setString(1, mpaRating.getName());
                    return stmt;
                },
                keyHolder
        );

            return findRatingById(Objects.requireNonNull(keyHolder.getKey()).intValue());
        }

    @Override
    public MPARating updateRating(MPARating mpaRating) {
        jdbcTemplate.update(SQL_UPDATE_RATING,
                mpaRating.getName(),
                mpaRating.getId()
        );
        return findRatingById(mpaRating.getId());
    }

    @Override
     public void deleteRating(MPARating mpaRating) {
        jdbcTemplate.update(SQL_DELETE_RATING, mpaRating.getId());
    }

    private MPARating setMPARating(ResultSet rs) throws SQLException {
        return MPARating.builder()
                .id(rs.getInt("mpa_rating_id"))
                .name(rs.getString("mpa_rating_name"))
                .build();
    }

}
