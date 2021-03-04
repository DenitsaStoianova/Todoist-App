package bg.sofia.uni.fmi.mjt.todoist.storage.content.collaboration;

import bg.sofia.uni.fmi.mjt.todoist.command.enums.CommandKeywords;
import bg.sofia.uni.fmi.mjt.todoist.storage.Storage;
import bg.sofia.uni.fmi.mjt.todoist.file.executor.content.CollaborationParticipantsFileExecutor;
import bg.sofia.uni.fmi.mjt.todoist.file.executor.content.CollaborationsFileExecutor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CollaborationStorage implements Storage {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
    private static final String CREATED_COLLABORATIONS = "\nCreated collaborations:\n";
    private static final String PARTICIPANT_AT = "\nParticipant at collaborations:\n";

    private Map<String, Collaboration> collaborations;
    private final CollaborationParticipantsFileExecutor collaborationParticipantsFileExecutor;
    private final CollaborationsFileExecutor collaborationsFileExecutor;
    private String collaborationCreator;

    public CollaborationStorage() {
        this.collaborations = new HashMap<>();
        this.collaborationsFileExecutor = new CollaborationsFileExecutor();
        this.collaborationParticipantsFileExecutor = new CollaborationParticipantsFileExecutor();
    }

    public Collaboration getCollaboration(String name) {
        return collaborations.get(name);
    }

    public void setCollaborationCreator(String creator) {
        this.collaborationCreator = creator;
    }

    public boolean addCollaboration(String name) {
        if (!collaborations.containsKey(name)) {
            Collaboration collaboration = new Collaboration(name, collaborationCreator);
            collaborations.put(name, collaboration);
            return true;
        }
        return false;
    }

    public boolean deleteCollaboration(String name) {
        if (collaborations.containsKey(name)) {
            collaborations.remove(name);
            return true;
        }
        return false;
    }

    public String listCollaborations() {
        StringBuilder collaborationsBuilder = new StringBuilder();
        if (!collaborations.isEmpty()) {
            collaborationsBuilder.append(CREATED_COLLABORATIONS);
            collaborations.values().forEach(collaborationsBuilder::append);
        }
        String collaborations = collaborationParticipantsFileExecutor
                .loadParticipatingCollaborations(collaborationCreator);
        if (!collaborations.isEmpty()) {
            collaborationsBuilder.append(PARTICIPANT_AT);
            collaborationsBuilder.append(collaborations);
        }

        return collaborationsBuilder.toString();
    }

    public boolean addParticipant(String collaboration, String user) {
        if (collaborations.containsKey(collaboration)) {
            collaborations.get(collaboration).addParticipant(user);
            return true;
        }
        return false;
    }

    public boolean assignTask(String collaborationName, Map<CommandKeywords, String> taskComponents) {
        if (!collaborations.containsKey(collaborationName) || !collaborations.get(collaborationName)
                .containsParticipant(taskComponents.get(CommandKeywords.USER))) {
            return false;
        }
        Collaboration collaboration = collaborations.get(collaborationName);
        return taskComponents.containsKey(CommandKeywords.DATE) ?
                collaboration.assigneeOrdinaryTask(taskComponents.get(CommandKeywords.TASK),
                        LocalDate.parse(taskComponents.get(CommandKeywords.DATE), formatter),
                        taskComponents.get(CommandKeywords.USER))
                : collaboration.assigneeInboxTask(taskComponents.get(CommandKeywords.TASK),
                taskComponents.get(CommandKeywords.USER));
    }

    public String listUsers(String collaboration) {
        StringBuilder usersBuilder = new StringBuilder();
        collaborations.get(collaboration).getParticipants().forEach(p -> usersBuilder.append(p).append(";"));
        return usersBuilder.toString();
    }

    private Map<String, Set<Collaboration>> addParticipants() {
        Map<String, Set<Collaboration>> participantsInfo = new HashMap<>(); // participant, collaboration
        for (Map.Entry<String, Collaboration> collaborationEntry : collaborations.entrySet()) {
            Set<String> participants = collaborationEntry.getValue().getParticipants();
            for (String participant : participants) {
                if (!participantsInfo.containsKey(participant)) {
                    participantsInfo.put(participant, new HashSet<>());
                }
                participantsInfo.get(participant).add(collaborationEntry.getValue());
            }
        }
        return participantsInfo;
    }

    @Override
    public void loadContent(String user) {
        collaborations = collaborationsFileExecutor.loadCollaborations(user);
    }

    @Override
    public void saveContent(String user) {
        collaborationsFileExecutor.saveCollaborations(user, collaborations);
        collaborationParticipantsFileExecutor.saveParticipatingCollaborations(addParticipants());
    }

}
