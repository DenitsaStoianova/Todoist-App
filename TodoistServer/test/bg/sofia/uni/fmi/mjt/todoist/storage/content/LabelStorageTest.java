package bg.sofia.uni.fmi.mjt.todoist.storage.content;

import bg.sofia.uni.fmi.mjt.todoist.storage.content.label.LabelStorage;
import bg.sofia.uni.fmi.mjt.todoist.storage.content.task.InboxTask;
import bg.sofia.uni.fmi.mjt.todoist.storage.content.task.OrdinaryTask;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class LabelStorageTest {
    private LabelStorage labelStorage;

    @Before
    public void setUp() {
        this.labelStorage = new LabelStorage();
    }

    @Test
    public void testAddLabelSuccess() {
        assertTrue("Expected true when new label is added.", labelStorage.addLabel("label"));
    }

    @Test
    public void testAddLabelAlreadyExists() {
        labelStorage.addLabel("label");
        assertFalse("Expected false when added label already exists.", labelStorage.addLabel("label"));
    }

    @Test
    public void testDeleteLabelSuccess() {
        labelStorage.addLabel("label");
        assertTrue("Expected true when label is deleted.", labelStorage.deleteLabel("label"));
    }

    @Test
    public void testDeleteLabelNotExisting() {
        assertFalse("Expected false when label is not existing.", labelStorage.deleteLabel("label"));
    }

    @Test
    public void testListLabelsSuccess() {
        labelStorage.addLabel("label1");
        labelStorage.addLabel("label2");
        assertEquals("Expected listed labels.", "[label1, label2]", labelStorage.listLabels());
    }

    @Test
    public void testListLabelsNotExisting() {
        assertEquals("Expected listed labels.", "[]", labelStorage.listLabels());
    }

    @Test
    public void testLabelTaskSuccess(){
        labelStorage.addLabel("label");
        assertTrue("Expected true when task is labeled.",
                labelStorage.labelTask(new InboxTask("task"),"label"));
    }

    @Test
    public void testLabelTaskNotExistingLabel(){
        assertFalse("Expected false when label is not existing.",
                labelStorage.labelTask(new InboxTask("task"),"label"));
    }

    @Test
    public void testListTasksSuccess() {
        String labelName = "label";
        labelStorage.addLabel(labelName);
        labelStorage.labelTask(new InboxTask("inbox"), labelName);
        labelStorage.labelTask(new OrdinaryTask("ordinary"), labelName);
        String actual = "INBOX TASK: [Name: inbox], [Label: label], [Is finished: false]" + System.lineSeparator()
                + "ORDINARY TASK: [Date: null], [Name: ordinary], [Label: label], [Is finished: false]";
        String expected = labelStorage.listTasks("label");
        assertEquals("Expected listed labeled tasks.", expected, actual);
    }

    @Test
    public void testListTasksNotExisting() {
        assertNull("Expected listed labels.", labelStorage.listTasks("label"));
    }

}
