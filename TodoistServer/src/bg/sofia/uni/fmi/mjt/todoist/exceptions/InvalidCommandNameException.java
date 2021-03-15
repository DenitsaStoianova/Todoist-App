package bg.sofia.uni.fmi.mjt.todoist.exceptions;

public class InvalidCommandNameException extends CommandException {
    private static final long serialVersionUID = -5818202343296900781L;

    public InvalidCommandNameException(String message) {
        super(message);
    }
}
