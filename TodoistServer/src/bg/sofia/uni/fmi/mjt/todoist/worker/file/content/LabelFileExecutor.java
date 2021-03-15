package bg.sofia.uni.fmi.mjt.todoist.worker.file.content;

import bg.sofia.uni.fmi.mjt.todoist.storage.content.label.Label;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.TreeMap;

public class LabelFileExecutor extends FileExecutor{
    private static final String LABELS_FILE = "/Labels/labels.bin";

    @SuppressWarnings("unchecked")
    public  Map<String, Label> loadLabels(String user) throws IOException, ClassNotFoundException {
        Map<String, Label> labeledTasks = new TreeMap<>();
        File labelsFile = new File(getUserContentPath(user));
        if (labelsFile.exists()) {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(labelsFile))) {
                labeledTasks = ( Map<String, Label>) objectInputStream.readObject();
            }
        }
        return labeledTasks;
    }

    public void saveLabels(String user,  Map<String, Label> labeledTasks) throws IOException {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                new FileOutputStream(createFile(getUserContentPath(user))))) {
            objectOutputStream.writeObject(labeledTasks);
        }
    }

    @Override
    protected String getUserContentPath(String user) {
        return String.format(getUserWorkspaceDir(), user) + LABELS_FILE;
    }

}
