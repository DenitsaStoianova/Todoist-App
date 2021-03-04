package bg.sofia.uni.fmi.mjt.todoist.command.enums;

public class EnumEvaluator {
    public static final String INVALID_COMMAND_TOKEN_MSG
            = "Command token \"%s\" is invalid, please, select a valid one.";

    public static <E extends Enum<E>> E getValueOf(Class<E> enumClass, String value) {
        try {
            return Enum.valueOf(enumClass, value.replace("-", "_").toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(String.format(INVALID_COMMAND_TOKEN_MSG, value));
        }
    }
}
