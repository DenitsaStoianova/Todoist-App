package bg.sofia.uni.fmi.mjt.todoist.file.executor.content;

import bg.sofia.uni.fmi.mjt.todoist.storage.user.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class UsersFileExecutor extends FileExecutor {
    private static final String USERS_DATABASE_DIR = "database/Todoist-Users-Database/users.bin";

    @SuppressWarnings("unchecked")
    public Map<String, User> loadUsers() {
        File usersDatabase = new File(getUserContentPath(""));

        Map<String, User> users = new HashMap<>();
        if (usersDatabase.exists()) {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(usersDatabase))) {
                users = (Map<String, User>) objectInputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new IllegalStateException(READING_PROBLEM_MSG, e);
            }
        }
        return users;
    }

    public void saveUsers(Map<String, User> users) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(createFile("")))) {
            objectOutputStream.writeObject(users);
        } catch (IOException e) {
            throw new IllegalStateException(WRITING_PROBLEM_MSG, e);
        }
    }

    @Override
    protected String getUserContentPath(String user) {
        return USERS_DATABASE_DIR;
    }
}
