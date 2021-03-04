package bg.sofia.uni.fmi.mjt.todoist.file.executor.content;

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
    private static final String USER_ORDINARY_TASKS_FILE = "/ordinary-tasks.bin";

    @SuppressWarnings("unchecked")
    public Map<LocalDate, Map<String, OrdinaryTask>> loadOrdinaryTasks(String user) {
        Map<LocalDate, Map<String, OrdinaryTask>> ordinaryTasks = new HashMap<>();
        File ordinaryTasksFile = new File(getUserContentPath(user));
        if (ordinaryTasksFile.exists()) {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(ordinaryTasksFile))) {
                ordinaryTasks = (Map<LocalDate, Map<String, OrdinaryTask>>) objectInputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new IllegalStateException(READING_PROBLEM_MSG, e);
            }
        }
        return ordinaryTasks;
    }

    public void saveOrdinaryTasks(String user, Map<LocalDate, Map<String, OrdinaryTask>> ordinaryTasks) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(createFile(user)))) {
            objectOutputStream.writeObject(ordinaryTasks);
        } catch (IOException e) {
            throw new IllegalStateException(WRITING_PROBLEM_MSG, e);
        }
    }

    @Override
    protected String getUserContentPath(String user) {
        return String.format(getUserWorkspaceDir(), user) + USER_ORDINARY_TASKS_FILE;
    }

}
