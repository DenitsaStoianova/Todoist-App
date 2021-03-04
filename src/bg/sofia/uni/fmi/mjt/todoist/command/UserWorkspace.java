package bg.sofia.uni.fmi.mjt.todoist.command;

import bg.sofia.uni.fmi.mjt.todoist.storage.content.collaboration.CollaborationStorage;
import bg.sofia.uni.fmi.mjt.todoist.storage.content.label.LabelStorage;
import bg.sofia.uni.fmi.mjt.todoist.storage.content.task.taskstorage.TaskStorage;

public class UserWorkspace {
    private final TaskStorage taskStorage;
    private final LabelStorage labelStorage;
    private final CollaborationStorage collaborationStorage;

    public UserWorkspace() {
        this.taskStorage = new TaskStorage();
        this.labelStorage = new LabelStorage();
        this.collaborationStorage = new CollaborationStorage();
    }

    public TaskStorage getTaskStorage() {
        return taskStorage;
    }

    public LabelStorage getLabelStorage() {
        return labelStorage;
    }

    public CollaborationStorage getCollaborationStorage() {
        return collaborationStorage;
    }
}
