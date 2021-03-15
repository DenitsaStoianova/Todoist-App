package bg.sofia.uni.fmi.mjt.todoist.storage.content;

import bg.sofia.uni.fmi.mjt.todoist.command.enums.CommandKeywords;
import bg.sofia.uni.fmi.mjt.todoist.storage.content.task.TaskStorage;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TaskStorageTest {
    private TaskStorage taskStorage;

    @Before
    public void setUp() {
        taskStorage = new TaskStorage();
    }

    @Test
    public void testAddTaskInboxSuccess() {
        assertTrue("Expected true when inbox task is added.",
                taskStorage.addTask(Map.ofEntries(Map.entry(CommandKeywords.NAME, "task1"),
                        Map.entry(CommandKeywords.DESCRIPTION, "some task"))));
    }

    @Test
    public void testAddTaskOrdinarySuccess() {
        assertTrue("Expected true when inbox task is added.",
                taskStorage.addTask(Map.ofEntries(Map.entry(CommandKeywords.NAME, "task1"),
                        Map.entry(CommandKeywords.DATE, "05/05/2021"), Map.entry(CommandKeywords.DESCRIPTION, "some task"))));
    }

    @Test
    public void testAddTaskInboxTaskAlreadyExists() {
        addInboxTask();
        assertFalse("Expected false when inbox task is added second time.",
                taskStorage.addTask(Map.ofEntries(Map.entry(CommandKeywords.NAME, "task"),
                        Map.entry(CommandKeywords.DESCRIPTION, "some task"))));
    }

    @Test
    public void testAddTaskOrdinaryTaskAlreadyExists() {
        addOrdinaryTask();
        assertFalse("Expected false when inbox task is added second time.",
                taskStorage.addTask(Map.ofEntries(Map.entry(CommandKeywords.NAME, "task"),
                        Map.entry(CommandKeywords.DATE, "05/05/2021"),
                        Map.entry(CommandKeywords.DESCRIPTION, "some task"))));
    }

    @Test
    public void testUpdateTaskInboxTaskSuccess() {
        addInboxTask();
        taskStorage.setTaskComponents(Map.ofEntries(Map.entry(CommandKeywords.NAME, "task"),
                Map.entry(CommandKeywords.DESCRIPTION, "some task new description")));
        assertTrue("Expected true when inbox task is updated.", taskStorage.updateTask());
    }

    @Test
    public void testUpdateTaskOrdinaryTaskSuccess() {
        addOrdinaryTask();
        taskStorage.setTaskComponents(Map.ofEntries(Map.entry(CommandKeywords.NAME, "task"),
                Map.entry(CommandKeywords.DATE, "05/05/2021"), Map.entry(CommandKeywords.DESCRIPTION, "some task")));
        assertTrue("Expected true when inbox task is updated.", taskStorage.updateTask());
    }

    @Test
    public void testUpdateTaskInboxTaskNotExisting() {
        taskStorage.setTaskComponents(Map.ofEntries(Map.entry(CommandKeywords.NAME, "task1"),
                Map.entry(CommandKeywords.DESCRIPTION, "some task new description")));
        assertFalse("Expected true when inbox task is not existing.", taskStorage.updateTask());
    }

    @Test
    public void testUpdateTaskOrdinaryTaskNotExisting() {
        taskStorage.setTaskComponents(Map.ofEntries(Map.entry(CommandKeywords.NAME, "task1"),
                Map.entry(CommandKeywords.DATE, "05/05/2021"), Map.entry(CommandKeywords.DESCRIPTION, "some task")));
        assertFalse("Expected false when inbox task is not existing.", taskStorage.updateTask());
    }

    @Test
    public void testGetTaskInboxTaskSuccess() {
        addInboxTask();
        taskStorage.setTaskComponents(Map.ofEntries(Map.entry(CommandKeywords.NAME, "task"),
                Map.entry(CommandKeywords.DESCRIPTION, "some task new description")));
        String expected = "INBOX TASK: [Name: task], [Description: some task], [Is finished: false]";
        String actual = taskStorage.getTask();
        assertEquals("Expected right task information.", expected, actual);
    }

    @Test
    public void testGetTaskOrdinaryTaskSuccess() {
        addOrdinaryTask();
        taskStorage.setTaskComponents(Map.ofEntries(Map.entry(CommandKeywords.NAME, "task"),
                Map.entry(CommandKeywords.DATE, "05/05/2021"), Map.entry(CommandKeywords.DESCRIPTION, "some task")));
        String expected = "ORDINARY TASK: [Date: 2021-05-05], [Name: task], [Description: some task], [Is finished: false]";
        String actual = taskStorage.getTask();
        assertEquals("Expected right task information.", expected, actual);
    }

    @Test
    public void testGetTaskInboxTaskNotExisting() {
        taskStorage.setTaskComponents(Map.ofEntries(Map.entry(CommandKeywords.NAME, "task1"),
                Map.entry(CommandKeywords.DESCRIPTION, "some task new description")));
        String actual = taskStorage.getTask();
        assertNull("Expected right task information.", actual);
    }

    @Test
    public void testGetTaskOrdinaryTaskNotExisting() {
        taskStorage.setTaskComponents(Map.ofEntries(Map.entry(CommandKeywords.NAME, "task1"),
                Map.entry(CommandKeywords.DATE, "05/05/2021"), Map.entry(CommandKeywords.DESCRIPTION, "some task")));
        String actual = taskStorage.getTask();
        assertNull("Expected right task information.", actual);
    }

    @Test
    public void testDeleteTaskInboxTaskSuccess() {
        addInboxTask();
        taskStorage.setTaskComponents(Map.ofEntries(Map.entry(CommandKeywords.NAME, "task"),
                Map.entry(CommandKeywords.DESCRIPTION, "some task new description")));
        assertTrue("Expected true when inbox task is deleted.", taskStorage.deleteTask());
    }

    @Test
    public void testDeleteTaskOrdinaryTaskSuccess() {
        addOrdinaryTask();
        taskStorage.setTaskComponents(Map.ofEntries(Map.entry(CommandKeywords.NAME, "task"),
                Map.entry(CommandKeywords.DATE, "05/05/2021"), Map.entry(CommandKeywords.DESCRIPTION, "some task")));
        assertTrue("Expected true when inbox task is updated.", taskStorage.deleteTask());
    }

    @Test
    public void testDeleteTaskInboxTaskNotExisting() {
        taskStorage.setTaskComponents(Map.ofEntries(Map.entry(CommandKeywords.NAME, "task"),
                Map.entry(CommandKeywords.DESCRIPTION, "some task new description")));
        assertFalse("Expected false when inbox task is not existing.", taskStorage.deleteTask());
    }

    @Test
    public void testDeleteTaskOrdinaryTaskNotExisting() {
        taskStorage.setTaskComponents(Map.ofEntries(Map.entry(CommandKeywords.NAME, "task"),
                Map.entry(CommandKeywords.DATE, "05/05/2021"), Map.entry(CommandKeywords.DESCRIPTION, "some task")));
        assertFalse("Expected false when ordinary task is not existing.", taskStorage.deleteTask());
    }

    @Test
    public void testListTasksInboxTasksSuccess() {
        addInboxTask();
        taskStorage.setTaskComponents(Map.ofEntries(Map.entry(CommandKeywords.TYPE, "inbox")));
        String expected = "INBOX TASK: [Name: task], [Description: some task], [Is finished: false]";
        String actual = taskStorage.listTasks();
        assertEquals("Expected right task information.", expected, actual);
    }

    @Test
    public void testListTasksOrdinaryTasksSuccess() {
        addOrdinaryTask();
        taskStorage.setTaskComponents(Map.ofEntries(Map.entry(CommandKeywords.TYPE, "ordinary")));
        String expected = "ORDINARY TASK: [Date: 2021-05-05], [Name: task], [Description: some task], [Is finished: false]";
        String actual = taskStorage.listTasks();
        assertEquals("Expected right task information.", expected, actual);
    }

    @Test
    public void testListTasksInboxTaskNotExisting() {
        taskStorage.setTaskComponents(Map.ofEntries(Map.entry(CommandKeywords.NAME, "task1"),
                Map.entry(CommandKeywords.DESCRIPTION, "some task new description")));
        String actual = taskStorage.listTasks();
        assertEquals("Expected right task information.", "", actual);
    }

    @Test
    public void testListTasksOrdinaryTaskNotExisting() {
        taskStorage.setTaskComponents(Map.ofEntries(Map.entry(CommandKeywords.NAME, "task1"),
                Map.entry(CommandKeywords.DATE, "05/05/2021"), Map.entry(CommandKeywords.DESCRIPTION, "some task")));
        String actual = taskStorage.listTasks();
        assertEquals("Expected right task information.", "", actual);
    }

    @Test
    public void testListDashboardInboxSuccess() {
        addInboxTask();
        taskStorage.setTaskComponents(Map.ofEntries(Map.entry(CommandKeywords.TYPE, "inbox")));
        String expected = "INBOX TASK: [Name: task], [Description: some task], [Is finished: false]";
        String actual = taskStorage.listDashboard();
        assertEquals("Expected right task information.", expected, actual);
    }

    @Test
    public void testListDashboardOrdinaryTasksSuccess() {
        addOrdinaryTask();
        taskStorage.setTaskComponents(Map.ofEntries(Map.entry(CommandKeywords.TYPE, "ordinary")));
        String expected = "ORDINARY TASK: [Date: 2021-05-05], [Name: task], [Description: some task], [Is finished: false]";
        String actual = taskStorage.listDashboard();
        assertEquals("Expected right task information.", expected, actual);
    }

    @Test
    public void testListDashboardInboxTaskNotExisting() {
        taskStorage.setTaskComponents(Map.ofEntries(Map.entry(CommandKeywords.NAME, "task1"),
                Map.entry(CommandKeywords.DESCRIPTION, "some task new description")));
        String actual = taskStorage.listDashboard();
        assertEquals("Expected right task information.", "", actual);
    }

    @Test
    public void testListDashboardOrdinaryTaskNotExisting() {
        taskStorage.setTaskComponents(Map.ofEntries(Map.entry(CommandKeywords.NAME, "task1"),
                Map.entry(CommandKeywords.DATE, "05/05/2021"), Map.entry(CommandKeywords.DESCRIPTION, "some task")));
        String actual = taskStorage.listDashboard();
        assertNull("Expected right task information.", actual);
    }

    @Test
    public void testFinishTaskInboxSuccess() {
        addInboxTask();
        taskStorage.setTaskComponents(Map.ofEntries(Map.entry(CommandKeywords.NAME, "task"),
                Map.entry(CommandKeywords.DESCRIPTION, "some task")));
        assertTrue("Expected true when inbox task is finished.", taskStorage.finishTask());
    }

    @Test
    public void testFinishTaskOrdinarySuccess() {
        addOrdinaryTask();
        taskStorage.setTaskComponents(Map.ofEntries(Map.entry(CommandKeywords.NAME, "task"),
                Map.entry(CommandKeywords.DATE, "05/05/2021"),
                Map.entry(CommandKeywords.DESCRIPTION, "some task")));
        assertTrue("Expected true when inbox task is finished.", taskStorage.finishTask());
    }

    @Test
    public void testFinishTaskInboxTaskNotExisting() {
        taskStorage.setTaskComponents(Map.ofEntries(Map.entry(CommandKeywords.NAME, "task"),
                Map.entry(CommandKeywords.DESCRIPTION, "some task")));
        assertFalse("Expected false when inbox task is not existing.", taskStorage.finishTask());
    }

    @Test
    public void testFinishTaskOrdinaryTaskAlreadyExists() {
        taskStorage.setTaskComponents(Map.ofEntries(Map.entry(CommandKeywords.NAME, "task"),
                Map.entry(CommandKeywords.DATE, "05/05/2021"),
                Map.entry(CommandKeywords.DESCRIPTION, "some task")));
        assertFalse("Expected false when inbox task is not existing.", taskStorage.finishTask());
    }

    private void addInboxTask() {
        taskStorage.addTask(Map.ofEntries(Map.entry(CommandKeywords.NAME, "task"),
                Map.entry(CommandKeywords.DESCRIPTION, "some task")));
    }

    private void addOrdinaryTask() {
        taskStorage.addTask(Map.ofEntries(Map.entry(CommandKeywords.NAME, "task"),
                Map.entry(CommandKeywords.DATE, "05/05/2021"),
                Map.entry(CommandKeywords.DESCRIPTION, "some task")));
    }
}
