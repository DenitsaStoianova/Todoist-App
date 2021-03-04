package bg.sofia.uni.fmi.mjt.todoist.storage.content.task.manager;

import bg.sofia.uni.fmi.mjt.todoist.command.enums.CommandKeywords;
import bg.sofia.uni.fmi.mjt.todoist.storage.content.task.Task;

import java.util.Map;

public class InboxTaskManager<T extends Task> implements TaskManager {
    private final Map<String, T> tasks;
    private final Map<CommandKeywords, String> taskComponents;
    private final TasksWorker<T> tasksWorker;

    public InboxTaskManager(Map<CommandKeywords, String> taskComponents, Map<String, T> tasks) {
        this.taskComponents = taskComponents;
        this.tasks = tasks;
        this.tasksWorker = new TasksWorker<>();
    }

    @Override
    public boolean updateTask() {
        return tasksWorker.updateTask(tasks, taskComponents);
    }

    @Override
    public boolean deleteTask() {
        return tasksWorker.deleteTask(tasks, taskComponents.get(CommandKeywords.NAME));
    }

    @Override
    public String getTask() {
        return tasksWorker.getTask(tasks, taskComponents.get(CommandKeywords.NAME));
    }

    @Override
    public String listTasks() {
        return tasksWorker.listTasks(tasks, taskComponents);
    }

    @Override
    public String listDashboard() {
        return tasksWorker.listTasks(tasks, taskComponents);
    }

    @Override
    public boolean finishTask() {
        return tasksWorker.finishTask(tasks, taskComponents.get(CommandKeywords.NAME));
    }

    @Override
    public void labelTask() {
        tasksWorker.labelTask(tasks, taskComponents);
    }

    @Override
    public Task getTaskData() {
        return tasksWorker.getTaskData(tasks, taskComponents.get(CommandKeywords.NAME));
    }
}
