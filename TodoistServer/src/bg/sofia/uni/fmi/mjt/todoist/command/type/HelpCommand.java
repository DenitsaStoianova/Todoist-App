package bg.sofia.uni.fmi.mjt.todoist.command.type;

import bg.sofia.uni.fmi.mjt.todoist.command.enums.CommandKeywords;
import bg.sofia.uni.fmi.mjt.todoist.command.enums.CommandType;
import bg.sofia.uni.fmi.mjt.todoist.constants.CommandConstants;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class HelpCommand implements Command {
    protected static final String READING_PROBLEM_MSG = "A problem occurred while reading from help file.";
    private static final String HELP_TASK_DIR = "database/Todoist-System-Database/help-task.txt";
    private static final String HELP_COLLABORATION_DIR = "database/Todoist-System-Database/help-collaboration.txt";
    private static final String HELP_LABEL_DIR = "database/Todoist-System-Database/help-label.txt";

    @Override
    public String execute(CommandType commandType, Map<CommandKeywords, String> commandArguments) throws IOException {
        return switch (commandType) {
            case HELP_TASK -> getHelpCommands(HELP_TASK_DIR);
            case HELP_COLLABORATION -> getHelpCommands(HELP_COLLABORATION_DIR);
            case HELP_LABEL -> getHelpCommands(HELP_LABEL_DIR);
            default -> CommandConstants.INCORRECT_COMMAND;
        };
    }

    private String getHelpCommands(String path) throws IOException {
        StringBuilder commandBuilder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            String fileLine;
            while ((fileLine = bufferedReader.readLine()) != null) {
                commandBuilder.append(fileLine).append(System.lineSeparator());
            }
        }
        return commandBuilder.toString();
    }
}
