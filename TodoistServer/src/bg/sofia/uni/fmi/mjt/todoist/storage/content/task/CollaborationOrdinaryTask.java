package bg.sofia.uni.fmi.mjt.todoist.storage.content.task;

public class CollaborationOrdinaryTask extends OrdinaryTask {
    private static final String ASSIGNEE = "[Assignee: %s]";
    private static final String DELIMITER = ", ";

    private String assignee;

    public CollaborationOrdinaryTask(String name) {
        super(name);
    }

    public void assigneeTask(String user) {
        this.assignee = user;
    }

    @Override
    public String toString() {
        return assignee == null ? super.toString() : super.toString() + DELIMITER + String.format(ASSIGNEE, assignee);
    }
}
