package bg.sofia.uni.fmi.mjt.todoist.command;

import bg.sofia.uni.fmi.mjt.todoist.command.enums.CommandGroup;
import bg.sofia.uni.fmi.mjt.todoist.command.enums.CommandKeywords;
import bg.sofia.uni.fmi.mjt.todoist.command.enums.CommandType;
import bg.sofia.uni.fmi.mjt.todoist.command.type.CollaborationCommand;
import bg.sofia.uni.fmi.mjt.todoist.command.type.Command;
import bg.sofia.uni.fmi.mjt.todoist.command.type.HelpCommand;
import bg.sofia.uni.fmi.mjt.todoist.command.type.LabelCommand;
import bg.sofia.uni.fmi.mjt.todoist.command.type.TaskCommand;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

public class CommandFactory {
    private static final int START_IDX = 4;
    private static final int HELP_CMD_GROUP_IDX = 6;
    private static final int TASK_CMD_GROUP_IDX = 14;
    private static final int COLLABORATION_CMD_GROUP_IDX = 21;

    private static final EnumMap<CommandType, CommandGroup> commandGroups = new EnumMap<>(CommandType.class);
    private static final EnumMap<CommandGroup, Command> commands = new EnumMap<>(CommandGroup.class);

    private final UserWorkspace userWorkspace;

    public CommandFactory(UserWorkspace userWorkspace) {
        this.userWorkspace = userWorkspace;
        initializeCommandGroups();
        initializeCommands();
    }

    public String executeCommand(CommandType commandType, Map<CommandKeywords, String> commandArguments){
        CommandGroup group = commandGroups.get(commandType); // help, task, collaboration, label
        Command command = group.equals(CommandGroup.TASK) &&
                commandArguments.containsKey(CommandKeywords.COLLABORATION) ?
                commands.get(CommandGroup.COLLABORATION) : commands.get(group);
        return command.execute(commandType, commandArguments);
    }

    private void initializeCommands() {
        commands.put(CommandGroup.TASK, new TaskCommand(userWorkspace.getTaskStorage()));
        commands.put(CommandGroup.COLLABORATION, new CollaborationCommand(userWorkspace.getCollaborationStorage()));
        commands.put(CommandGroup.LABEL, new LabelCommand(userWorkspace.getLabelStorage(), userWorkspace.getTaskStorage()));
        commands.put(CommandGroup.HELP, new HelpCommand());
    }

    private void initializeCommandGroups() {
        CommandType[] commands = CommandType.values();
        for (int i = START_IDX; i < commands.length; i++) {
            if (i <= HELP_CMD_GROUP_IDX) {
                commandGroups.put(commands[i], CommandGroup.HELP);
            } else if (i <= TASK_CMD_GROUP_IDX) {
                commandGroups.put(commands[i], CommandGroup.TASK);
            } else if (i <= COLLABORATION_CMD_GROUP_IDX) {
                commandGroups.put(commands[i], CommandGroup.COLLABORATION);
            } else {
                commandGroups.put(commands[i], CommandGroup.LABEL);
            }
        }
    }

}
