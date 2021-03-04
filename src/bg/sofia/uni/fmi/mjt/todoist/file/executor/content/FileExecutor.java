package bg.sofia.uni.fmi.mjt.todoist.file.executor.content;

import java.io.File;
import java.io.IOException;

public abstract class FileExecutor {
    private static final String USER_WORKSPACE_DIR = "database/Todoist-Workspaces-Database/%s-Todoist-Workspace";
    protected static final String READING_PROBLEM_MSG = "A problem occurred while reading from file";
    protected static final String WRITING_PROBLEM_MSG = "A problem occurred while writing to file";

    protected String getUserWorkspaceDir() {
        return USER_WORKSPACE_DIR;
    }

    protected File createFile(String user) {
        File usersFile = new File(getUserContentPath(user));
        usersFile.getParentFile().mkdirs();
        try {
            usersFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return usersFile;
    }

    protected abstract String getUserContentPath(String user);
}
