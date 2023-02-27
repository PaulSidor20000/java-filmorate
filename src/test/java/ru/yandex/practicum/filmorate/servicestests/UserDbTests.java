package ru.yandex.practicum.filmorate.servicestests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbTests extends TestEnvironment {
    private final UserService userService;
    private final JdbcTemplate jdbcTemplate;

    @BeforeEach
    void before() {
        userService.addUser(user1);
        userService.addUser(user2);
        userService.addUser(friend1);
    }

    @AfterEach
    void after() {
        jdbcTemplate.update("DELETE FROM FRIENDSHIP");
        jdbcTemplate.update("DELETE FROM USERS");
        jdbcTemplate.update("ALTER TABLE USERS ALTER COLUMN USER_ID RESTART WITH 1");
    }

    @Test
    void findAllUsersTest() {
        List<User> users = userService.findAllUsers();

        assertThat(users, equalTo(List.of(user1, user2, friend1)));
    }

    @Test
    void addUserTest() {
        User user = User.builder()
                .name("Jack1")
                .login("user1")
                .email("user1@mail.ru")
                .birthday(LocalDate.parse("2000-10-10"))
                .friends(List.of())
                .build();

        User newUser = userService.addUser(user);

        assertThat(newUser.getId(), equalTo(4L));
        assertThat(newUser.getName(), equalTo("Jack1"));
        assertThat(newUser.getLogin(), equalTo("user1"));
        assertThat(newUser.getEmail(), equalTo("user1@mail.ru"));
        assertThat(newUser.getBirthday(), equalTo(LocalDate.parse("2000-10-10")));
        assertThat(newUser.getFriends(), equalTo(List.of()));
    }

    @Test
    void updateUserTest() {
        User user = User.builder()
                .id(1L)
                .name("Hank4")
                .login("user4")
                .email("user4@mail.ru")
                .birthday(LocalDate.parse("2000-05-11"))
                .build();

        User newUser = userService.updateUser(user);

        assertThat(newUser.getId(), equalTo(1L));
        assertThat(newUser.getName(), equalTo("Hank4"));
        assertThat(newUser.getLogin(), equalTo("user4"));
        assertThat(newUser.getEmail(), equalTo("user4@mail.ru"));
        assertThat(newUser.getBirthday(), equalTo(LocalDate.parse("2000-05-11")));
    }

    @Test
    void findUserByIdTest() {
        User newUser = userService.findUserById(1L);

        assertThat(newUser, equalTo(user1));
    }

    @Test
    void addToFriendTest() {
        User userWithFriend = User.builder()
                .id(1L)
                .name("Jack1")
                .login("user1")
                .email("user1@mail.ru")
                .birthday(LocalDate.parse("2000-10-10"))
                .friends(List.of(friend1.getId()))
                .build();

        userService.addToFriend(1L, 3L);
        User newUser = userService.findUserById(1L);

        assertThat(newUser, equalTo(userWithFriend));
    }

    @Test
    void deleteFromFriendsTest() {
        User userWithoutFriend = User.builder()
                .id(1L)
                .name("Jack1")
                .login("user1")
                .email("user1@mail.ru")
                .birthday(LocalDate.parse("2000-10-10"))
                .friends(List.of())
                .build();

        userService.addToFriend(1L, 3L);
        User userWithFriend = userService.findUserById(1L);

        assertThat(userWithFriend.getFriends(), equalTo(List.of(friend1.getId())));

        userService.deleteFromFriends(1L, 3L);
        User newUser = userService.findUserById(1L);

        assertThat(newUser, equalTo(userWithoutFriend));
    }

    @Test
    void findFriendsTest() {
        userService.addToFriend(1L, 3L);
        List<User> usersFriend = userService.findFriends(1L);

        assertThat(usersFriend, equalTo(List.of(friend1)));
    }

    @Test
    void findCommonFriendsTest() {
        userService.addToFriend(1L, 3L);
        userService.addToFriend(2L, 3L);
        List<User> CommonFriends = userService.findCommonFriends(1L, 2L);

        assertThat(CommonFriends, equalTo(List.of(friend1)));
    }

}
