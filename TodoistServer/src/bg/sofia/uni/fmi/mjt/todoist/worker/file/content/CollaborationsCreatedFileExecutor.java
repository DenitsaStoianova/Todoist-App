package bg.sofia.uni.fmi.mjt.todoist.worker.file.content;

import bg.sofia.uni.fmi.mjt.todoist.storage.content.collaboration.Collaboration;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class CollaborationsCreatedFileExecutor extends CollaborationFileExecutor {
    private static final String USER_COLLABORATIONS_FILE = "/Collaborations/Created/";

    public void saveCollaborations(String user, Map<String, Collaboration> collaborations) throws IOException {
        for (Map.Entry<String, Collaboration> collaboration : collaborations.entrySet()) {
            try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    new FileOutputStream(createFile(getDstPath(user, collaboration.getKey()))))) {
                objectOutputStream.writeObject(collaboration.getValue());
            }
        }
    }

    public void removeCollaborationFile(String user, String collaboration) throws IOException {
        String collaborationFilePath = getDstPath(user, collaboration);
        if (Files.exists(Paths.get(collaborationFilePath))) {
            Files.delete(Path.of(collaborationFilePath));
        }
    }

    private String getDstPath(String user, String collaboration) {
        return getUserContentPath(user) + collaboration + FILE_EXTENSION;
    }

    @Override
    protected String getUserContentPath(String user) {
        return String.format(getUserWorkspaceDir(), user) + USER_COLLABORATIONS_FILE;
    }
}
