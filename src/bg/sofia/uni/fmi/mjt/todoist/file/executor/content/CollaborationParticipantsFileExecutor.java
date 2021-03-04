package bg.sofia.uni.fmi.mjt.todoist.file.executor.content;

import bg.sofia.uni.fmi.mjt.todoist.storage.content.collaboration.Collaboration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class CollaborationParticipantsFileExecutor extends FileExecutor {
    private static final String COLLABORATIONS_FILE = "/participating-collaborations.txt";

    public String loadParticipatingCollaborations(String user) {
        StringBuilder collaborationsBuilder = new StringBuilder();
        File collaborationsFile = new File(getUserContentPath(user));
        if (collaborationsFile.exists()) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(collaborationsFile))) {
                String line;
                while ((line = bufferedReader.readLine()) != null){
                    collaborationsBuilder.append(line).append(System.lineSeparator());
                }
            } catch (IOException e) {
                throw new IllegalStateException(READING_PROBLEM_MSG, e);
            }
        }
        return collaborationsBuilder.toString();
    }

    public void saveParticipatingCollaborations(Map<String, Set<Collaboration>> collaborations) {
        for (Map.Entry<String, Set<Collaboration>> participantsInfo : collaborations.entrySet()) {
            try (BufferedWriter bufferedWriter =
                         new BufferedWriter(new FileWriter(createFile(participantsInfo.getKey()), true))) {
                for(Collaboration collaboration : participantsInfo.getValue()){
                    bufferedWriter.write(collaboration.toString() + System.lineSeparator());
                }
            } catch (IOException e) {
                throw new IllegalStateException(WRITING_PROBLEM_MSG, e);
            }
        }
    }

    @Override
    protected String getUserContentPath(String user) {
        return String.format(getUserWorkspaceDir(), user) + COLLABORATIONS_FILE;
    }
}
