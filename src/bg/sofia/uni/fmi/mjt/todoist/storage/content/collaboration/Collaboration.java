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

    private static final String COLLABORATION_INFO = "COLLABORATION:%sName: %s%s";
    private static final String INBOX_TASKS_INFO = "--INBOX TASKS--" + System.lineSeparator();
    private static final String ORDINARY_TASKS_INFO = "--ORDINARY TASKS--" + System.lineSeparator();
    private static final String ORDINARY_TASKS_DATE_INFO = "TASKS FROM DATE: %s" + System.lineSeparator();
    private static final String PARTICIPANTS_INFO = "--PARTICIPANTS--" + System.lineSeparator();

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
        collaborationBuilder.append(String.format(COLLABORATION_INFO, System.lineSeparator(),
                name, System.lineSeparator()));
        if (!inboxTasks.isEmpty()) {
            collaborationBuilder.append(INBOX_TASKS_INFO);
            inboxTasks.values().forEach(collaborationBuilder::append);
        }
        if (!ordinaryTasks.isEmpty()) {
            collaborationBuilder.append(ORDINARY_TASKS_INFO);
            for (Map.Entry<LocalDate, Map<String, CollaborationOrdinaryTask>> ordinaryTask : ordinaryTasks.entrySet()) {
                collaborationBuilder.append(String.format(ORDINARY_TASKS_DATE_INFO, ordinaryTask.getKey()));
                ordinaryTask.getValue().values().forEach(collaborationBuilder::append);
            }
        }
        if (!participants.isEmpty()) {
            collaborationBuilder.append(PARTICIPANTS_INFO);
            collaborationBuilder.append(participants.stream().reduce((s1, s2) -> s1 + ", " + s2).orElse(""));
        }
        return collaborationBuilder.toString();
    }
}
