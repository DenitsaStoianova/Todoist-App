package bg.sofia.uni.fmi.mjt.todoist.file.executor.content;

import bg.sofia.uni.fmi.mjt.todoist.storage.content.collaboration.Collaboration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class CollaborationsFileExecutor extends FileExecutor {
    private static final String USER_COLLABORATIONS_FILE = "/collaborations.bin";

    @SuppressWarnings("unchecked")
    public Map<String, Collaboration> loadCollaborations(String user) {
        Map<String, Collaboration> collaborations = new HashMap<>();
        File inboxTasksFile = new File(getUserContentPath(user));
        if (inboxTasksFile.exists()) {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(inboxTasksFile))) {
                collaborations = (Map<String, Collaboration>) objectInputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new IllegalStateException(READING_PROBLEM_MSG, e);
            }
        }
        return collaborations;
    }

    public void saveCollaborations(String user, Map<String, Collaboration> collaborations) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(createFile(user)))) {
            objectOutputStream.writeObject(collaborations);
        } catch (IOException e) {
            throw new IllegalStateException(WRITING_PROBLEM_MSG, e);
        }
    }

    @Override
    protected String getUserContentPath(String user) {
        return String.format(getUserWorkspaceDir(), user) + USER_COLLABORATIONS_FILE;
    }
}
