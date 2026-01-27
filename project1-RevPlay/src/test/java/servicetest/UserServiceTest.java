package servicetest;

import org.example.dao.UserDao;
import org.example.model.User;
import org.example.service.UserService;
import org.example.util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserDao userDao;   // fake DAO

    @InjectMocks
    private UserService userService; // real service with mocked DAO

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("test@gmail.com");
        user.setPassword("1234");
    }

    // ---------------- REGISTER ----------------

    @Test
    void testRegisterSuccess() {
        when(userDao.userExists(user.getEmail())).thenReturn(false);
        when(userDao.registerUser(any(User.class))).thenReturn(true);

        boolean result = userService.register(user);

        assertTrue(result);
        verify(userDao).registerUser(any(User.class));
    }

    @Test
    void testRegisterFailWhenUserExists() {
        when(userDao.userExists(user.getEmail())).thenReturn(true);

        boolean result = userService.register(user);

        assertFalse(result);
        verify(userDao, never()).registerUser(any());
    }

    @Test
    void testRegisterFailWhenEmailNull() {
        user.setEmail(null);

        boolean result = userService.register(user);

        assertFalse(result);
        verify(userDao, never()).registerUser(any());
    }

    // ---------------- LOGIN ----------------

    @Test
    void testLoginSuccess() {
        String hashed = PasswordUtil.hashPassword("1234");
        user.setPassword(hashed);

        when(userDao.getUserByEmail("test@gmail.com")).thenReturn(user);

        User loggedIn = userService.login("test@gmail.com", "1234");

        assertNotNull(loggedIn);
        assertEquals("test@gmail.com", loggedIn.getEmail());
    }

    @Test
    void testLoginFailWrongPassword() {
        user.setPassword(PasswordUtil.hashPassword("1234"));
        when(userDao.getUserByEmail("test@gmail.com")).thenReturn(user);

        User loggedIn = userService.login("test@gmail.com", "wrong");

        assertNull(loggedIn);
    }

    @Test
    void testLoginFailUserNotFound() {
        when(userDao.getUserByEmail("test@gmail.com")).thenReturn(null);

        User loggedIn = userService.login("test@gmail.com", "1234");

        assertNull(loggedIn);
    }

    // ---------------- CHANGE PASSWORD ----------------

    @Test
    void testChangePasswordSuccess() {
        String oldHashed = PasswordUtil.hashPassword("old123");
        user.setPassword(oldHashed);

        when(userDao.getUserByEmail(user.getEmail())).thenReturn(user);
        when(userDao.updatePassword(eq(user.getEmail()), anyString())).thenReturn(true);

        boolean result = userService.changePassword(
                user.getEmail(), "old123", "new123");

        assertTrue(result);
    }

    @Test
    void testChangePasswordFailWrongOldPassword() {
        user.setPassword(PasswordUtil.hashPassword("old123"));
        when(userDao.getUserByEmail(user.getEmail())).thenReturn(user);

        boolean result = userService.changePassword(
                user.getEmail(), "wrong", "new123");

        assertFalse(result);
        verify(userDao, never()).updatePassword(anyString(), anyString());
    }

    @Test
    void testChangePasswordFailUserNotFound() {
        when(userDao.getUserByEmail(user.getEmail())).thenReturn(null);

        boolean result = userService.changePassword(
                user.getEmail(), "old", "new");

        assertFalse(result);
    }

    // ---------------- RECOVER PASSWORD ----------------

    @Test
    void testRecoverPasswordSuccess() {
        user.setPassword(PasswordUtil.hashPassword("old123"));

        when(userDao.verifySecurityAnswer(user.getEmail(), "pet"))
                .thenReturn(true);
        when(userDao.getUserByEmail(user.getEmail()))
                .thenReturn(user);
        when(userDao.updatePassword(eq(user.getEmail()), anyString()))
                .thenReturn(true);

        boolean result = userService.recoverPassword(
                user.getEmail(), "pet", "new123");

        assertTrue(result);
    }

    @Test
    void testRecoverPasswordFailWrongAnswer() {
        when(userDao.verifySecurityAnswer(user.getEmail(), "pet"))
                .thenReturn(false);

        boolean result = userService.recoverPassword(
                user.getEmail(), "pet", "new123");

        assertFalse(result);
    }

    // ---------------- SECURITY QUESTION ----------------

    @Test
    void testGetSecurityQuestion() {
        when(userDao.getSecurityQuestion(user.getEmail()))
                .thenReturn("Your pet name?");

        String question = userService.getSecurityQuestion(user.getEmail());

        assertEquals("Your pet name?", question);
    }
}
