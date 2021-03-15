package bg.sofia.uni.fmi.mjt.todoist.worker.file.content;

import bg.sofia.uni.fmi.mjt.todoist.storage.content.collaboration.Collaboration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class CollaborationFileExecutor extends FileExecutor {
    protected static final String FILE_EXTENSION = ".bin";

    public Map<String, Collaboration> loadCollaborations(String user) throws IOException, ClassNotFoundException {
        Map<String, Collaboration> collaborations = new HashMap<>();
        Collaboration collaboration;
        List<Path> collaborationPaths = getCreatedCollaborationsPaths(getUserContentPath(user));
        for (Path path : collaborationPaths) {
            File inboxTasksFile = new File(String.valueOf(path));
            try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(inboxTasksFile))) {
                collaboration = (Collaboration) objectInputStream.readObject();
                collaborations.put(collaboration.getName(), collaboration);
            }
        }
        return collaborations;
    }

    protected List<Path> getCreatedCollaborationsPaths(String folderPath) throws IOException {
        List<Path> collaborationsPaths = new ArrayList<>();
        if (Files.exists(Paths.get(folderPath))) {
            try (Stream<Path> paths = Files.walk(Paths.get(folderPath))) {
                collaborationsPaths = paths.filter(Files::isRegularFile)
                        .collect(Collectors.toList());
            }
        }
        return collaborationsPaths;
    }
}
