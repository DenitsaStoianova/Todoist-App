package bg.sofia.uni.fmi.mjt.todoist.storage.content.task.manager;

import bg.sofia.uni.fmi.mjt.todoist.command.enums.CommandKeywords;
import bg.sofia.uni.fmi.mjt.todoist.storage.content.task.Task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class TasksWorker<T extends Task> {
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
    public static final String COMPLETED_TASK = "true";

    public boolean updateTask(Map<String, T> tasks, Map<CommandKeywords, String> taskComponents) {
        if (tasks.containsKey(taskComponents.get(CommandKeywords.NAME))) {
            for (Map.Entry<CommandKeywords, String> component : taskComponents.entrySet()) {
                switch (component.getKey()) {
                    case DUE_DATE -> tasks.get(taskComponents.get(CommandKeywords.NAME))
                            .setDueDate(LocalDate.parse(component.getValue(), formatter));
                    case DESCRIPTION -> tasks.get(taskComponents.get(CommandKeywords.NAME))
                            .setDescription(component.getValue());
                }
            }
            return true;
        }
        return false;
    }

    public boolean deleteTask(Map<String, T> tasks, String name) {
        if (tasks.containsKey(name)) {
            tasks.remove(name);
            return true;
        }
        return false;
    }

    public String getTask(Map<String, T> tasks, String name) {
        if (tasks.containsKey(name)) {
            return tasks.get(name).toString();
        }
        return null;
    }

    public String listTasks(Map<String, T> inboxTasks, Map<CommandKeywords, String> taskComponents) {
        StringBuilder stringBuilder = new StringBuilder();
        if (taskComponents.containsKey(CommandKeywords.COMPLETED)) {
            if (taskComponents.get(CommandKeywords.COMPLETED).equals(COMPLETED_TASK)) {
                inboxTasks.values().stream()
                        .filter(Task::isFinished)
                        .forEach(stringBuilder::append);
            } else {
                inboxTasks.values().stream()
                        .filter(t -> !t.isFinished())
                        .forEach(stringBuilder::append);
            }
        } else {
            inboxTasks.values().forEach(stringBuilder::append);
        }

        return stringBuilder.toString();
    }

    public String listDashboard(Map<String, T> inboxTasks) {
        StringBuilder stringBuilder = new StringBuilder();
        inboxTasks.values()
                .stream()
                .filter(t -> t.getDueDate().equals(LocalDate.now()))
                .forEach(stringBuilder::append);
        return stringBuilder.toString();
    }

    public boolean finishTask(Map<String, T> inboxTasks, String name) {
        if (inboxTasks.containsKey(name)) {
            inboxTasks.get(name).finishTask();
            return true;
        }
        return false;
    }

    public void labelTask(Map<String, T> inboxTasks, Map<CommandKeywords, String> taskComponents) {
        inboxTasks.get(taskComponents.get(CommandKeywords.NAME))
                .labelTask(taskComponents.get(CommandKeywords.LABEL));
    }

    public Task getTaskData(Map<String, T> inboxTasks, String name) {
        return inboxTasks.get(name);
    }
}
