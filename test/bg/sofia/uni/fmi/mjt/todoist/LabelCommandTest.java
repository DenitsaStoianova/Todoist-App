package bg.sofia.uni.fmi.mjt.todoist;

import bg.sofia.uni.fmi.mjt.todoist.command.enums.CommandKeywords;
import bg.sofia.uni.fmi.mjt.todoist.command.enums.CommandType;
import bg.sofia.uni.fmi.mjt.todoist.command.type.Command;
import bg.sofia.uni.fmi.mjt.todoist.command.type.LabelCommand;
import bg.sofia.uni.fmi.mjt.todoist.storage.content.label.LabelStorage;
import bg.sofia.uni.fmi.mjt.todoist.storage.content.task.InboxTask;
import bg.sofia.uni.fmi.mjt.todoist.storage.content.task.taskstorage.TaskStorage;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LabelCommandTest {
    private TaskStorage taskStorage;
    private LabelStorage labelStorage;
    private Command labelCommand;

    @Before
    public void setUp() {
        this.taskStorage = mock(TaskStorage.class);
        this.labelStorage = mock(LabelStorage.class);
        this.labelCommand = new LabelCommand(labelStorage, taskStorage);
    }

    @Test
    public void testAddLabelSuccess() {
        when(labelStorage.addLabel("project")).thenReturn(true);
        final String expected = "[ Label \"project\" was added successfully. ]";
        String actual = labelCommand.execute(CommandType.ADD_LABEL,
                Map.ofEntries(Map.entry(CommandKeywords.NAME, "project")));
        assertEquals("Add label command message is not correct.", expected, actual);
    }

    @Test
    public void testAddLabelAlreadyExisting() {
        when(labelStorage.addLabel("project")).thenReturn(false);
        final String expected = "[ Label \"project\" already exists at your workspace. ]";
        String actual = labelCommand.execute(CommandType.ADD_LABEL,
                Map.ofEntries(Map.entry(CommandKeywords.NAME, "project")));
        assertEquals("Add label command message is not correct.", expected, actual);
    }

    @Test
    public void testDeleteLabelSuccess() {
        when(labelStorage.deleteLabel("project")).thenReturn(true);
        final String expected = "[ Label \"project\" was deleted successfully. ]";
        String actual = labelCommand.execute(CommandType.DELETE_LABEL,
                Map.ofEntries(Map.entry(CommandKeywords.NAME, "project")));
        assertEquals("Delete label command message is not correct.", expected, actual);
    }

    @Test
    public void testDeleteLabelNotExisting() {
        when(labelStorage.deleteLabel("project")).thenReturn(false);
        final String expected = "[ Label \"project\" does not exists at your workspace. ]";
        String actual = labelCommand.execute(CommandType.DELETE_LABEL,
                Map.ofEntries(Map.entry(CommandKeywords.NAME, "project")));
        assertEquals("Delete label command message is not correct.", expected, actual);
    }

    @Test
    public void testListLabelsSuccess() {
        final String expected = "project, homework";
        when(labelStorage.listLabels()).thenReturn(expected);
        String actual = labelCommand.execute(CommandType.LIST_LABELS, new HashMap<>());
        assertEquals("List labels command message is not correct.", expected, actual);
    }

    @Test
    public void testListLabelsNoLabels() {
        when(labelStorage.listLabels()).thenReturn("");
        final String expected = "[ There are no labels in your workspace. ]";
        String actual = labelCommand.execute(CommandType.LIST_LABELS, new HashMap<>());
        assertEquals("List labels command message is not correct.", expected, actual);
    }

    @Test
    public void testLabelTaskNotExistingInboxTask() {
        when(taskStorage.getTaskData()).thenReturn(null);
        final String expected = "[ Task \"task\" does not exist in your workspace. ]";
        String actual = labelCommand.execute(CommandType.LABEL_TASK,
                Map.ofEntries(Map.entry(CommandKeywords.NAME, "task")));
        assertEquals("Label task command message is not correct.", expected, actual);
    }

    @Test
    public void testLabelTaskNotExistingOrdinaryTask() {
        when(taskStorage.getTaskData()).thenReturn(null);
        final String expected = "[ Task \"task\" does not exist in your workspace. ]";
        String actual = labelCommand.execute(CommandType.LABEL_TASK,
                Map.ofEntries(Map.entry(CommandKeywords.NAME, "task"),
                        Map.entry(CommandKeywords.DATE, "22/12/2021")));
        assertEquals("Label task command message is not correct.", expected, actual);
    }

    @Test
    public void testLabelInboxTaskSuccess() {
        when(taskStorage.getTaskData()).thenReturn(new InboxTask("task"));
        when(labelStorage.labelTask(new InboxTask("task"), "project")).thenReturn(true);
        final String expected = "[ Task \"task\" was labeled successfully with label \"project\". ]";
        String actual = labelCommand.execute(CommandType.LABEL_TASK,
                Map.ofEntries(Map.entry(CommandKeywords.NAME, "task"), Map.entry(CommandKeywords.LABEL, "project")));
        assertEquals("Label task command message is not correct.", expected, actual);
    }

    @Test
    public void testLabelInboxTaskFailure() {
        when(taskStorage.getTaskData()).thenReturn(new InboxTask("task"));
        when(labelStorage.labelTask(new InboxTask("task"), "project")).thenReturn(false);
        final String expected = "[ Task \"task\" cannot be labeled with label \"project\", check correctly your data. ]";
        String actual = labelCommand.execute(CommandType.LABEL_TASK,
                Map.ofEntries(Map.entry(CommandKeywords.NAME, "task"), Map.entry(CommandKeywords.LABEL, "project")));
        assertEquals("Label task command message is not correct.", expected, actual);
    }

    @Test
    public void testListTasksSuccess() {
        final String expected = String.format("Name: task1%sName: task2", System.lineSeparator());
        when(labelStorage.listTasks("project")).thenReturn(expected);
        String actual = labelCommand.execute(CommandType.LIST_TASKS,
                Map.ofEntries(Map.entry(CommandKeywords.LABEL, "project")));
        assertEquals("List tasks command message is not correct.", expected, actual);
    }

    @Test
    public void testListTasksNoTasks() {
        when(labelStorage.listTasks("project")).thenReturn("");
        final String expected = "[ Label \"project\" does not exists at your workspace or you have not any tasks with it. ]";
        String actual = labelCommand.execute(CommandType.LIST_TASKS,
                Map.ofEntries(Map.entry(CommandKeywords.LABEL, "project")));
        assertEquals("List tasks command message is not correct.", expected, actual);
    }

}
