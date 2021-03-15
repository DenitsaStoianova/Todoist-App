package bg.sofia.uni.fmi.mjt.todoist.manager;

import bg.sofia.uni.fmi.mjt.todoist.command.enums.CommandKeywords;
import bg.sofia.uni.fmi.mjt.todoist.storage.content.task.Task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class OrdinaryTaskManager<T extends Task> implements TaskManager {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
    private final Map<LocalDate, Map<String, T>> ordinaryTasks;
    private final Map<CommandKeywords, String> taskComponents;
    private final TasksWorker<T> tasksWorker;

    public OrdinaryTaskManager(Map<CommandKeywords, String> taskComponents,
                               Map<LocalDate, Map<String, T>> ordinaryTasks) {
        this.taskComponents = taskComponents;
        this.ordinaryTasks = ordinaryTasks;
        this.tasksWorker = new TasksWorker<>();
    }

    @Override
    public boolean updateTask() {
        LocalDate date = LocalDate.parse(taskComponents.get(CommandKeywords.DATE), formatter);
        if (ordinaryTasks.containsKey(date)) {
            return tasksWorker.updateTask(ordinaryTasks.get(date), taskComponents);
        }
        return false;
    }

    @Override
    public boolean deleteTask() {
        LocalDate date = LocalDate.parse(taskComponents.get(CommandKeywords.DATE), formatter);
        if (ordinaryTasks.containsKey(date)) {
            return tasksWorker.deleteTask(ordinaryTasks.get(date), taskComponents.get(CommandKeywords.NAME));
        }
        return false;
    }

    @Override
    public String getTask() {
        LocalDate date = LocalDate.parse(taskComponents.get(CommandKeywords.DATE), formatter);
        if (ordinaryTasks.containsKey(date)) {
            return tasksWorker.getTask(ordinaryTasks.get(date), taskComponents.get(CommandKeywords.NAME));
        }
        return null;
    }

    @Override
    public String listTasks() {
        LocalDate date = LocalDate.parse(taskComponents.get(CommandKeywords.DATE), formatter);
        if (ordinaryTasks.containsKey(date)) {
            return tasksWorker.listTasks(ordinaryTasks.get(date), taskComponents);
        }
        return "";
    }

    @Override
    public String listDashboard() {
        Map<String, T> map = (Map<String, T>) ordinaryTasks.values();
        return tasksWorker.listDashboard(map);
    }

    @Override
    public boolean finishTask() {
        LocalDate date = LocalDate.parse(taskComponents.get(CommandKeywords.DATE), formatter);
        if (ordinaryTasks.containsKey(date)) {
            return tasksWorker.finishTask(ordinaryTasks.get(date), taskComponents.get(CommandKeywords.NAME));
        }
        return false;
    }

    @Override
    public void labelTask() {
        LocalDate date = LocalDate.parse(taskComponents.get(CommandKeywords.DATE), formatter);
        tasksWorker.labelTask(ordinaryTasks.get(date), taskComponents);
    }

    @Override
    public Task getTaskData() {
        LocalDate date = LocalDate.parse(taskComponents.get(CommandKeywords.DATE), formatter);
        if (ordinaryTasks.containsKey(date)) {
            return tasksWorker.getTaskData(ordinaryTasks.get(date), taskComponents.get(CommandKeywords.NAME));
        }
        return null;
    }
}
