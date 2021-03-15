package bg.sofia.uni.fmi.mjt.todoist.command;

import bg.sofia.uni.fmi.mjt.todoist.command.enums.CommandKeywords;
import bg.sofia.uni.fmi.mjt.todoist.command.enums.CommandType;
import bg.sofia.uni.fmi.mjt.todoist.command.type.CollaborationTaskCommand;
import bg.sofia.uni.fmi.mjt.todoist.storage.content.collaboration.CollaborationTaskStorage;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CollaborationTaskCommandTest {
    private CollaborationTaskStorage collaborationStorage;
    private CollaborationTaskCommand collaborationTaskCommand;

    private final Map<CommandKeywords, String> ordinaryTaskCommandTokens = new HashMap<>();
    private final Map<CommandKeywords, String> inboxTaskCommandTokens = new HashMap<>();

    @Before
    public void setUp() {
        this.collaborationStorage = mock(CollaborationTaskStorage.class);
        this.collaborationTaskCommand = new CollaborationTaskCommand(collaborationStorage, "project");
        initializeOrdinaryTasksTokens();
        initializeInboxTasksTokens();
    }

    @Test
    public void testAddInboxTaskSuccess() {
        when(collaborationStorage.addTask(Map.ofEntries(Map.entry(CommandKeywords.NAME, "task")))).thenReturn(true);
        final String expected = "[ Task \"task\" was added successfully to collaboration \"project\". ]";
        String actual = collaborationTaskCommand.executeTaskCommands(CommandType.ADD_TASK,
                Map.ofEntries(Map.entry(CommandKeywords.NAME, "task")));
        assertEquals("Add inbox task command is not correct.", expected, actual);
    }


    @Test
    public void testAddOrdinaryTaskSuccess() {
        when(collaborationStorage.addTask(ordinaryTaskCommandTokens)).thenReturn(true);
        final String expected = "[ Task \"task\" was added successfully to collaboration \"project\". ]";
        String actual = collaborationTaskCommand.executeTaskCommands(CommandType.ADD_TASK, ordinaryTaskCommandTokens);
        assertEquals("Add ordinary task command is not correct.", expected, actual);
    }

    @Test
    public void testAddInboxTaskAlreadyExisting() {
        when(collaborationStorage.addTask(Map.ofEntries(Map.entry(CommandKeywords.NAME, "task")))).thenReturn(false);
        final String expected = "[ Task \"task\" already exists at \"project\" collaboration, please select another name. ]";
        String actual = collaborationTaskCommand.executeTaskCommands(CommandType.ADD_TASK,
                Map.ofEntries(Map.entry(CommandKeywords.NAME, "task")));
        assertEquals("Add inbox task command is not correct.", expected, actual);
    }

    @Test
    public void testAddOrdinaryTaskAlreadyExisting() {
        final Map<CommandKeywords, String> commandTokens = Map.ofEntries(Map.entry(CommandKeywords.NAME, "task"),
                Map.entry(CommandKeywords.DATE, "22/12/2021"));
        when(collaborationStorage.addTask(commandTokens)).thenReturn(false);
        final String expected = "[ Task \"task\" already exists at \"project\" collaboration, please select another name. ]";
        String actual = collaborationTaskCommand.executeTaskCommands(CommandType.ADD_TASK, commandTokens);
        assertEquals("Add ordinary task command is not correct.", expected, actual);
    }


    @Test
    public void testUpdateInboxTaskSuccess() {
        when(collaborationStorage.updateTask()).thenReturn(true);
        final String expected = "[ Task \"task\" was updated successfully to collaboration \"project\". ]";
        String actual = collaborationTaskCommand.executeTaskCommands(CommandType.UPDATE_TASK,
                Map.ofEntries(Map.entry(CommandKeywords.NAME, "task")));
        assertEquals("Update inbox task command is not correct.", expected, actual);
    }

    @Test
    public void testUpdateOrdinaryTaskSuccess() {
        when(collaborationStorage.updateTask()).thenReturn(true);
        final String expected = "[ Task \"task\" was updated successfully to collaboration \"project\". ]";
        String actual = collaborationTaskCommand.executeTaskCommands(CommandType.UPDATE_TASK, ordinaryTaskCommandTokens);
        assertEquals("Update ordinary task command is not correct.", expected, actual);
    }

    @Test
    public void testUpdateInboxTaskNotExisting() {
        when(collaborationStorage.updateTask()).thenReturn(false);
        final String expected = "[ Task \"task\" does not exist in collaboration \"project\". ]";
        String actual = collaborationTaskCommand.executeTaskCommands(CommandType.UPDATE_TASK,
                Map.ofEntries(Map.entry(CommandKeywords.NAME, "task")));
        assertEquals("Update inbox task command is not correct.", expected, actual);
    }

    @Test
    public void testUpdateOrdinaryTaskNotExisting() {
        when(collaborationStorage.updateTask()).thenReturn(false);
        final String expected = "[ Task \"task\" does not exist in collaboration \"project\". ]";
        String actual = collaborationTaskCommand.executeTaskCommands(CommandType.UPDATE_TASK, ordinaryTaskCommandTokens);
        assertEquals("Update ordinary task command is not correct.", expected, actual);
    }

    @Test
    public void testDeleteInboxTaskSuccess() {
        when(collaborationStorage.deleteTask()).thenReturn(true);
        final String expected = "[ Task \"task\" was deleted successfully at collaboration \"project\". ]";
        String actual = collaborationTaskCommand.executeTaskCommands(CommandType.DELETE_TASK,
                Map.ofEntries(Map.entry(CommandKeywords.NAME, "task")));
        assertEquals("Delete inbox task command is not correct.", expected, actual);
    }

    @Test
    public void testDeleteOrdinaryTaskSuccess() {
        when(collaborationStorage.deleteTask()).thenReturn(true);
        final String expected = "[ Task \"task\" was deleted successfully at collaboration \"project\". ]";
        String actual = collaborationTaskCommand.executeTaskCommands(CommandType.DELETE_TASK, ordinaryTaskCommandTokens);
        assertEquals("Delete ordinary task command is not correct.", expected, actual);
    }

    @Test
    public void testDeleteInboxTaskNotExisting() {
        when(collaborationStorage.deleteTask()).thenReturn(false);
        final String expected = "[ Task \"task\" does not exist in collaboration \"project\". ]";
        String actual = collaborationTaskCommand.executeTaskCommands(CommandType.DELETE_TASK,
                Map.ofEntries(Map.entry(CommandKeywords.NAME, "task")));
        assertEquals("Delete inbox task command is not correct.", expected, actual);
    }

    @Test
    public void testDeleteOrdinaryTaskNotExisting() {
        when(collaborationStorage.deleteTask()).thenReturn(false);
        final String expected = "[ Task \"task\" does not exist in collaboration \"project\". ]";
        String actual = collaborationTaskCommand.executeTaskCommands(CommandType.DELETE_TASK, ordinaryTaskCommandTokens);
        assertEquals("Delete ordinary task command is not correct.", expected, actual);
    }

    @Test
    public void testGetInboxTaskSuccess() {
        final String expected = String.format("INBOX TASK:%sName: task", System.lineSeparator());
        when(collaborationStorage.getTask()).thenReturn(expected);
        String actual = collaborationTaskCommand.executeTaskCommands(CommandType.GET_TASK, inboxTaskCommandTokens);
        assertEquals("Get inbox task command is not correct.", expected, actual);
    }

    @Test
    public void testGetOrdinaryTaskSuccess() {
        final String expected = String.format("ORDINARY TASK:%sDate: 22/12/2021%sName: task",
                System.lineSeparator(), System.lineSeparator());
        when(collaborationStorage.getTask()).thenReturn(expected);
        String actual = collaborationTaskCommand.executeTaskCommands(CommandType.GET_TASK, ordinaryTaskCommandTokens);
        assertEquals("Get ordinary task command is not correct.", expected, actual);
    }

    @Test
    public void testGetInboxTaskNotExisting() {
        when(collaborationStorage.getTask()).thenReturn(null);
        final String expected = "[ Task \"task\" does not exist in collaboration \"project\". ]";
        String actual = collaborationTaskCommand.executeTaskCommands(CommandType.GET_TASK, inboxTaskCommandTokens);
        assertEquals("Get inbox task command is not correct.", expected, actual);
    }

    @Test
    public void testGetOrdinaryTaskNotExisting() {
        when(collaborationStorage.getTask()).thenReturn(null);
        final String expected = "[ Task \"task\" does not exist in collaboration \"project\". ]";
        String actual = collaborationTaskCommand.executeTaskCommands(CommandType.GET_TASK, ordinaryTaskCommandTokens);
        assertEquals("Get ordinary task command is not correct.", expected, actual);
    }

    @Test
    public void testListInboxTasksSuccess() {
        final String expected = String.format("INBOX TASK:%sName: task", System.lineSeparator());
        when(collaborationStorage.listTasks()).thenReturn(expected);
        String actual = collaborationTaskCommand.executeTaskCommands(CommandType.LIST_TASKS, inboxTaskCommandTokens);
        assertEquals("List inbox tasks command is not correct.", expected, actual);
    }

    @Test
    public void testListOrdinaryTasksSuccess() {
        final String expected = String.format("ORDINARY TASK:%sDate: 22/12/2021%sName: task",
                System.lineSeparator(), System.lineSeparator());
        when(collaborationStorage.listTasks()).thenReturn(expected);
        String actual = collaborationTaskCommand.executeTaskCommands(CommandType.LIST_TASKS, ordinaryTaskCommandTokens);
        assertEquals("List ordinary tasks command is not correct.", expected, actual);
    }

    @Test
    public void testListOrdinaryTasksCompletedSuccess() {
        final String expected = String.format("ORDINARY TASK:%sDate: 22/12/2021%sName: task",
                System.lineSeparator(), System.lineSeparator());
        when(collaborationStorage.listTasks()).thenReturn(expected);
        ordinaryTaskCommandTokens.put(CommandKeywords.COMPLETED, "true");
        String actual = collaborationTaskCommand.executeTaskCommands(CommandType.LIST_TASKS, ordinaryTaskCommandTokens);
        assertEquals("List ordinary tasks command is not correct.", expected, actual);
    }

    @Test
    public void testListInboxTasksNoTasks() {
        when(collaborationStorage.listTasks()).thenReturn("");
        final String expected = "[ There are no tasks in collaboration \"project\". ]";
        String actual = collaborationTaskCommand.executeTaskCommands(CommandType.LIST_TASKS, inboxTaskCommandTokens);
        assertEquals("List inbox tasks command is not correct.", expected, actual);
    }

    @Test
    public void testListOrdinaryTasksNoTasks() {
        when(collaborationStorage.listTasks()).thenReturn("");
        final String expected = "[ There are no tasks in collaboration \"project\". ]";
        String actual = collaborationTaskCommand.executeTaskCommands(CommandType.LIST_TASKS, ordinaryTaskCommandTokens);
        assertEquals("List ordinary tasks command is not correct.", expected, actual);
    }

    @Test
    public void testListDashboardSuccess() {
        final String expected = String.format("ORDINARY TASK:%sDate: 22/12/2021%sName: task%sDue date: %s",
                System.lineSeparator(), System.lineSeparator(), System.lineSeparator(), LocalDate.now());
        when(collaborationStorage.listDashboard()).thenReturn(expected);
        String actual = collaborationTaskCommand.executeTaskCommands(CommandType.LIST_DASHBOARD,
                Map.ofEntries(Map.entry(CommandKeywords.TYPE, "ordinary")));
        assertEquals("List dashboard command is not correct.", expected, actual);
    }

    @Test
    public void testListDashboardNoTasks() {
        when(collaborationStorage.listDashboard()).thenReturn("");
        final String expected = "[ There are no tasks for today in collaboration \"project\". ]";
        String actual = collaborationTaskCommand.executeTaskCommands(CommandType.LIST_DASHBOARD,
                Map.ofEntries(Map.entry(CommandKeywords.TYPE, "ordinary")));
        assertEquals("List dashboard command is not correct.", expected, actual);
    }

    @Test
    public void testFinishInboxTaskSuccess() {
        when(collaborationStorage.finishTask()).thenReturn(true);
        final String expected = "[ Task \"task\" was finished successfully at collaboration \"project\". ]";
        String actual = collaborationTaskCommand.executeTaskCommands(CommandType.FINISH_TASK, inboxTaskCommandTokens);
        assertEquals("Finish inbox task command is not correct.", expected, actual);
    }

    @Test
    public void testFinishOrdinaryTaskSuccess() {
        when(collaborationStorage.finishTask()).thenReturn(true);
        final String expected = "[ Task \"task\" was finished successfully at collaboration \"project\". ]";
        String actual = collaborationTaskCommand.executeTaskCommands(CommandType.FINISH_TASK, ordinaryTaskCommandTokens);
        assertEquals("Finish ordinary task command is not correct.", expected, actual);
    }

    @Test
    public void testFinishInboxTaskNotExisting() {
        when(collaborationStorage.finishTask()).thenReturn(false);
        final String expected = "[ Task \"task\" does not exist in collaboration \"project\". ]";
        String actual = collaborationTaskCommand.executeTaskCommands(CommandType.FINISH_TASK, inboxTaskCommandTokens);
        assertEquals("Finish inbox task command is not correct.", expected, actual);
    }

    @Test
    public void testFinishOrdinaryTaskNotExisting() {
        when(collaborationStorage.finishTask()).thenReturn(false);
        final String expected = "[ Task \"task\" does not exist in collaboration \"project\". ]";
        String actual = collaborationTaskCommand.executeTaskCommands(CommandType.FINISH_TASK, ordinaryTaskCommandTokens);
        assertEquals("Finish ordinary task command is not correct.", expected, actual);
    }

    private void initializeOrdinaryTasksTokens() {
        ordinaryTaskCommandTokens.put(CommandKeywords.NAME, "task");
        ordinaryTaskCommandTokens.put(CommandKeywords.DATE, "22/12/2021");
    }

    private void initializeInboxTasksTokens() {
        inboxTaskCommandTokens.put(CommandKeywords.NAME, "task");
    }


}
