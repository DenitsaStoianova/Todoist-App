package bg.sofia.uni.fmi.mjt.todoist;

import bg.sofia.uni.fmi.mjt.todoist.command.enums.CommandKeywords;
import bg.sofia.uni.fmi.mjt.todoist.command.enums.CommandType;
import bg.sofia.uni.fmi.mjt.todoist.command.type.CollaborationCommand;
import bg.sofia.uni.fmi.mjt.todoist.command.type.Command;
import bg.sofia.uni.fmi.mjt.todoist.storage.content.collaboration.CollaborationStorage;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CollaborationCommandTest {
    private CollaborationStorage collaborationStorage;
    private Command collaborationCommand;

    @Before
    public void setUp() {
        this.collaborationStorage = mock(CollaborationStorage.class);
        this.collaborationCommand = new CollaborationCommand(collaborationStorage);
    }

    @Test
    public void testAddCollaborationSuccess() {
        when(collaborationStorage.addCollaboration("group-project")).thenReturn(true);
        final String expected = "[ Collaboration \"group-project\" was added successfully to your workspace. ]";
        String actual = collaborationCommand.execute(CommandType.ADD_COLLABORATION,
                Map.ofEntries(Map.entry(CommandKeywords.NAME, "group-project")));
        assertEquals("Add collaboration command message is not correct.", expected, actual);
    }

    @Test
    public void testAddCollaborationAlreadyExisting() {
        when(collaborationStorage.addCollaboration("group-project")).thenReturn(false);
        final String expected = "[ Collaboration \"group-project\" already exists your workspace. ]";
        String actual = collaborationCommand.execute(CommandType.ADD_COLLABORATION,
                Map.ofEntries(Map.entry(CommandKeywords.NAME, "group-project")));
        assertEquals("Add collaboration command message is not correct.", expected, actual);
    }

    @Test
    public void testDeleteCollaborationSuccess() {
        when(collaborationStorage.deleteCollaboration("group-project")).thenReturn(true);
        final String expected = "[ Collaboration \"group-project\" was successfully deleted from your workspace. ]";
        String actual = collaborationCommand.execute(CommandType.DELETE_COLLABORATION,
                Map.ofEntries(Map.entry(CommandKeywords.NAME, "group-project")));
        assertEquals("Delete collaboration command message is not correct.", expected, actual);
    }

    @Test
    public void testDeleteCollaborationNotExisting() {
        when(collaborationStorage.deleteCollaboration("group-project")).thenReturn(false);
        final String expected = "[ Collaboration \"group-project\" does not exists at your workspace. ]";
        String actual = collaborationCommand.execute(CommandType.DELETE_COLLABORATION,
                Map.ofEntries(Map.entry(CommandKeywords.NAME, "group-project")));
        assertEquals("Delete collaboration command message is not correct.", expected, actual);
    }

    @Test
    public void testAddUserSuccess() {
        when(collaborationStorage.addParticipant("group-project", "Kevin")).thenReturn(true);
        final String expected = "[ User \"Kevin\" was successfully added to collaboration \"group-project\". ]";
        String actual = collaborationCommand.execute(CommandType.ADD_USER,
                Map.ofEntries(Map.entry(CommandKeywords.COLLABORATION, "group-project"),
                        Map.entry(CommandKeywords.USER, "Kevin")));
        assertEquals("Add user to collaboration command message is not correct.", expected, actual);
    }

    @Test
    public void testAddUserNotExisting() {
        when(collaborationStorage.addParticipant("group-project", "Kevin")).thenReturn(false);
        final String expected = "[ Collaboration \"group-project\" does not exists at your workspace. ]";
        String actual = collaborationCommand.execute(CommandType.ADD_USER,
                Map.ofEntries(Map.entry(CommandKeywords.COLLABORATION, "group-project"),
                        Map.entry(CommandKeywords.USER, "Kevin")));
        assertEquals("Add user to collaboration command message is not correct.", expected, actual);
    }

    @Test
    public void testAssignTaskSuccess() {
        when(collaborationStorage.assignTask("group-project",
                Map.ofEntries(Map.entry(CommandKeywords.COLLABORATION, "group-project"),
                        Map.entry(CommandKeywords.USER, "Kevin"), Map.entry(CommandKeywords.TASK, "task"))))
                .thenReturn(true);
        final String expected = "[ Task \"task\" from collaboration \"group-project\" was assigned to user \"Kevin\". ]";
        String actual = collaborationCommand.execute(CommandType.ASSIGN_TASK,
                Map.ofEntries(Map.entry(CommandKeywords.COLLABORATION, "group-project"),
                        Map.entry(CommandKeywords.USER, "Kevin"), Map.entry(CommandKeywords.TASK, "task")));
        assertEquals("Assign task to user at collaboration command message is not correct.", expected, actual);
    }

    @Test
    public void testAssignTaskFailure() {
        when(collaborationStorage.assignTask("group-project",
                Map.ofEntries(Map.entry(CommandKeywords.COLLABORATION, "group-project"),
                        Map.entry(CommandKeywords.USER, "Kevin"), Map.entry(CommandKeywords.TASK, "task"))))
                .thenReturn(false);
        final String expected = "[ Task \"task\" from collaboration \"group-project\" cannot be assigned " +
                "to user \"Kevin\", check your data correctness. ]";
        String actual = collaborationCommand.execute(CommandType.ASSIGN_TASK,
                Map.ofEntries(Map.entry(CommandKeywords.COLLABORATION, "group-project"),
                        Map.entry(CommandKeywords.USER, "Kevin"), Map.entry(CommandKeywords.TASK, "task")));
        assertEquals("Assign task to user at collaboration command message is not correct.", expected, actual);
    }


}
