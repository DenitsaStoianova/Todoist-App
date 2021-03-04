package bg.sofia.uni.fmi.mjt.todoist.command.type;

import bg.sofia.uni.fmi.mjt.todoist.command.CommandConstants;
import bg.sofia.uni.fmi.mjt.todoist.command.enums.CommandKeywords;
import bg.sofia.uni.fmi.mjt.todoist.command.enums.CommandType;
import bg.sofia.uni.fmi.mjt.todoist.storage.content.collaboration.Collaboration;
import bg.sofia.uni.fmi.mjt.todoist.storage.content.task.taskstorage.CollaborationTaskStorage;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CollaborationTaskCommand {
    private final CollaborationTaskStorage collaborationTaskStorage;
    private final String collaborationName;

    public CollaborationTaskCommand(Collaboration collaboration, String collaborationName) {
        collaborationTaskStorage = new CollaborationTaskStorage(collaboration);
        this.collaborationName = collaborationName;
    }

    public String executeTaskCommands(CommandType commandType, Map<CommandKeywords, String> commandArguments) {
        collaborationTaskStorage.setTaskComponents(commandArguments);
        return switch (commandType) {
            case ADD_TASK -> addTask(commandArguments.get(CommandKeywords.NAME), commandArguments);
            case UPDATE_TASK -> updateTask(commandArguments.get(CommandKeywords.NAME));
            case DELETE_TASK -> deleteTask(commandArguments.get(CommandKeywords.NAME));
            case GET_TASK -> getTask(commandArguments.get(CommandKeywords.NAME));
            case LIST_TASKS -> listTasks();
            case LIST_DASHBOARD -> listDashboard();
            case FINISH_TASK -> finishTask(commandArguments.get(CommandKeywords.NAME));
            default -> CommandConstants.INCORRECT_COMMAND;
        };
    }

    private String addTask(String name, Map<CommandKeywords, String> commandArguments) {
        return collaborationTaskStorage.addTask(commandArguments) ?
                String.format(CommandConstants.SUCCESS_ADD_TASK_COLLABORATION, name, collaborationName) :
                String.format(CommandConstants.ALREADY_EXISTING_TASK_COLLABORATION, name, collaborationName);
    }

    private String updateTask(String name) {
        return collaborationTaskStorage.updateTask() ?
                String.format(CommandConstants.SUCCESS_UPDATE_TASK_COLLABORATION, name, collaborationName) :
                String.format(CommandConstants.NOT_EXISTING_TASK_COLLABORATION, name, collaborationName);
    }

    private String deleteTask(String name) {
        return collaborationTaskStorage.deleteTask() ?
                String.format(CommandConstants.SUCCESS_DELETE_TASK_COLLABORATION, name, collaborationName) :
                String.format(CommandConstants.NOT_EXISTING_TASK_COLLABORATION, name, collaborationName);
    }

    private String getTask(String name) {
        String taskInfo = collaborationTaskStorage.getTask();
        return taskInfo == null ? String.format(CommandConstants.NOT_EXISTING_TASK_COLLABORATION,
                name, collaborationName) : taskInfo;
    }

    private String listTasks() {
        String tasksInfo = collaborationTaskStorage.listTasks();
        return tasksInfo.isEmpty() ? String.format(CommandConstants.NO_TASKS_COLLABORATION, collaborationName)
                : tasksInfo;
    }

    private String listDashboard() {
        String tasksInfo = collaborationTaskStorage.listDashboard();
        return tasksInfo.isEmpty() ? String.format(CommandConstants.NO_DAILY_TASKS_COLLABORATION, collaborationName)
                : tasksInfo;
    }

    private String finishTask(String name) {
        return collaborationTaskStorage.finishTask() ?
                String.format(CommandConstants.SUCCESS_FINISH_TASK_COLLABORATION, name, collaborationName) :
                String.format(CommandConstants.NOT_EXISTING_TASK_COLLABORATION, name, collaborationName);
    }

}
