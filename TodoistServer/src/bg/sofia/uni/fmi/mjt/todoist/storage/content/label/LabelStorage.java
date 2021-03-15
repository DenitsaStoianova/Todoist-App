package bg.sofia.uni.fmi.mjt.todoist.storage.content.label;

import bg.sofia.uni.fmi.mjt.todoist.storage.Storage;
import bg.sofia.uni.fmi.mjt.todoist.storage.content.task.Task;
import bg.sofia.uni.fmi.mjt.todoist.worker.file.content.LabelFileExecutor;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class LabelStorage implements Storage {
    private final LabelFileExecutor labelFileExecutor;
    private Map<String, Label> labeledTasks;

    public LabelStorage() {
        this.labelFileExecutor = new LabelFileExecutor();
        this.labeledTasks = new TreeMap<>();
    }

    public boolean addLabel(String name) {
        if (!labeledTasks.containsKey(name)) {
            labeledTasks.put(name, new Label(name));
            return true;
        }
        return false;
    }

    public boolean deleteLabel(String name) {
        if (labeledTasks.containsKey(name)) {
            labeledTasks.get(name).removeLabelFromTasks();
            labeledTasks.remove(name);
            return true;
        }
        return false;
    }

    public String listLabels() {
        return labeledTasks.keySet().toString();
    }

    public boolean labelTask(Task task, String label) {
        if (labeledTasks.containsKey(label)) {
            task.labelTask(label);
            labeledTasks.get(label).addTask(task);
            return true;
        }
        return false;
    }

    public String listTasks(String label) {
        return (labeledTasks.containsKey(label) && !labeledTasks.get(label).getTasks().isEmpty()) ?
                labeledTasks.get(label).getTasks().stream()
                        .map(Task::toString).reduce((s1, s2) -> s1 + System.lineSeparator() + s2).orElse("") : null;
    }

    @Override
    public void loadContent(String user) throws IOException, ClassNotFoundException {
        labeledTasks = labelFileExecutor.loadLabels(user);
    }

    @Override
    public void saveContent(String user) throws IOException {
        labelFileExecutor.saveLabels(user, labeledTasks);
    }
}
