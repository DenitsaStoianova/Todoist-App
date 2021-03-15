package bg.sofia.uni.fmi.mjt.todoist.command.type;

import bg.sofia.uni.fmi.mjt.todoist.command.enums.CommandKeywords;
import bg.sofia.uni.fmi.mjt.todoist.command.enums.CommandType;
import bg.sofia.uni.fmi.mjt.todoist.constants.CommandConstants;
import bg.sofia.uni.fmi.mjt.todoist.storage.content.task.TaskStorage;

import java.util.Map;

public class TaskCommand implements Command {
    private final TaskStorage taskStorage;

    public TaskCommand(TaskStorage taskStorage) {
        this.taskStorage = taskStorage;
    }
    @Override
    public String execute(CommandType commandType, Map<CommandKeywords, String> commandArguments) {
        taskStorage.setTaskComponents(commandArguments);
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
        return taskStorage.addTask(commandArguments) ?
                String.format(CommandConstants.SUCCESS_ADD_TASK, name) :
                String.format(CommandConstants.ALREADY_EXISTING_TASK, name);
    }

    private String updateTask(String name) {
        return taskStorage.updateTask() ?
                String.format(CommandConstants.SUCCESS_UPDATE_TASK, name) :
                String.format(CommandConstants.NOT_EXISTING_TASK, name);
    }

    private String deleteTask(String name) {
        return taskStorage.deleteTask() ?
                String.format(CommandConstants.SUCCESS_DELETE_TASK, name) :
                String.format(CommandConstants.NOT_EXISTING_TASK, name);
    }

    private String getTask(String name) {
        String taskInfo = taskStorage.getTask();
        return taskInfo == null ? String.format(CommandConstants.NOT_EXISTING_TASK, name) : taskInfo;
    }

    private String listTasks() {
        String tasksInfo = taskStorage.listTasks();
        return tasksInfo.isEmpty() ? CommandConstants.NO_TASKS : tasksInfo;
    }

    private String listDashboard() {
        String tasksInfo = taskStorage.listDashboard();
        return tasksInfo.isEmpty() ? CommandConstants.NO_DAILY_TASKS : tasksInfo;
    }

    private String finishTask(String name) {
        return taskStorage.finishTask() ?
                String.format(CommandConstants.SUCCESS_FINISH_TASK, name) :
                String.format(CommandConstants.NOT_EXISTING_TASK, name);
    }
}
