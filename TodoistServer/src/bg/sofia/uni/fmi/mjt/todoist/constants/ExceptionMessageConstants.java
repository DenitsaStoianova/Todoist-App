package bg.sofia.uni.fmi.mjt.todoist.constants;

public class ExceptionMessageConstants {
    public static final String NULL_COMMAND = "Command cannot be null.";
    public static final String INVALID_COMMAND_ARGS
            = "Invalid command arguments. Dou you know correct command syntax? "
            + "Press \"help-task\", \"help-collaboration\" or \"help-label\" to look at it.";
    public static final String INVALID_COMMAND_TOKEN_MSG
            = "Command token \"%s\" is invalid, please, select a valid one.";

    private ExceptionMessageConstants() {
    }
}
