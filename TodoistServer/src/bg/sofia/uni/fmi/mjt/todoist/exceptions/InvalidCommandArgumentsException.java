package bg.sofia.uni.fmi.mjt.todoist.exceptions;

public class InvalidCommandArgumentsException extends CommandException {
    private static final long serialVersionUID = 5227379656155646306L;

    public InvalidCommandArgumentsException(String message) {
        super(message);
    }
}
