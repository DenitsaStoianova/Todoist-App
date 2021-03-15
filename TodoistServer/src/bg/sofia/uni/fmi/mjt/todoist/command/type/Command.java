package bg.sofia.uni.fmi.mjt.todoist.command.type;

import bg.sofia.uni.fmi.mjt.todoist.command.enums.CommandKeywords;
import bg.sofia.uni.fmi.mjt.todoist.command.enums.CommandType;

import java.io.IOException;
import java.util.Map;

public interface Command {
    String execute(CommandType commandType, Map<CommandKeywords, String> commandArguments) throws IOException;
}
