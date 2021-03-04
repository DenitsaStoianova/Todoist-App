package bg.sofia.uni.fmi.mjt.todoist.storage.content.task;

public class CollaborationOrdinaryTask extends OrdinaryTask {
    private static final String ASSIGNEE = "Assignee: %s" + System.lineSeparator();

    private String assignee;

    public CollaborationOrdinaryTask(String name) {
        super(name);
    }

    public void assigneeTask(String user) {
        this.assignee = user;
    }

    @Override
    public String toString() {
        return assignee == null ? super.toString() : super.toString() + String.format(ASSIGNEE, assignee);
    }
}
