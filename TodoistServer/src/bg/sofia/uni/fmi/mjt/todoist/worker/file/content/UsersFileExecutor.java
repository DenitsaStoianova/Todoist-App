package bg.sofia.uni.fmi.mjt.todoist.worker.file.content;

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
    public Map<String, User> loadUsers() throws IOException, ClassNotFoundException {
        File usersDatabase = new File(getUserContentPath(""));

        Map<String, User> users = new HashMap<>();
        if (usersDatabase.exists()) {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(usersDatabase))) {
                users = (Map<String, User>) objectInputStream.readObject();
            }
        }
        return users;
    }

    public void saveUsers(Map<String, User> users) throws IOException {
        try (ObjectOutputStream objectOutputStream =
                     new ObjectOutputStream(new FileOutputStream(createFile(getUserContentPath(""))))) {
            objectOutputStream.writeObject(users);
        }
    }

    @Override
    protected String getUserContentPath(String user) {
        return USERS_DATABASE_DIR;
    }
}
