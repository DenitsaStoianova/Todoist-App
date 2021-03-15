package bg.sofia.uni.fmi.mjt.todoist.exceptions;

public class EmptyCommandException extends CommandException {
    private static final long serialVersionUID = -6412913124325481871L;

    public EmptyCommandException(String message) {
        super(message);
    }
}
