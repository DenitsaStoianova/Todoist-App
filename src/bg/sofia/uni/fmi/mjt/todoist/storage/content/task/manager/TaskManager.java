package bg.sofia.uni.fmi.mjt.todoist.storage.content.task.manager;

import bg.sofia.uni.fmi.mjt.todoist.storage.content.task.Task;

public interface TaskManager {
    boolean updateTask();

    boolean deleteTask();

    String getTask();

    String listTasks();

    String listDashboard();

    boolean finishTask();

    void labelTask();

    Task getTaskData();
}
