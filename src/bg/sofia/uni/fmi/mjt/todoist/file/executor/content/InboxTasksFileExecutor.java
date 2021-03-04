package bg.sofia.uni.fmi.mjt.todoist.file.executor.content;

import bg.sofia.uni.fmi.mjt.todoist.storage.content.task.InboxTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class InboxTasksFileExecutor extends FileExecutor {
    private static final String USER_INBOX_TASKS_FILE = "/inbox-tasks.bin";

    @SuppressWarnings("unchecked")
    public Map<String, InboxTask> loadInboxTasks(String user) {
        Map<String, InboxTask> inboxTasks = new HashMap<>();
        File inboxTasksFile = new File(getUserContentPath(user));
        if (inboxTasksFile.exists()) {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(inboxTasksFile))) {
                inboxTasks = (Map<String, InboxTask>) objectInputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new IllegalStateException(READING_PROBLEM_MSG, e);
            }
        }
        return inboxTasks;
    }

    public void saveInboxTasks(String user, Map<String, InboxTask> inboxTasks) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(createFile(user)))) {
            objectOutputStream.writeObject(inboxTasks);
        } catch (IOException e) {
            throw new IllegalStateException(WRITING_PROBLEM_MSG, e);
        }
    }

    @Override
    protected String getUserContentPath(String user) {
        return String.format(getUserWorkspaceDir(), user) + USER_INBOX_TASKS_FILE;
    }
}
