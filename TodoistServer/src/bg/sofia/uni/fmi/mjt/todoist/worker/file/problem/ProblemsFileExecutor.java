package bg.sofia.uni.fmi.mjt.todoist.worker.file.problem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

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
        PrintStream ps = null;
        try {
            ps = new PrintStream(new File("/sample.log"));
            throw new FileNotFoundException("Sample Exception");
        } catch (FileNotFoundException e) {
            e.printStackTrace(ps);
        }
    }


}
