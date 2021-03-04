package bg.sofia.uni.fmi.mjt.todoist.command.type;

import bg.sofia.uni.fmi.mjt.todoist.command.CommandConstants;
import bg.sofia.uni.fmi.mjt.todoist.command.enums.CommandKeywords;
import bg.sofia.uni.fmi.mjt.todoist.command.enums.CommandType;
import bg.sofia.uni.fmi.mjt.todoist.storage.content.label.LabelStorage;
import bg.sofia.uni.fmi.mjt.todoist.storage.content.task.Task;
import bg.sofia.uni.fmi.mjt.todoist.storage.content.task.taskstorage.TaskStorage;

import java.util.Map;

public class LabelCommand implements Command {
    private final LabelStorage labelStorage;
    private final TaskStorage taskStorage;

    public LabelCommand(LabelStorage labelStorage, TaskStorage taskStorage) {
        this.labelStorage = labelStorage;
        this.taskStorage = taskStorage;
    }

    @Override
    public String execute(CommandType commandType, Map<CommandKeywords, String> commandArguments) {
        return switch (commandType) {
            case ADD_LABEL -> addLabel(commandArguments.get(CommandKeywords.NAME));
            case DELETE_LABEL -> deleteStorageLabel(commandArguments.get(CommandKeywords.NAME));
            case LIST_LABELS -> listStorageLabels();
            case LABEL_TASK -> labelStorageTask(commandArguments);
            case LIST_TASKS -> listStorageTasks(commandArguments.get(CommandKeywords.LABEL));
            default -> CommandConstants.INCORRECT_COMMAND;
        };
    }

    private String addLabel(String name) {
        return labelStorage.addLabel(name) ?
                String.format(CommandConstants.SUCCESS_ADD_LABEL, name)
                : String.format(CommandConstants.LABEL_ALREADY_EXISTS, name);
    }

    private String deleteStorageLabel(String name) {
        return labelStorage.deleteLabel(name) ?
                String.format(CommandConstants.SUCCESS_DELETE_LABEL, name)
                : String.format(CommandConstants.NOT_EXISTING_LABEL, name);
    }

    private String listStorageLabels() {
        String labelsInfo = labelStorage.listLabels();
        return labelsInfo.isEmpty() ?
                CommandConstants.NO_LABELS : labelsInfo;
    }

    private String labelStorageTask(Map<CommandKeywords, String> commandArguments) {
        Task task = taskStorage.getTaskData();
        if (task != null) {
            taskStorage.labelTask();
            return labelStorage.labelTask(task, commandArguments.get(CommandKeywords.LABEL)) ?
                    String.format(CommandConstants.SUCCESS_LABEL_TASK,
                            commandArguments.get(CommandKeywords.NAME), commandArguments.get(CommandKeywords.LABEL))
                    : String.format(CommandConstants.CANNOT_LABEL_TASK,
                    commandArguments.get(CommandKeywords.NAME), commandArguments.get(CommandKeywords.LABEL));
        }
        return String.format(CommandConstants.NOT_EXISTING_TASK, commandArguments.get(CommandKeywords.NAME));
    }

    private String listStorageTasks(String name) {
        String tasksInfo = labelStorage.listTasks(name);
        return tasksInfo.isEmpty() ?
                String.format(CommandConstants.NO_LABEL_TASKS, name) : tasksInfo;
    }

}
