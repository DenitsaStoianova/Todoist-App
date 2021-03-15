package bg.sofia.uni.fmi.mjt.todoist.storage.content.label;

import bg.sofia.uni.fmi.mjt.todoist.storage.content.task.Task;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Label implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String name;
    private final List<Task> tasks;

    public Label(String name) {
        this.name = name;
        this.tasks = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addTask(Task task) {
        this.tasks.add(task);
    }

    public void removeLabelFromTasks() {
        tasks.forEach(t -> t.removeLabel(name));
    }

    public List<Task> getTasks() {
        return tasks;
    }
}
