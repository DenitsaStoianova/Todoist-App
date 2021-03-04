package bg.sofia.uni.fmi.mjt.todoist.file.executor.content;

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
    private static final String LABELS_FILE = "/labels.bin";

    @SuppressWarnings("unchecked")
    public  Map<String, Label> loadLabels(String user) {
        Map<String, Label> labeledTasks = new TreeMap<>();
        File labelsFile = new File(getUserContentPath(user));
        if (labelsFile.exists()) {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(labelsFile))) {
                labeledTasks = ( Map<String, Label>) objectInputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new IllegalStateException(READING_PROBLEM_MSG, e);
            }
        }
        return labeledTasks;
    }

    public void saveLabels(String user,  Map<String, Label> labeledTasks) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(createFile(user)))) {
            objectOutputStream.writeObject(labeledTasks);
        } catch (IOException e) {
            throw new IllegalStateException(WRITING_PROBLEM_MSG, e);
        }
    }

    @Override
    protected String getUserContentPath(String user) {
        return String.format(getUserWorkspaceDir(), user) + LABELS_FILE;
    }

}
