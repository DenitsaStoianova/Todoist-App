package bg.sofia.uni.fmi.mjt.todoist.storage.user;

import bg.sofia.uni.fmi.mjt.todoist.storage.user.UserStorage;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserStorageTest {
    private UserStorage userStorage;

    private final String testUser = "Alex";
    private final String testPassword = "123K";

    @Before
    public void setUp() {
        userStorage = new UserStorage();
        userStorage.registerUser(testUser, testPassword);
    }

    @Test
    public void testRegisterUserSuccess() {
        final String username = "Paul";
        final String password = "456";
        assertTrue("Expected true when new user is added.", userStorage.registerUser(username, password));
    }

    @Test
    public void testRegisterUserStorageContainsUser() {
        assertFalse("Expected false when new user is added.", userStorage.registerUser(testUser, testPassword));
    }

    @Test
    public void testLoginUserSuccess() {
        assertTrue("Expected true when registered user login.", userStorage.loginUser(testUser, testPassword));
    }

    @Test
    public void testLoginUserWrongPassword() {
        final String password = "456";
        assertFalse("Expected true when registered user login with wrong password.",
                userStorage.loginUser(testUser, password));
    }

    @Test
    public void testLoginUserNotRegistered() {
        final String username = "Paul";
        final String password = "456";
        assertFalse("Expected true when registered user login with wrong password.",
                userStorage.loginUser(username, password));
    }

    @Test
    public void testIsLoggedWithNotLoggedUser() {
        assertFalse("Expected false when new user is not logged.", userStorage.isUserLogged());
    }

    @Test
    public void testIsLoggedWithLoggedUser() {
        userStorage.loginUser(testUser, testPassword);
        assertTrue("Expected true when user is logged.", userStorage.isUserLogged());
    }

}
