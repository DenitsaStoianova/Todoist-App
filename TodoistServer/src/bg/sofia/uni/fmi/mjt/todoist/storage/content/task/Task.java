package bg.sofia.uni.fmi.mjt.todoist.storage.content.task;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public abstract class Task implements Serializable {
    private static final String NAME = "[Name: %s]";
    private static final String DUE_DATE = "[Due date: %s]";
    private static final String DESCRIPTION = "[Description: %s]";
    private static final String LABEL = "[Label: %s]";
    private static final String FINISHED = "[Is finished: %s]";
    private static final String DELIMITER = ", ";

    private static final long serialVersionUID = 1L;
    private final String name;
    private LocalDate dueDate;
    private String description;
    private String label;
    private boolean isFinished = false;

    public Task(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void labelTask(String label) {
        this.label = label;
    }

    public void removeLabel(String label) {
        this.label = "";
    }

    public void finishTask() {
        this.isFinished = true;
    }

    public boolean isFinished() {
        return this.isFinished;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Task task = (Task) o;
        return Objects.equals(name, task.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        StringBuilder taskBuilder = new StringBuilder();
        taskBuilder.append(String.format(NAME, name));

        if (dueDate != null) {
            taskBuilder.append(DELIMITER).append(String.format(DUE_DATE, dueDate));
        }
        if (description != null) {
            taskBuilder.append(DELIMITER).append(String.format(DESCRIPTION, description));
        }
        if (label != null) {
            taskBuilder.append(DELIMITER).append(String.format(LABEL, label));
        }
        taskBuilder.append(DELIMITER).append(String.format(FINISHED, isFinished ? "true" : "false"));

        return taskBuilder.toString();
    }
}
