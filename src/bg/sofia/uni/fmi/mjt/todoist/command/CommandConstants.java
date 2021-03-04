package bg.sofia.uni.fmi.mjt.todoist.command;

public class CommandConstants {
    public static final String SUCCESS_REGISTER_USER
            = "[ You were registered successfully. Welcome in Todoist, login to manage your plans! ]";
    public static final String ALREADY_TAKEN_USERNAME
            = "[ Username \"%s\" is already taken, please select another one. ]";

    public static final String SUCCESS_LOGIN_USER
            = "[ You are successfully logged in and you can manage your tasks. ]";
    public static final String INVALID_LOGIN_USER_INFO
            = "[ Invalid password/username combination. ]";

    public static final String SUCCESS_LOGOUT_USER
            = "[ You are successfully logged out. ]";
    public static final String NOT_LOGGED_IN
            = "[ You are not logged in. ]";

    public static final String DISCONNECTED
            = "[ Disconnected from server. ]";
    public static final String INCORRECT_COMMAND
            = "[ Incorrect command. ]";

    public static final String SUCCESS_ADD_TASK
            = "[ Task \"%s\" was added successfully to your workspace. ]";
    public static final String ALREADY_EXISTING_TASK
            = "[ Task \"%s\" already exists at your workspace, please select another name. ]";

    public static final String SUCCESS_UPDATE_TASK
            = "[ Task \"%s\" was updated successfully. ]";
    public static final String SUCCESS_FINISH_TASK
            = "[ Task \"%s\" was finished successfully. ]";
    public static final String SUCCESS_DELETE_TASK
            = "[ Task \"%s\" was deleted successfully from your workspace. ]";

    public static final String NOT_EXISTING_TASK
            = "[ Task \"%s\" does not exist in your workspace. ]";
    public static final String NO_TASKS
            = "[ There are no tasks in your workspace. ]";
    public static final String NO_DAILY_TASKS
            = "[ There are no tasks for today in your workspace. ]";
    public static final String NOT_EXISTING_COLLABORATION
            = "[ Collaboration \"%s\" does not exist in your workspace. ]";

    public static final String SUCCESS_ADD_LABEL
            = "[ Label \"%s\" was added successfully. ]";
    public static final String LABEL_ALREADY_EXISTS
            = "[ Label \"%s\" already exists at your workspace. ]";

    public static final String SUCCESS_DELETE_LABEL
            = "[ Label \"%s\" was deleted successfully. ]";
    public static final String NOT_EXISTING_LABEL
            = "[ Label \"%s\" does not exists at your workspace. ]";

    public static final String NO_LABELS
            = "[ There are no labels in your workspace. ]";
    public static final String NO_LABEL_TASKS
            = "[ Label \"%s\" does not exists at your workspace or you have not any tasks with it. ]";
    public static final String NO_COLLABORATIONS
            = "[ There are no collaborations in your workspace. ]";

    public static final String SUCCESS_LABEL_TASK
            = "[ Task \"%s\" was labeled successfully with label \"%s\". ]";
    public static final String CANNOT_LABEL_TASK
            = "[ Task \"%s\" cannot be labeled with label \"%s\", check correctly your data. ]";

    public static final String SUCCESS_ADD_COLLABORATION
            = "[ Collaboration \"%s\" was added successfully to your workspace. ]";
    public static final String ALREADY_CONTAINING_COLLABORATION
            = "[ Collaboration \"%s\" already exists your workspace. ]";

    public static final String SUCCESS_DELETE_COLLABORATION
            = "[ Collaboration \"%s\" was successfully deleted from your workspace. ]";
    public static final String NOT_CONTAINING_COLLABORATION
            = "[ Collaboration \"%s\" does not exists at your workspace. ]";

    public static final String SUCCESS_ADD_USER_COLLABORATION
            = "[ User \"%s\" was successfully added to collaboration \"%s\". ]";

    public static final String INVALID_LIST_USERS_DATA
            = "[ Collaboration \"%s\" does not exists at your workspace or it has not any participants. ]";

    public static final String SUCCESS_ADD_TASK_COLLABORATION
            = "[ Task \"%s\" was added successfully to collaboration \"%s\". ]";
    public static final String ALREADY_EXISTING_TASK_COLLABORATION
            = "[ Task \"%s\" already exists at \"%s\" collaboration, please select another name. ]";

    public static final String SUCCESS_UPDATE_TASK_COLLABORATION
            = "[ Task \"%s\" was updated successfully to collaboration \"%s\". ]";
    public static final String SUCCESS_FINISH_TASK_COLLABORATION
            = "[ Task \"%s\" was finished successfully at collaboration \"%s\". ]";
    public static final String SUCCESS_DELETE_TASK_COLLABORATION
            = "[ Task \"%s\" was deleted successfully at collaboration \"%s\". ]";

    public static final String NOT_EXISTING_TASK_COLLABORATION
            = "[ Task \"%s\" does not exist in collaboration \"%s\". ]";
    public static final String NO_TASKS_COLLABORATION
            = "[ There are no tasks in collaboration \"%s\". ]";
    public static final String NO_DAILY_TASKS_COLLABORATION
            = "[ There are no tasks for today in collaboration \"%s\". ]";

    public static final String SUCCESS_ASSIGNEE_TASK
            = "[ Task \"%s\" from collaboration \"%s\" was assigned to user \"%s\". ]";
    public static final String CANNOT_ASSIGNEE_TASK
            = "[ Task \"%s\" from collaboration \"%s\" cannot be assigned to user \"%s\", " +
            "check your data correctness. ]";

    private CommandConstants() {
    }
}
