package bg.sofia.uni.fmi.mjt.todoist.command.type;

import bg.sofia.uni.fmi.mjt.todoist.command.CommandConstants;
import bg.sofia.uni.fmi.mjt.todoist.command.enums.CommandKeywords;
import bg.sofia.uni.fmi.mjt.todoist.command.enums.CommandType;
import bg.sofia.uni.fmi.mjt.todoist.storage.content.collaboration.Collaboration;
import bg.sofia.uni.fmi.mjt.todoist.storage.content.collaboration.CollaborationStorage;

import java.util.Map;

public class CollaborationCommand implements Command {
    private final CollaborationStorage collaborationStorage;

    public CollaborationCommand(CollaborationStorage collaborationStorage) {
        this.collaborationStorage = collaborationStorage;
    }

    @Override
    public String execute(CommandType commandType, Map<CommandKeywords, String> commandArguments) {
        return switch (commandType) {
            case ADD_COLLABORATION -> addCollaborationStorage(commandArguments.get(CommandKeywords.NAME));
            case DELETE_COLLABORATION -> deleteCollaborationStorage(commandArguments.get(CommandKeywords.NAME));
            case LIST_COLLABORATIONS -> listCollaborationStorage();
            case ADD_USER -> addParticipantStorage(commandArguments.get(CommandKeywords.COLLABORATION),
                    commandArguments.get(CommandKeywords.USER));
            case LIST_USERS -> listUsersStorage(commandArguments.get(CommandKeywords.COLLABORATION));
            case ASSIGN_TASK -> assignTask(commandArguments.get(CommandKeywords.COLLABORATION), commandArguments);
            default -> executeCollaborationTaskCommands(commandArguments.get(CommandKeywords.COLLABORATION),
                    commandType, commandArguments);
        };
    }

    private String addCollaborationStorage(String name) {
        return collaborationStorage.addCollaboration(name) ?
                String.format(CommandConstants.SUCCESS_ADD_COLLABORATION, name)
                : String.format(CommandConstants.ALREADY_CONTAINING_COLLABORATION, name);
    }

    private String deleteCollaborationStorage(String name) {
        return collaborationStorage.deleteCollaboration(name) ?
                String.format(CommandConstants.SUCCESS_DELETE_COLLABORATION, name)
                : String.format(CommandConstants.NOT_CONTAINING_COLLABORATION, name);
    }

    private String listCollaborationStorage() {
        String collaborationsInfo = collaborationStorage.listCollaborations();
        return collaborationsInfo.isEmpty() ? CommandConstants.NO_COLLABORATIONS : collaborationsInfo;
    }

    private String addParticipantStorage(String collaboration, String user) {
        return collaborationStorage.addParticipant(collaboration, user) ?
                String.format(CommandConstants.SUCCESS_ADD_USER_COLLABORATION, user, collaboration)
                : String.format(CommandConstants.NOT_CONTAINING_COLLABORATION, collaboration);
    }

    private String listUsersStorage(String collaboration) {
        String usersInfo = collaborationStorage.listUsers(collaboration);
        return usersInfo.isEmpty() ?
                String.format(CommandConstants.INVALID_LIST_USERS_DATA, collaboration) : usersInfo;
    }

    private String assignTask(String collaboration, Map<CommandKeywords, String> commandArguments) {
        return collaborationStorage.assignTask(collaboration, commandArguments) ?
                String.format(CommandConstants.SUCCESS_ASSIGNEE_TASK, commandArguments.get(CommandKeywords.TASK),
                        collaboration, commandArguments.get(CommandKeywords.USER)) :
                String.format(CommandConstants.CANNOT_ASSIGNEE_TASK, commandArguments.get(CommandKeywords.TASK),
                        collaboration, commandArguments.get(CommandKeywords.USER));
    }

    public String executeCollaborationTaskCommands(String collaborationName, CommandType commandType,
                                                   Map<CommandKeywords, String> commandArguments) {
        Collaboration collaboration = collaborationStorage.getCollaboration(collaborationName);
        if (collaboration != null) {
            CollaborationTaskCommand collaborationTaskCommand =
                    new CollaborationTaskCommand(collaboration, collaborationName);
            return collaborationTaskCommand.executeTaskCommands(commandType, commandArguments);
        }
        return String.format(CommandConstants.NOT_EXISTING_COLLABORATION, collaborationName);
    }

}
