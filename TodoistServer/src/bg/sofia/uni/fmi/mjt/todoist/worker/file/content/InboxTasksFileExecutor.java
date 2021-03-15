package bg.sofia.uni.fmi.mjt.todoist.worker.file.content;

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
    private static final String USER_INBOX_TASKS_FILE = "/Tasks/inbox-tasks.bin";

    @SuppressWarnings("unchecked")
    public Map<String, InboxTask> loadInboxTasks(String user) throws IOException, ClassNotFoundException {
        Map<String, InboxTask> inboxTasks = new HashMap<>();
        File inboxTasksFile = new File(getUserContentPath(user));
        if (inboxTasksFile.exists()) {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(inboxTasksFile))) {
                inboxTasks = (Map<String, InboxTask>) objectInputStream.readObject();
            }
        }
        return inboxTasks;
    }

    public void saveInboxTasks(String user, Map<String, InboxTask> inboxTasks) throws IOException {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                new FileOutputStream(createFile(getUserContentPath(user))))) {
            objectOutputStream.writeObject(inboxTasks);
        }
    }

    @Override
    protected String getUserContentPath(String user) {
        return String.format(getUserWorkspaceDir(), user) + USER_INBOX_TASKS_FILE;
    }
}
