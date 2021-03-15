package bg.sofia.uni.fmi.mjt.todoist.storage.content.collaboration;

import bg.sofia.uni.fmi.mjt.todoist.storage.content.task.CollaborationInboxTask;
import bg.sofia.uni.fmi.mjt.todoist.storage.content.task.CollaborationOrdinaryTask;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Collaboration implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final String COLLABORATION_INFO = "=> COLLABORATION: [Name: %s]";
    private static final String INBOX_TASKS_INFO = "INBOX TASKS:";
    private static final String INBOX_TASK_MSG = "INBOX TASK: ";
    private static final String ORDINARY_TASKS_INFO = "ORDINARY TASKS:";
    private static final String ORDINARY_TASK_MSG = "ORDINARY TASK: ";
    private static final String ORDINARY_TASKS_DATE_INFO = "TASKS FROM DATE: %s";
    private static final String PARTICIPANTS_INFO = "PARTICIPANTS: ";
    private static final String DELIMITER = System.lineSeparator();

    private final String name;
    private final String creator;
    private final Map<String, CollaborationInboxTask> inboxTasks;
    private final Map<LocalDate, Map<String, CollaborationOrdinaryTask>> ordinaryTasks;
    private final Set<String> participants;

    public Collaboration(String name, String creator) {
        this.name = name;
        this.creator = creator;
        this.participants = new TreeSet<>();
        this.inboxTasks = new HashMap<>();
        this.ordinaryTasks = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public String getCreator() {
        return creator;
    }

    public void addParticipant(String user) {
        participants.add(user);
    }

    public Set<String> getParticipants() {
        return participants;
    }

    public boolean containsParticipant(String user) {
        return participants.contains(user);
    }

    public Map<String, CollaborationInboxTask> getInboxTasks() {
        return inboxTasks;
    }

    public Map<LocalDate, Map<String, CollaborationOrdinaryTask>> getOrdinaryTasks() {
        return ordinaryTasks;
    }

    public boolean assigneeInboxTask(String task, String user) {
        if (inboxTasks.containsKey(task)) {
            inboxTasks.get(task).assigneeTask(user);
            return true;
        }
        return false;
    }

    public boolean assigneeOrdinaryTask(String task, LocalDate date, String user) {
        if (ordinaryTasks.containsKey(date) && ordinaryTasks.get(date).containsKey(task)) {
            ordinaryTasks.get(date).get(task).assigneeTask(user);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder collaborationBuilder = new StringBuilder();
        collaborationBuilder.append(DELIMITER).append(String.format(COLLABORATION_INFO, name));
        if (!inboxTasks.isEmpty()) {
            collaborationBuilder.append(DELIMITER).append(INBOX_TASKS_INFO);
            inboxTasks.values().forEach(t -> collaborationBuilder
                    .append(DELIMITER)
                    .append(t.toString().replace(INBOX_TASK_MSG, "")));
        }
        if (!ordinaryTasks.isEmpty()) {
            collaborationBuilder.append(DELIMITER).append(ORDINARY_TASKS_INFO);
            ordinaryTasks.forEach((key, value) -> {
                collaborationBuilder.append(String.format(ORDINARY_TASKS_DATE_INFO, key));
                value.values().forEach(t -> collaborationBuilder
                        .append(t.toString().replace(ORDINARY_TASK_MSG, "")));
            });
        }
        if (!participants.isEmpty()) {
            collaborationBuilder.append(DELIMITER).append(PARTICIPANTS_INFO).append(participants);
        }
        return collaborationBuilder.toString();
    }
}
