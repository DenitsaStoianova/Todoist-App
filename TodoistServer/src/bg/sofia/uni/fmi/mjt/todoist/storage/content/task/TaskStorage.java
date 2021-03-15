package bg.sofia.uni.fmi.mjt.todoist.storage.content.task;

import bg.sofia.uni.fmi.mjt.todoist.command.enums.CommandKeywords;
import bg.sofia.uni.fmi.mjt.todoist.creator.TaskCreator;
import bg.sofia.uni.fmi.mjt.todoist.storage.Storage;
import bg.sofia.uni.fmi.mjt.todoist.manager.InboxTaskManager;
import bg.sofia.uni.fmi.mjt.todoist.manager.OrdinaryTaskManager;
import bg.sofia.uni.fmi.mjt.todoist.manager.TaskManager;
import bg.sofia.uni.fmi.mjt.todoist.worker.file.content.InboxTasksFileExecutor;
import bg.sofia.uni.fmi.mjt.todoist.worker.file.content.OrdinaryTasksFileExecutor;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class TaskStorage implements Storage {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
    private static final String type = "inbox";
    private Map<String, InboxTask> inboxTasks;
    private Map<LocalDate, Map<String, OrdinaryTask>> ordinaryTasks;
    private final InboxTasksFileExecutor inboxTasksFileExecutor;
    private final OrdinaryTasksFileExecutor ordinaryTasksFileExecutor;
    private TaskManager taskManager;

    public TaskStorage() {
        this.inboxTasksFileExecutor = new InboxTasksFileExecutor();
        this.ordinaryTasksFileExecutor = new OrdinaryTasksFileExecutor();
        this.inboxTasks = new HashMap<>();
        this.ordinaryTasks = new HashMap<>();
    }

    public void setTaskComponents(Map<CommandKeywords, String> taskComponents) {
        if(taskComponents.containsKey(CommandKeywords.TYPE)){
            this.taskManager = taskComponents.get(CommandKeywords.TYPE).equals(type) ?
                    new InboxTaskManager<>(taskComponents, inboxTasks):
                    new OrdinaryTaskManager<>(taskComponents, ordinaryTasks);
        } else{
            this.taskManager = taskComponents.containsKey(CommandKeywords.DATE) ?
                    new OrdinaryTaskManager<>(taskComponents, ordinaryTasks) :
                    new InboxTaskManager<>(taskComponents, inboxTasks);
        }
    }

    public boolean addTask(Map<CommandKeywords, String> taskComponents) {
        String taskName = taskComponents.get(CommandKeywords.NAME);
        if (taskComponents.containsKey(CommandKeywords.DATE)) {
            LocalDate taskDate = LocalDate.parse(taskComponents.get(CommandKeywords.DATE), formatter);
            if (ordinaryTasks.containsKey(taskDate) && ordinaryTasks.get(taskDate).containsKey(taskName)) {
                return false;
            } else if (ordinaryTasks.containsKey(taskDate) && !ordinaryTasks.get(taskDate).containsKey(taskName)) {
                ordinaryTasks.get(taskDate).put(taskName, TaskCreator.createOrdinaryTask(taskComponents));
            } else {
                Map<String, OrdinaryTask> tasks = new HashMap<>();
                tasks.put(taskName, TaskCreator.createOrdinaryTask(taskComponents));
                ordinaryTasks.put(taskDate, tasks);
            }
            return true;
        }
        if (!inboxTasks.containsKey(taskName)) {
            inboxTasks.put(taskName, TaskCreator.createInboxTask(taskComponents));
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

    public void labelTask() {
         taskManager.labelTask();
    }

    public Task getTaskData() {
        return taskManager.getTaskData();
    }

    @Override
    public void loadContent(String user) throws IOException, ClassNotFoundException {
        inboxTasks = inboxTasksFileExecutor.loadInboxTasks(user);
        ordinaryTasks = ordinaryTasksFileExecutor.loadOrdinaryTasks(user);
    }

    @Override
    public void saveContent(String user) throws IOException {
        inboxTasksFileExecutor.saveInboxTasks(user, inboxTasks);
        ordinaryTasksFileExecutor.saveOrdinaryTasks(user, ordinaryTasks);
    }
}
