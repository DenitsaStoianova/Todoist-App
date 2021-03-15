package bg.sofia.uni.fmi.mjt.todoist.worker.file.content;

import bg.sofia.uni.fmi.mjt.todoist.storage.content.collaboration.Collaboration;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class CollaborationParticipatedFileExecutor extends CollaborationFileExecutor {
    private static final String COLLABORATIONS_FILE = "/Collaborations/Participated/";
    private static final String DELIMITER = "-";

    public void saveCollaborations(Map<String, Collaboration> collaborations) throws IOException {
        for (Collaboration collaboration : collaborations.values()) {
            for (String participant : collaboration.getParticipants()) {
                try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                        new FileOutputStream(createFile(getParticipantCollaborationPath(collaboration, participant))))) {
                    objectOutputStream.writeObject(collaboration);
                }
            }
        }
    }

    public void removeCollaborationFile(String participant, Collaboration collaboration) throws IOException {
        String collaborationFilePath = getParticipantCollaborationPath(collaboration, participant);
        if (Files.exists(Paths.get(collaborationFilePath))) {
            Files.delete(Path.of(collaborationFilePath));
        }
    }

    private String getParticipantCollaborationPath(Collaboration collaboration, String participant) {
        return getUserContentPath(participant)
                + collaboration.getCreator()
                + DELIMITER
                + collaboration.getName().replace(" ", DELIMITER)
                + FILE_EXTENSION;
    }

    @Override
    protected String getUserContentPath(String user) {
        return String.format(getUserWorkspaceDir(), user) + COLLABORATIONS_FILE;
    }
}
