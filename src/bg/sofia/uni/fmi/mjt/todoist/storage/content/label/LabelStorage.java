package bg.sofia.uni.fmi.mjt.todoist.storage.content.label;

import bg.sofia.uni.fmi.mjt.todoist.storage.Storage;
import bg.sofia.uni.fmi.mjt.todoist.storage.content.task.Task;
import bg.sofia.uni.fmi.mjt.todoist.file.executor.content.LabelFileExecutor;

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
        if (!labeledTasks.containsKey(name)) {
            labeledTasks.get(name).removeLabelFromTasks();
            labeledTasks.remove(name);
            return true;
        }
        return false;
    }

    public String listLabels() {
        return labeledTasks.keySet().stream().reduce((l1, l2) -> l1 + ", " + l2).orElse("");
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
        StringBuilder tasksBuilder = new StringBuilder();
        if (labeledTasks.containsKey(label) && !labeledTasks.get(label).getTasks().isEmpty()) {
            labeledTasks.get(label).getTasks().forEach(tasksBuilder::append);
        }
        return tasksBuilder.toString();
    }

    @Override
    public void loadContent(String user) {
        labeledTasks = labelFileExecutor.loadLabels(user);
    }

    @Override
    public void saveContent(String user) {
        labelFileExecutor.saveLabels(user, labeledTasks);
    }
}
