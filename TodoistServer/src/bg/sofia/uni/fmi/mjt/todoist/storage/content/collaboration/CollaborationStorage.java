package bg.sofia.uni.fmi.mjt.todoist.storage.content.collaboration;

import bg.sofia.uni.fmi.mjt.todoist.command.enums.CommandKeywords;
import bg.sofia.uni.fmi.mjt.todoist.storage.Storage;
import bg.sofia.uni.fmi.mjt.todoist.worker.file.content.CollaborationParticipatedFileExecutor;
import bg.sofia.uni.fmi.mjt.todoist.worker.file.content.CollaborationsCreatedFileExecutor;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class CollaborationStorage implements Storage {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
    private static final String CREATED_COLLABORATIONS = "[CREATED COLLABORATIONS]";
    private static final String PARTICIPANT_AT = "[PARTICIPANT AT COLLABORATIONS]";
    private static final String PARTICIPATED_CREATOR = "[CREATOR: %s]";
    private static final String ALL_PARTICIPANTS = "[ALL PARTICIPANTS: %s]";
    private static final String DELIMITER = System.lineSeparator();

    private Map<String, Collaboration> createdCollaborations;
    private Map<String, Collaboration> participatedCollaborations;

    private final CollaborationParticipatedFileExecutor collaborationParticipatedFileExecutor;
    private final CollaborationsCreatedFileExecutor collaborationsCreatedFileExecutor;
    private String collaborationCreator;

    public CollaborationStorage() {
        this.createdCollaborations = new HashMap<>();
        this.participatedCollaborations = new HashMap<>();
        this.collaborationsCreatedFileExecutor = new CollaborationsCreatedFileExecutor();
        this.collaborationParticipatedFileExecutor = new CollaborationParticipatedFileExecutor();
    }

    public Collaboration getCollaboration(String name) {
        return createdCollaborations.get(name);
    }

    public void setCollaborationCreator(String creator) {
        this.collaborationCreator = creator;
    }

    public boolean addCollaboration(String name) {
        if (!createdCollaborations.containsKey(name)) {
            createdCollaborations.put(name, new Collaboration(name, collaborationCreator));
            return true;
        }
        return false;
    }

    public boolean deleteCollaboration(String name) throws IOException {
        if (createdCollaborations.containsKey(name)) {
            Collaboration collaboration = createdCollaborations.get(name);
            collaborationsCreatedFileExecutor.removeCollaborationFile(collaboration.getCreator(), name);
            for (String participant : collaboration.getParticipants()) {
                collaborationParticipatedFileExecutor.removeCollaborationFile(participant, collaboration);
            }
            createdCollaborations.remove(name);
            return true;
        }
        return false;
    }

    public String listCollaborations() {
        StringBuilder collaborationsBuilder = new StringBuilder();
        if (!createdCollaborations.isEmpty()) {
            appendCollaborationType(collaborationsBuilder, CREATED_COLLABORATIONS);
            createdCollaborations.values().forEach(collaborationsBuilder::append);
        }
        if (!participatedCollaborations.isEmpty()) {
            appendCollaborationType(collaborationsBuilder, PARTICIPANT_AT);
            participatedCollaborations.values().forEach(c ->
                    collaborationsBuilder.append(c).append(DELIMITER)
                            .append(String.format(PARTICIPATED_CREATOR, c.getCreator())));
        }
        return collaborationsBuilder.toString();
    }

    private void appendCollaborationType(StringBuilder collaborationsBuilder, String type) {
        collaborationsBuilder
                .append(collaborationsBuilder.isEmpty() ? "" : DELIMITER)
                .append(type);
    }

    public boolean addParticipant(String collaboration, String user) {
        if (createdCollaborations.containsKey(collaboration)) {
            createdCollaborations.get(collaboration).addParticipant(user);
            return true;
        }
        return false;
    }

    public boolean assignTask(String collaborationName, Map<CommandKeywords, String> taskComponents) {
        if (!createdCollaborations.containsKey(collaborationName) || !createdCollaborations.get(collaborationName)
                .containsParticipant(taskComponents.get(CommandKeywords.USER))) {
            return false;
        }
        Collaboration collaboration = createdCollaborations.get(collaborationName);
        return taskComponents.containsKey(CommandKeywords.DATE) ?
                collaboration.assigneeOrdinaryTask(taskComponents.get(CommandKeywords.TASK),
                        LocalDate.parse(taskComponents.get(CommandKeywords.DATE), formatter),
                        taskComponents.get(CommandKeywords.USER))
                : collaboration.assigneeInboxTask(taskComponents.get(CommandKeywords.TASK),
                taskComponents.get(CommandKeywords.USER));
    }

    public String listUsers(String collaboration) {
        return createdCollaborations.containsKey(collaboration) ?
        String.format(ALL_PARTICIPANTS, createdCollaborations.get(collaboration).getParticipants()) : "";
    }

    @Override
    public void loadContent(String user) throws IOException, ClassNotFoundException {
        createdCollaborations = collaborationsCreatedFileExecutor.loadCollaborations(user);
        participatedCollaborations = collaborationParticipatedFileExecutor.loadCollaborations(user);
    }

    @Override
    public void saveContent(String user) throws IOException {
        collaborationsCreatedFileExecutor.saveCollaborations(user, createdCollaborations);
        collaborationParticipatedFileExecutor.saveCollaborations(createdCollaborations);
    }

}
