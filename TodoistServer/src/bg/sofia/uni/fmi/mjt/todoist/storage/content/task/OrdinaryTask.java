package bg.sofia.uni.fmi.mjt.todoist.storage.content.task;

import java.time.LocalDate;
import java.util.Objects;

public class OrdinaryTask extends Task {
    private static final String ORDINARY_TASK = "ORDINARY TASK: ";
    private static final String DATE = "[Date: %s]";
    private static final String DELIMITER = ", ";

    private LocalDate date;

    public OrdinaryTask(String name) {
        super(name);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        OrdinaryTask that = (OrdinaryTask) o;
        return Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), date);
    }

    @Override
    public String toString() {
        return ORDINARY_TASK + String.format(DATE, date) + DELIMITER + super.toString();
    }
}
