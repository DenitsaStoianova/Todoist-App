package bg.sofia.uni.fmi.mjt.todoist;

import bg.sofia.uni.fmi.mjt.todoist.command.enums.CommandKeywords;
import bg.sofia.uni.fmi.mjt.todoist.command.enums.CommandType;
import bg.sofia.uni.fmi.mjt.todoist.command.type.Command;
import bg.sofia.uni.fmi.mjt.todoist.command.type.TaskCommand;
import bg.sofia.uni.fmi.mjt.todoist.storage.content.task.taskstorage.TaskStorage;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TaskCommandTest {
    private TaskStorage taskStorage;
    private Command taskCommand;

    private final Map<CommandKeywords, String> ordinaryTaskCommandTokens = new HashMap<>();
    private final Map<CommandKeywords, String> inboxTaskCommandTokens =  new HashMap<>();

    @Before
    public void setUp() {
        this.taskStorage = mock(TaskStorage.class);
        this.taskCommand = new TaskCommand(taskStorage);
        initializeOrdinaryTasksTokens();
        initializeInboxTasksTokens();
    }

    @Test
    public void testAddInboxTaskSuccess() {
        when(taskStorage.addTask(Map.ofEntries(Map.entry(CommandKeywords.NAME, "task")))).thenReturn(true);
        final String expected = "[ Task \"task\" was added successfully to your workspace. ]";
        String actual = taskCommand.execute(CommandType.ADD_TASK,
                Map.ofEntries(Map.entry(CommandKeywords.NAME, "task")));
        assertEquals("Add inbox task command is not correct.", expected, actual);
    }

    @Test
    public void testAddOrdinaryTaskSuccess() {
        when(taskStorage.addTask(ordinaryTaskCommandTokens)).thenReturn(true);
        final String expected = "[ Task \"task\" was added successfully to your workspace. ]";
        String actual = taskCommand.execute(CommandType.ADD_TASK, ordinaryTaskCommandTokens);
        assertEquals("Add ordinary task command is not correct.", expected, actual);
    }

    @Test
    public void testAddInboxTaskAlreadyExisting() {
        when(taskStorage.addTask(Map.ofEntries(Map.entry(CommandKeywords.NAME, "task")))).thenReturn(false);
        final String expected = "[ Task \"task\" already exists at your workspace, please select another name. ]";
        String actual = taskCommand.execute(CommandType.ADD_TASK,
                Map.ofEntries(Map.entry(CommandKeywords.NAME, "task")));
        assertEquals("Add inbox task command is not correct.", expected, actual);
    }

    @Test
    public void testAddOrdinaryTaskAlreadyExisting() {
        final Map<CommandKeywords, String> commandTokens = Map.ofEntries(Map.entry(CommandKeywords.NAME, "task"),
                Map.entry(CommandKeywords.DATE, "22/12/2021"));
        when(taskStorage.addTask(commandTokens)).thenReturn(false);
        final String expected = "[ Task \"task\" already exists at your workspace, please select another name. ]";
        String actual = taskCommand.execute(CommandType.ADD_TASK, commandTokens);
        assertEquals("Add ordinary task command is not correct.", expected, actual);
    }

    @Test
    public void testUpdateInboxTaskSuccess() {
        when(taskStorage.updateTask()).thenReturn(true);
        final String expected = "[ Task \"task\" was updated successfully. ]";
        String actual = taskCommand.execute(CommandType.UPDATE_TASK,
                Map.ofEntries(Map.entry(CommandKeywords.NAME, "task")));
        assertEquals("Update inbox task command is not correct.", expected, actual);
    }

    @Test
    public void testUpdateOrdinaryTaskSuccess() {
        when(taskStorage.updateTask()).thenReturn(true);
        final String expected = "[ Task \"task\" was updated successfully. ]";
        String actual = taskCommand.execute(CommandType.UPDATE_TASK, ordinaryTaskCommandTokens);
        assertEquals("Update ordinary task command is not correct.", expected, actual);
    }

    @Test
    public void testUpdateInboxTaskNotExisting() {
        when(taskStorage.updateTask()).thenReturn(false);
        final String expected = "[ Task \"task\" does not exist in your workspace. ]";
        String actual = taskCommand.execute(CommandType.UPDATE_TASK,
                Map.ofEntries(Map.entry(CommandKeywords.NAME, "task")));
        assertEquals("Update inbox task command is not correct.", expected, actual);
    }

    @Test
    public void testUpdateOrdinaryTaskNotExisting() {
        when(taskStorage.updateTask()).thenReturn(false);
        final String expected = "[ Task \"task\" does not exist in your workspace. ]";
        String actual = taskCommand.execute(CommandType.UPDATE_TASK, ordinaryTaskCommandTokens);
        assertEquals("Update ordinary task command is not correct.", expected, actual);
    }

    @Test
    public void testDeleteInboxTaskSuccess() {
        when(taskStorage.deleteTask()).thenReturn(true);
        final String expected = "[ Task \"task\" was deleted successfully from your workspace. ]";
        String actual = taskCommand.execute(CommandType.DELETE_TASK,
                Map.ofEntries(Map.entry(CommandKeywords.NAME, "task")));
        assertEquals("Delete inbox task command is not correct.", expected, actual);
    }

    @Test
    public void testDeleteOrdinaryTaskSuccess() {
        when(taskStorage.deleteTask()).thenReturn(true);
        final String expected = "[ Task \"task\" was deleted successfully from your workspace. ]";
        String actual = taskCommand.execute(CommandType.DELETE_TASK, ordinaryTaskCommandTokens);
        assertEquals("Delete ordinary task command is not correct.", expected, actual);
    }

    @Test
    public void testDeleteInboxTaskNotExisting() {
        when(taskStorage.deleteTask()).thenReturn(false);
        final String expected = "[ Task \"task\" does not exist in your workspace. ]";
        String actual = taskCommand.execute(CommandType.DELETE_TASK,
                Map.ofEntries(Map.entry(CommandKeywords.NAME, "task")));
        assertEquals("Delete inbox task command is not correct.", expected, actual);
    }

    @Test
    public void testDeleteOrdinaryTaskNotExisting() {
        when(taskStorage.deleteTask()).thenReturn(false);
        final String expected = "[ Task \"task\" does not exist in your workspace. ]";
        String actual = taskCommand.execute(CommandType.DELETE_TASK, ordinaryTaskCommandTokens);
        assertEquals("Delete ordinary task command is not correct.", expected, actual);
    }

    @Test
    public void testGetInboxTaskSuccess(){
        final String expected = String.format("INBOX TASK:%sName: task", System.lineSeparator());
        when(taskStorage.getTask()).thenReturn(expected);
        String actual = taskCommand.execute(CommandType.GET_TASK, inboxTaskCommandTokens);
        assertEquals("Get inbox task command is not correct.", expected, actual);
    }

    @Test
    public void testGetOrdinaryTaskSuccess(){
        final String expected = String.format("ORDINARY TASK:%sDate: 22/12/2021%sName: task",
                System.lineSeparator(), System.lineSeparator());
        when(taskStorage.getTask()).thenReturn(expected);
        String actual = taskCommand.execute(CommandType.GET_TASK, ordinaryTaskCommandTokens);
        assertEquals("Get ordinary task command is not correct.", expected, actual);
    }

    @Test
    public void testGetInboxTaskNotExisting(){
        when(taskStorage.getTask()).thenReturn(null);
        final String expected = "[ Task \"task\" does not exist in your workspace. ]";
        String actual = taskCommand.execute(CommandType.GET_TASK, inboxTaskCommandTokens);
        assertEquals("Get inbox task command is not correct.", expected, actual);
    }

    @Test
    public void testGetOrdinaryTaskNotExisting(){
        when(taskStorage.getTask()).thenReturn(null);
        final String expected = "[ Task \"task\" does not exist in your workspace. ]";
        String actual = taskCommand.execute(CommandType.GET_TASK, ordinaryTaskCommandTokens);
        assertEquals("Get ordinary task command is not correct.", expected, actual);
    }

    @Test
    public void testListInboxTasksSuccess(){
        final String expected = String.format("INBOX TASK:%sName: task", System.lineSeparator());
        when(taskStorage.listTasks()).thenReturn(expected);
        String actual = taskCommand.execute(CommandType.LIST_TASKS, inboxTaskCommandTokens);
        assertEquals("List inbox tasks command is not correct.", expected, actual);
    }

    @Test
    public void testListOrdinaryTasksSuccess(){
        final String expected = String.format("ORDINARY TASK:%sDate: 22/12/2021%sName: task",
                System.lineSeparator(), System.lineSeparator());
        when(taskStorage.listTasks()).thenReturn(expected);
        String actual = taskCommand.execute(CommandType.LIST_TASKS, ordinaryTaskCommandTokens);
        assertEquals("List ordinary tasks command is not correct.", expected, actual);
    }

    @Test
    public void testListOrdinaryTasksCompletedSuccess(){
        final String expected = String.format("ORDINARY TASK:%sDate: 22/12/2021%sName: task",
                System.lineSeparator(), System.lineSeparator());
        when(taskStorage.listTasks()).thenReturn(expected);
        ordinaryTaskCommandTokens.put(CommandKeywords.COMPLETED, "true");
        String actual = taskCommand.execute(CommandType.LIST_TASKS, ordinaryTaskCommandTokens);
        assertEquals("List ordinary tasks command is not correct.", expected, actual);
    }

    @Test
    public void testListInboxTasksNoTasks(){
        when(taskStorage.listTasks()).thenReturn("");
        final String expected = "[ There are no tasks in your workspace. ]";
        String actual = taskCommand.execute(CommandType.LIST_TASKS, inboxTaskCommandTokens);
        assertEquals("List inbox tasks command is not correct.", expected, actual);
    }

    @Test
    public void testListOrdinaryTasksNoTasks(){
        when(taskStorage.listTasks()).thenReturn("");
        final String expected = "[ There are no tasks in your workspace. ]";
        String actual = taskCommand.execute(CommandType.LIST_TASKS, ordinaryTaskCommandTokens);
        assertEquals("List ordinary tasks command is not correct.", expected, actual);
    }

    @Test
    public void testListDashboardSuccess(){
        final String expected = String.format("ORDINARY TASK:%sDate: 22/12/2021%sName: task%sDue date: %s",
                System.lineSeparator(), System.lineSeparator(),System.lineSeparator(), LocalDate.now());
        when(taskStorage.listDashboard()).thenReturn(expected);
        String actual = taskCommand.execute(CommandType.LIST_DASHBOARD,
                Map.ofEntries(Map.entry(CommandKeywords.TYPE, "ordinary")));
        assertEquals("List dashboard command is not correct.", expected, actual);
    }

    @Test
    public void testListDashboardNoTasks(){
        when(taskStorage.listDashboard()).thenReturn("");
        final String expected = "[ There are no tasks for today in your workspace. ]";
        String actual = taskCommand.execute(CommandType.LIST_DASHBOARD,
                Map.ofEntries(Map.entry(CommandKeywords.TYPE, "ordinary")));
        assertEquals("List dashboard command is not correct.", expected, actual);
    }

    @Test
    public void testFinishInboxTaskSuccess(){
        when(taskStorage.finishTask()).thenReturn(true);
        final String expected = "[ Task \"task\" was finished successfully. ]";
        String actual = taskCommand.execute(CommandType.FINISH_TASK, inboxTaskCommandTokens);
        assertEquals("Finish inbox task command is not correct.", expected, actual);
    }

    @Test
    public void testFinishOrdinaryTaskSuccess(){
        when(taskStorage.finishTask()).thenReturn(true);
        final String expected = "[ Task \"task\" was finished successfully. ]";
        String actual = taskCommand.execute(CommandType.FINISH_TASK, ordinaryTaskCommandTokens);
        assertEquals("Finish ordinary task command is not correct.", expected, actual);
    }

    @Test
    public void testFinishInboxTaskNotExisting(){
        when(taskStorage.finishTask()).thenReturn(false);
        final String expected ="[ Task \"task\" does not exist in your workspace. ]";
        String actual = taskCommand.execute(CommandType.FINISH_TASK, inboxTaskCommandTokens);
        assertEquals("Finish inbox task command is not correct.", expected, actual);
    }

    @Test
    public void testFinishOrdinaryTaskNotExisting(){
        when(taskStorage.finishTask()).thenReturn(false);
        final String expected = "[ Task \"task\" does not exist in your workspace. ]";
        String actual = taskCommand.execute(CommandType.FINISH_TASK, ordinaryTaskCommandTokens);
        assertEquals("Finish ordinary task command is not correct.", expected, actual);
    }

    private void initializeOrdinaryTasksTokens(){
        ordinaryTaskCommandTokens.put(CommandKeywords.NAME, "task");
        ordinaryTaskCommandTokens.put(CommandKeywords.DATE, "22/12/2021");
    }

    private void initializeInboxTasksTokens(){
        inboxTaskCommandTokens.put(CommandKeywords.NAME, "task");
    }


}
