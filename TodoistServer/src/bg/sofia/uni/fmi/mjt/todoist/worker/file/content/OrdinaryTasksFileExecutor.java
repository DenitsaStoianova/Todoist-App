package bg.sofia.uni.fmi.mjt.todoist.worker.file.content;

import bg.sofia.uni.fmi.mjt.todoist.storage.content.task.OrdinaryTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class OrdinaryTasksFileExecutor extends FileExecutor {
    private static final String USER_ORDINARY_TASKS_FILE = "/Tasks/ordinary-tasks.bin";

    @SuppressWarnings("unchecked")
    public Map<LocalDate, Map<String, OrdinaryTask>> loadOrdinaryTasks(String user) throws IOException, ClassNotFoundException {
        Map<LocalDate, Map<String, OrdinaryTask>> ordinaryTasks = new HashMap<>();
        File ordinaryTasksFile = new File(getUserContentPath(user));
        if (ordinaryTasksFile.exists()) {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(ordinaryTasksFile))) {
                ordinaryTasks = (Map<LocalDate, Map<String, OrdinaryTask>>) objectInputStream.readObject();
            }
        }
        return ordinaryTasks;
    }

    public void saveOrdinaryTasks(String user, Map<LocalDate, Map<String, OrdinaryTask>> ordinaryTasks) throws IOException {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                new FileOutputStream(createFile(getUserContentPath(user))))) {
            objectOutputStream.writeObject(ordinaryTasks);
        }
    }

    @Override
    protected String getUserContentPath(String user) {
        return String.format(getUserWorkspaceDir(), user) + USER_ORDINARY_TASKS_FILE;
    }

}
