package bg.sofia.uni.fmi.mjt.todoist.storage.content.task;

public class InboxTask extends Task {
    private static final String INBOX_TASK = "INBOX TASK: ";

    public InboxTask(String name) {
        super(name);
    }

    @Override
    public String toString() {
        return INBOX_TASK + super.toString();
    }
}
