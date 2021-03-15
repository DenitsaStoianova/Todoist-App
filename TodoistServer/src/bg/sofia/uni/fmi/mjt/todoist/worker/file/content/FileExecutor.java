package bg.sofia.uni.fmi.mjt.todoist.worker.file.content;

import java.io.File;
import java.io.IOException;

public abstract class FileExecutor {
    private static final String USER_WORKSPACE_DIR = "database/Todoist-Workspaces-Database/%s-Todoist-Workspace";

    protected String getUserWorkspaceDir() {
        return USER_WORKSPACE_DIR;
    }

    protected File createFile(String userContentPath) throws IOException {
        File usersFile = new File(userContentPath);
        usersFile.getParentFile().mkdirs();
        usersFile.createNewFile();
        return usersFile;
    }

    protected abstract String getUserContentPath(String user);
}
