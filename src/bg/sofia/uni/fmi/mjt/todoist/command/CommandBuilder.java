package bg.sofia.uni.fmi.mjt.todoist.command;

import bg.sofia.uni.fmi.mjt.todoist.command.enums.CommandKeywords;
import bg.sofia.uni.fmi.mjt.todoist.command.enums.CommandType;
import bg.sofia.uni.fmi.mjt.todoist.command.enums.EnumEvaluator;
import bg.sofia.uni.fmi.mjt.todoist.exceptions.InvalidCommandArgumentsException;
import bg.sofia.uni.fmi.mjt.todoist.exceptions.NullCommandException;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class CommandBuilder {
    private static final String NULL_COMMAND = "Command cannot be null.";
    private static final String INVALID_COMMAND_ARGS
            = "Invalid command arguments. Dou you know correct command syntax? "
            + "Press \"help-task\", \"help-collaboration\" or \"help-label\" to look at it.";

    private static final String commandPatternString = "(\\s<([^<>]+)=([^<>]+)>)*";
    private static final String commandArgumentsDelimiters = "(\\s<)|(>\\s<)|>";
    private static final String argumentTokensDelimiter = "=";
    private static final int tokenNameIndex = 0;
    private static final int tokenValueIndex = 1;

    public static Map<CommandKeywords, String> getCommandArguments(String command)
            throws InvalidCommandArgumentsException, NullCommandException {
        if (command == null) {
            throw new NullCommandException(NULL_COMMAND);
        }
        String commandName = getCommandName(command);
        String commandArguments = command.replace(commandName, "");
        if (!checkCommandArguments(commandArguments)) {
            throw new InvalidCommandArgumentsException(INVALID_COMMAND_ARGS);
        }

        Map<CommandKeywords, String> commandArgumentsMap = new HashMap<>();
        String[] argumentTokens;
        String[] commandArgumentsArray = commandArguments.split(commandArgumentsDelimiters);
        for (int i = 1; i < commandArgumentsArray.length; i++) {
            argumentTokens = commandArgumentsArray[i].split(argumentTokensDelimiter);
            CommandKeywords keyword = EnumEvaluator.getValueOf(CommandKeywords.class, argumentTokens[tokenNameIndex]);
            commandArgumentsMap.put(keyword, argumentTokens[tokenValueIndex]);
        }

        return commandArgumentsMap;
    }

    public static CommandType getCommandType(String command) {
        return EnumEvaluator.getValueOf(CommandType.class, getCommandName(command));
    }

    private static String getCommandName(String command) {
        return command.substring(0, command.contains(" ") ? command.indexOf(" ") : command.length());
    }

    private static boolean checkCommandArguments(String commandArguments) {
        Pattern commandPattern = Pattern.compile(commandPatternString);
        return commandPattern.matcher(commandArguments).matches();
    }
}