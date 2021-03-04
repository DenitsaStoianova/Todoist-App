package bg.sofia.uni.fmi.mjt.todoist.file.executor.problem;

import java.io.File;
import java.io.IOException;

public class ProblemsFileExecutor{
    private static final String PROBLEMS_FILE_DIR = "database/Todoist-Problems-Database/problems.txt";

    protected File createFile(String user) {
        File usersFile = new File(PROBLEMS_FILE_DIR);
        usersFile.getParentFile().mkdirs();
        try {
            usersFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return usersFile;
    }

    public void saveSystemProblem(){

    }


}
