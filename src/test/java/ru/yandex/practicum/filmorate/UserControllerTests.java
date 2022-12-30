package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTests {

    private static final  String ASSERT_SIZE = "Wrong actual size";
    private static final String ASSERT_VALUE = "Wrong actual value";
    private static final String ASSERT_NULL = "Null point reference";
    private static final String ASSERT_TROWS_INVALID = "The method should trows ValidationException";
    private UserController userController;
    private User user;

    @BeforeEach
    void beforeEach() {
        userController = new UserController();

        user = User.builder()
                .email("mail@mail.com")
                .login("John22")
                .name("John Smith")
                .birthday(LocalDate.of(2000, Month.JANUARY, 1))
                .build();
    }

    @AfterEach
    void afterEach() {
        userController.clearUsers();
    }

    // create User
    @Test
    void setNewUserNormallyAndGetAllUsers() {
        userController.setNewUser(user);

        assertEquals(1, userController.getAllUsers().size(), ASSERT_SIZE);
        assertEquals(user, userController.getAllUsers().get(0), ASSERT_VALUE);
        assertNotNull(userController.getAllUsers().get(0), ASSERT_NULL);

        // check user inside storage
        assertEquals(1, userController.getAllUsers().get(0).getId(), ASSERT_VALUE);
        assertEquals("mail@mail.com", userController.getAllUsers().get(0).getEmail(), ASSERT_VALUE);
        assertEquals("John22", userController.getAllUsers().get(0).getLogin(), ASSERT_VALUE);
        assertEquals("John Smith", userController.getAllUsers().get(0).getName(), ASSERT_VALUE);
        assertEquals(LocalDate.of(2000, Month.JANUARY, 1),
                userController.getAllUsers().get(0).getBirthday(), ASSERT_VALUE);
    }

    @Test
    void setNewUserWrongLogin() {
        user.setLogin("");
        assertThrows(ValidationException.class, () -> userController.setNewUser(user), ASSERT_TROWS_INVALID);
        assertEquals(0, userController.getAllUsers().size(), ASSERT_SIZE);

        user.setLogin("   New  User  ");
        assertThrows(ValidationException.class, () -> userController.setNewUser(user), ASSERT_TROWS_INVALID);
        assertEquals(0, userController.getAllUsers().size(), ASSERT_SIZE);
    }

    @Test
    void setNewUserWrongName() {
        user.setName("");
        userController.setNewUser(user);

        assertEquals("John22", userController.getAllUsers().get(0).getName(), ASSERT_VALUE);
        assertEquals(1, userController.getAllUsers().size(), ASSERT_SIZE);
    }

    @Test
    void setNewUserWrongEmail() {
        user.setEmail("mail.com");
        assertThrows(ValidationException.class, () -> userController.setNewUser(user), ASSERT_TROWS_INVALID);
        assertEquals(0, userController.getAllUsers().size(), ASSERT_SIZE);
    }

    @Test
    void setNewUserWrongBirthday() {
        user.setBirthday(LocalDate.of(2100, Month.JANUARY, 1));
        assertThrows(ValidationException.class, () -> userController.setNewUser(user), ASSERT_TROWS_INVALID);
        assertEquals(0, userController.getAllUsers().size(), ASSERT_SIZE);
    }

    // update User
    @Test
    void updateUserNormallyAndGetAllUsers() {
        userController.setNewUser(user);
        user = User.builder()
                .id(1)
                .login("johnsmith23")
                .email("johnsmith2000@mail.com")
                .name("John Smith Jr.")
                .birthday(LocalDate.of(1999, Month.JANUARY, 1))
                .build();
        userController.updateUser(user);

        assertEquals(1, userController.getAllUsers().size(), ASSERT_SIZE);
        assertEquals(user, userController.getAllUsers().get(0), ASSERT_VALUE);
        assertNotNull(userController.getAllUsers().get(0), ASSERT_NULL);

        // check user inside storage
        assertEquals(1, userController.getAllUsers().get(0).getId(), ASSERT_VALUE);
        assertEquals("johnsmith2000@mail.com", userController.getAllUsers().get(0).getEmail(), ASSERT_VALUE);
        assertEquals("johnsmith23", userController.getAllUsers().get(0).getLogin(), ASSERT_VALUE);
        assertEquals("John Smith Jr.", userController.getAllUsers().get(0).getName(), ASSERT_VALUE);
        assertEquals(LocalDate.of(1999, Month.JANUARY, 1),
                userController.getAllUsers().get(0).getBirthday(), ASSERT_VALUE);
    }

    @Test
    void updateUserWithWrongId() {
        userController.setNewUser(user);
        user = User.builder()
                .id(10)
                .login("johnsmith23")
                .email("johnsmith2000@mail.com")
                .name("John Smith Jr.")
                .birthday(LocalDate.of(1999, Month.JANUARY, 1))
                .build();

        assertThrows(ValidationException.class, () -> userController.updateUser(user), ASSERT_TROWS_INVALID);
        assertEquals(1, userController.getAllUsers().size(), ASSERT_SIZE);
        assertEquals(1, userController.getAllUsers().get(0).getId(), ASSERT_SIZE);
    }

}
