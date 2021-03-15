package bg.sofia.uni.fmi.mjt.todoist.storage.content;

import bg.sofia.uni.fmi.mjt.todoist.command.enums.CommandKeywords;
import bg.sofia.uni.fmi.mjt.todoist.storage.content.collaboration.CollaborationStorage;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CollaborationStorageTest {
    private CollaborationStorage collaborationStorage;

    @Before
    public void setUp() {
        this.collaborationStorage = new CollaborationStorage();
    }

    @Test
    public void testAddCollaborationSuccess() {
        assertTrue("Expected true when adding new collaboration.",
                collaborationStorage.addCollaboration("collaboration"));
    }

    @Test
    public void testAddCollaborationAlreadyExisting() {
        collaborationStorage.addCollaboration("collaboration");
        assertFalse("Expected false when collaboration already exists.",
                collaborationStorage.addCollaboration("collaboration"));
    }

    @Test
    public void testDeleteCollaborationSuccess() throws IOException {
        collaborationStorage.addCollaboration("collaboration");
        assertTrue("Expected true when collaboration is deleted.",
                collaborationStorage.deleteCollaboration("collaboration"));
    }

    @Test
    public void testDeleteCollaborationNotExisting() throws IOException {
        assertFalse("Expected false when collaboration is not existing.",
                collaborationStorage.deleteCollaboration("collaboration"));
    }

    @Test
    public void testListCollaborationsSuccess()  {
        collaborationStorage.addCollaboration("collaboration1");
        collaborationStorage.addCollaboration("collaboration2");
        String expected = "[CREATED COLLABORATIONS]" + System.lineSeparator()
                + "=> COLLABORATION: [Name: collaboration2]" + System.lineSeparator()
                + "=> COLLABORATION: [Name: collaboration1]";
        String actual = collaborationStorage.listCollaborations();
        assertEquals("Expected listed collaborations.", expected, actual);
    }

    @Test
    public void testListCollaborationsNotExisting() {
        assertEquals("Expected listed collaborations.", "", collaborationStorage.listCollaborations());
    }

    @Test
    public void testAddUserSuccess() {
        collaborationStorage.addCollaboration("collaboration");
        assertTrue("Expected true when user is added.",
                collaborationStorage.addParticipant("collaboration", "user"));
    }

    @Test
    public void testAddUserNotExisting() {
        assertFalse("Expected false when collaboration is not existing.",
                collaborationStorage.addParticipant("collaboration", "user"));
    }

    @Test
    public void testAssigneeTaskNotExistingTask() {
        collaborationStorage.addCollaboration("collaboration");
        assertFalse("Expected false when task is assigned.",
                collaborationStorage.assignTask("collaboration",
                        Map.ofEntries(Map.entry(CommandKeywords.USER, "user"),
                                Map.entry(CommandKeywords.TASK, "task"))));
    }

    @Test
    public void testAssigneeTaskNotExisting() {
        assertFalse("Expected false when task is assigned.",
                collaborationStorage.assignTask("collaboration",
                        Map.ofEntries(Map.entry(CommandKeywords.USER, "user"),
                                Map.entry(CommandKeywords.TASK, "task"))));
    }

    @Test
    public void testListUsersSuccess() {
        String collaborationName = "collaboration";
        collaborationStorage.addCollaboration(collaborationName);
        collaborationStorage.addParticipant(collaborationName, "user1");
        collaborationStorage.addParticipant(collaborationName, "user2");
        String expected = collaborationStorage.listUsers(collaborationName);
        String actual = "[ALL PARTICIPANTS: [user1, user2]]";
        assertEquals("Expected listed collaboration participants.", expected, actual);
    }

    @Test
    public void testListUsersNotExisting() {
        assertEquals("Expected listed collaborations.", "",
                collaborationStorage.listUsers("collaboration"));
    }
}
