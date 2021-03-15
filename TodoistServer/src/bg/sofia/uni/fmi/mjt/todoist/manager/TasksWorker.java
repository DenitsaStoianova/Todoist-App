package bg.sofia.uni.fmi.mjt.todoist.manager;

import bg.sofia.uni.fmi.mjt.todoist.command.enums.CommandKeywords;
import bg.sofia.uni.fmi.mjt.todoist.storage.content.task.Task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Stream;

public class TasksWorker<T extends Task> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
    private static final String COMPLETED_TASK = "true";

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
        return tasks.containsKey(name) ? tasks.get(name).toString() : null;
    }

    public String listTasks(Map<String, T> inboxTasks, Map<CommandKeywords, String> taskComponents) {
        Stream<T> stringStream = taskComponents.containsKey(CommandKeywords.COMPLETED) ?
                inboxTasks.values().stream()
                        .filter(taskComponents.get(CommandKeywords.COMPLETED).equals(COMPLETED_TASK) ?
                                Task::isFinished : t -> !t.isFinished()) : inboxTasks.values().stream();
        return stringStream.map(Task::toString).reduce((t1, t2) -> t1 + System.lineSeparator() + t2).orElse("");
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
