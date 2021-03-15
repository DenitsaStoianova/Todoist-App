package bg.sofia.uni.fmi.mjt.todoist.command.type;

import bg.sofia.uni.fmi.mjt.todoist.command.CommandBuilder;
import bg.sofia.uni.fmi.mjt.todoist.command.CommandFactory;
import bg.sofia.uni.fmi.mjt.todoist.command.enums.CommandKeywords;
import bg.sofia.uni.fmi.mjt.todoist.command.enums.CommandType;
import bg.sofia.uni.fmi.mjt.todoist.constants.CommandConstants;
import bg.sofia.uni.fmi.mjt.todoist.exceptions.InvalidCommandArgumentsException;
import bg.sofia.uni.fmi.mjt.todoist.exceptions.EmptyCommandException;
import bg.sofia.uni.fmi.mjt.todoist.exceptions.InvalidCommandNameException;
import bg.sofia.uni.fmi.mjt.todoist.storage.user.UserStorage;
import bg.sofia.uni.fmi.mjt.todoist.worker.storage.StorageWorker;
import bg.sofia.uni.fmi.mjt.todoist.worker.storage.UserWorkspace;

import java.io.IOException;
import java.util.Map;

import static bg.sofia.uni.fmi.mjt.todoist.constants.CommandConstants.DISCONNECTED;

public class StartupCommand {
    private final UserStorage userStorage;
    private final UserWorkspace userWorkspace;
    private final StorageWorker storageWorker;
    private final CommandFactory commandFactory;

    public StartupCommand(UserStorage userStorage) {
        this.userStorage = userStorage;
        this.userWorkspace = new UserWorkspace();
        this.storageWorker = new StorageWorker(userWorkspace);
        this.commandFactory = new CommandFactory(userWorkspace);
    }

    public String execute(String commandText) throws EmptyCommandException, InvalidCommandArgumentsException,
            IOException, ClassNotFoundException, InvalidCommandNameException {
        CommandType commandType = CommandBuilder.getCommandType(commandText);
        Map<CommandKeywords, String> commandArguments = CommandBuilder.getCommandArguments(commandText);
        return switch (commandType) {
            case REGISTER -> register(commandArguments.get(CommandKeywords.USERNAME),
                    commandArguments.get(CommandKeywords.PASSWORD));
            case LOGIN -> login(commandArguments.get(CommandKeywords.USERNAME),
                    commandArguments.get(CommandKeywords.PASSWORD));
            case LOGOUT -> logout();
            case DISCONNECT -> disconnectUser();
            default -> userStorage.isUserLogged() ? commandFactory.executeCommand(commandType, commandArguments) :
                    CommandConstants.NOT_LOGGED_IN;
        };
    }

    public void saveDatabase() {
        try {
            userStorage.saveUsers();
        } catch (IOException e) {
            e.printStackTrace(); // TODO
        }
    }

    private String register(String username, String password) {
        return userStorage.registerUser(username, password) ?
                CommandConstants.SUCCESS_REGISTER_USER
                : String.format(CommandConstants.ALREADY_TAKEN_USERNAME, username);
    }

    private String login(String username, String password) throws IOException, ClassNotFoundException {
        if (userStorage.loginUser(username, password)) {
            storageWorker.loadStorages(username);
            userWorkspace.getCollaborationStorage().setCollaborationCreator(username);
            return CommandConstants.SUCCESS_LOGIN_USER;
        }
        return CommandConstants.INVALID_LOGIN_USER_INFO;
    }

    private String logout() throws IOException {
        if (userStorage.isUserLogged()) {
            String username = userStorage.logoutCurrentUser();
            storageWorker.saveStorages(username);
            return CommandConstants.SUCCESS_LOGOUT_USER;
        }
        return CommandConstants.NOT_LOGGED_IN;
    }

    private String disconnectUser() {
        userStorage.logoutCurrentUser();
        return DISCONNECTED;
    }
}
