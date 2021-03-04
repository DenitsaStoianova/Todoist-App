package bg.sofia.uni.fmi.mjt.todoist.storage.content.task.taskstorage;

import bg.sofia.uni.fmi.mjt.todoist.command.enums.CommandKeywords;
import bg.sofia.uni.fmi.mjt.todoist.storage.content.collaboration.Collaboration;
import bg.sofia.uni.fmi.mjt.todoist.storage.content.task.CollaborationInboxTask;
import bg.sofia.uni.fmi.mjt.todoist.storage.content.task.CollaborationOrdinaryTask;
import bg.sofia.uni.fmi.mjt.todoist.storage.content.task.TaskCreator;
import bg.sofia.uni.fmi.mjt.todoist.storage.content.task.manager.InboxTaskManager;
import bg.sofia.uni.fmi.mjt.todoist.storage.content.task.manager.OrdinaryTaskManager;
import bg.sofia.uni.fmi.mjt.todoist.storage.content.task.manager.TaskManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class CollaborationTaskStorage {
    protected static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
    private final Map<String, CollaborationInboxTask> inboxTasks;
    private final Map<LocalDate, Map<String, CollaborationOrdinaryTask>> ordinaryTasks;
    private TaskManager taskManager;

    public CollaborationTaskStorage(Collaboration collaboration) {
        this.inboxTasks = collaboration.getInboxTasks();
        this.ordinaryTasks = collaboration.getOrdinaryTasks();
    }

    public void setTaskComponents(Map<CommandKeywords, String> taskComponents) {
        if (taskComponents.containsKey(CommandKeywords.DATE)) {
            this.taskManager = new OrdinaryTaskManager<>(taskComponents, ordinaryTasks);
        } else {
            this.taskManager = new InboxTaskManager<>(taskComponents, inboxTasks);
        }
    }

    public boolean addTask(Map<CommandKeywords, String> taskComponents) {
        String taskName = taskComponents.get(CommandKeywords.NAME);
        if (taskComponents.containsKey(CommandKeywords.DATE)) {
            LocalDate taskDate = LocalDate.parse(taskComponents.get(CommandKeywords.DATE), formatter);
            if (ordinaryTasks.containsKey(taskDate) && ordinaryTasks.get(taskDate).containsKey(taskName)) {
                return false;
            } else if (ordinaryTasks.containsKey(taskDate) && !ordinaryTasks.get(taskDate).containsKey(taskName)) {
                ordinaryTasks.get(taskDate).put(taskName, TaskCreator.createCollaborationOrdinaryTask(taskComponents));
            } else {
                Map<String, CollaborationOrdinaryTask> tasks = new HashMap<>();
                tasks.put(taskName, TaskCreator.createCollaborationOrdinaryTask(taskComponents));
                ordinaryTasks.put(taskDate, tasks);
            }
            return true;
        }
        if (!inboxTasks.containsKey(taskName)) {
            inboxTasks.put(taskName, TaskCreator.createCollaborationInboxTask(taskComponents));
            return true;
        }
        return false;
    }

    public boolean updateTask() {
        return taskManager.updateTask();
    }

    public boolean deleteTask() {
        return taskManager.deleteTask();
    }

    public String getTask() {
        return taskManager.getTask();
    }

    public String listTasks() {
        return taskManager.listTasks();
    }

    public String listDashboard() {
        return taskManager.listDashboard();
    }

    public boolean finishTask() {
        return taskManager.finishTask();
    }
}
