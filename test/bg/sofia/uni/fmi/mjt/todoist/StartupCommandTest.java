package bg.sofia.uni.fmi.mjt.todoist;

import bg.sofia.uni.fmi.mjt.todoist.command.StartupCommand;
import bg.sofia.uni.fmi.mjt.todoist.exceptions.InvalidCommandArgumentsException;
import bg.sofia.uni.fmi.mjt.todoist.exceptions.NullCommandException;
import bg.sofia.uni.fmi.mjt.todoist.storage.user.UserStorage;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StartupCommandTest {
    private static UserStorage userStorage;
    private static StartupCommand startupCommand;

    @BeforeClass
    public static void setUp() {
        userStorage = mock(UserStorage.class);
        startupCommand = new StartupCommand(userStorage);
    }

    @Test
    public void testRegisterUserSuccess() throws InvalidCommandArgumentsException, NullCommandException {
        when(userStorage.registerUser("Kevin", "123")).thenReturn(true);
        final String expected = "[ You were registered successfully. Welcome in Todoist, login to manage your plans! ]";
        String actual = startupCommand.execute("register <username=Kevin> <password=123>");
        assertEquals("Register command message is not correct.", expected, actual);
    }

    @Test
    public void testRegisterUserAlreadyTakenUsername() throws InvalidCommandArgumentsException, NullCommandException {
        when(userStorage.registerUser("Kevin", "123")).thenReturn(false);
        final String expected = "[ Username \"Kevin\" is already taken, please select another one. ]";
        String actual = startupCommand.execute("register <username=Kevin> <password=123>");
        assertEquals("Register command message is not correct.", expected, actual);
    }

    @Test
    public void testLoginUserSuccess() throws InvalidCommandArgumentsException, NullCommandException {
        when(userStorage.loginUser("Kevin", "123")).thenReturn(true);
        final String expected = "[ You are successfully logged in and you can manage your tasks. ]";
        String actual = startupCommand.execute("login <username=Kevin> <password=123>");
        assertEquals("Login command message is not correct.", expected, actual);
    }

    @Test
    public void testLoginUserInvalidCombination() throws InvalidCommandArgumentsException, NullCommandException {
        when(userStorage.loginUser("Kevin", "123")).thenReturn(false);
        final String expected = "[ Invalid password/username combination. ]";
        String actual = startupCommand.execute("login <username=Kevin> <password=123>");
        assertEquals("Login command message is not correct.", expected, actual);
    }

    @Test
    public void testLogoutUserSuccess() throws InvalidCommandArgumentsException, NullCommandException {
        when(userStorage.isUserLogged()).thenReturn(true);
        final String expected = "[ You are successfully logged out. ]";
        String actual = startupCommand.execute("logout");
        assertEquals("Logout command message is not correct.", expected, actual);
    }

    @Test
    public void testLogoutUserNotLoggedIn() throws InvalidCommandArgumentsException, NullCommandException {
        when(userStorage.isUserLogged()).thenReturn(false);
        final String expected = "[ You are not logged in. ]";
        String actual = startupCommand.execute("logout");
        assertEquals("Logout command message is not correct.", expected, actual);
    }

    @Test
    public void testDisconnectUser() throws InvalidCommandArgumentsException, NullCommandException {
        final String expected = "[ Disconnected from server. ]";
        String actual = startupCommand.execute("disconnect");
        assertEquals("Disconnect command message is not correct.", expected, actual);
    }

}
