package bg.sofia.uni.fmi.mjt.todoist.command.enums;

import bg.sofia.uni.fmi.mjt.todoist.exceptions.InvalidCommandNameException;

import static bg.sofia.uni.fmi.mjt.todoist.constants.ExceptionMessageConstants.INVALID_COMMAND_TOKEN_MSG;

public class EnumEvaluator {
    public static <E extends Enum<E>> E getValueOf(Class<E> enumClass, String value) throws InvalidCommandNameException {
        try {
            return Enum.valueOf(enumClass, value.replace("-", "_").toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidCommandNameException(String.format(INVALID_COMMAND_TOKEN_MSG, value));
        }
    }

}
