package daotest;

import org.example.dao.UserDao;
import org.example.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserDaoTest {
    private UserDao userDao;
    private void cleanupUser(String email) {
        int userId = userDao.getUserIdByEmail(email);
        if (userId > 0) {
            userDao.deleteUserById(userId);
        }
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDao();
    }

    @Test
    void testRegisterUserSuccess() {
        User user = new User();
        user.setEmail("dao_test@gmail.com");
        user.setPassword("1234"); // plain or hashed – DAO doesn't care
        user.setRole("USER");
        user.setStatus("ACTIVE");
        boolean result = userDao.registerUser(user);
        assertTrue(result);
        // cleanup
        int userId = userDao.getUserIdByEmail("dao_test@gmail.com");
        assertTrue(userId > 0);
    }

    @Test
    void testUserExists() {
        User user = new User();
        user.setEmail("exists_test@gmail.com");
        user.setPassword("1234");
        user.setRole("USER");
        user.setStatus("ACTIVE");
        userDao.registerUser(user);
        boolean exists = userDao.userExists("exists_test@gmail.com");
        assertTrue(exists);
        cleanupUser("exists_test@gmail.com");
    }

    @Test
    void testGetUserByEmail() {
        User user = new User();
        user.setEmail("fetch_test@gmail.com");
        user.setPassword("1234");
        user.setRole("USER");
        user.setStatus("ACTIVE");
        userDao.registerUser(user);
        User fetchedUser = userDao.getUserByEmail("fetch_test@gmail.com");
        assertNotNull(fetchedUser);
        assertEquals("fetch_test@gmail.com", fetchedUser.getEmail());
        cleanupUser("fetch_test@gmail.com");
    }

    @Test
    void testUpdatePassword() {
        User user = new User();
        user.setEmail("password_test@gmail.com");
        user.setPassword("oldpass");
        user.setRole("USER");
        user.setStatus("ACTIVE");

        userDao.registerUser(user);
        boolean updated = userDao.updatePassword("password_test@gmail.com", "newpass");
        assertTrue(updated);

        User updatedUser = userDao.getUserByEmail("password_test@gmail.com");
        assertEquals("newpass", updatedUser.getPassword());

        cleanupUser("password_test@gmail.com");
    }


}

