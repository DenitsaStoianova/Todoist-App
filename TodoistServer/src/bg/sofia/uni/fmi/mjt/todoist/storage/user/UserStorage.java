package bg.sofia.uni.fmi.mjt.todoist.storage.user;

import bg.sofia.uni.fmi.mjt.todoist.worker.file.content.UsersFileExecutor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserStorage {
    private String currentlyLoggedUser = "";
    private Map<String, User> users;
    private final UsersFileExecutor usersFileExecutor;
    private final int usersCount;

    public UserStorage()  {
        this.users = new HashMap<>();
        this.usersFileExecutor = new UsersFileExecutor();
        try {
            this.users = usersFileExecutor.loadUsers();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        this.usersCount = users.size();
    }

    public boolean registerUser(String username, String password) {
        if (!users.containsKey(username)) {
            users.put(username, new User(username, password));
            return true;
        }
        return false;
    }

    public void saveUsers() throws IOException {
        if(usersCount != users.size()) {
            usersFileExecutor.saveUsers(users);
        }
    }

    public boolean loginUser(String username, String password) {
        if (users.containsKey(username) && users.get(username).checkPassword(password)) {
            this.currentlyLoggedUser = username;
            return true;
        }
        return false;
    }

    public String logoutCurrentUser() {
        String user = currentlyLoggedUser;
        this.currentlyLoggedUser = "";
        return user;
    }

    public boolean isUserLogged() {
        return !currentlyLoggedUser.isEmpty();
    }

}
