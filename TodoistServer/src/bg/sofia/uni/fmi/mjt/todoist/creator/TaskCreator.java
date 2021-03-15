package bg.sofia.uni.fmi.mjt.todoist.creator;

import bg.sofia.uni.fmi.mjt.todoist.command.enums.CommandKeywords;
import bg.sofia.uni.fmi.mjt.todoist.storage.content.task.CollaborationInboxTask;
import bg.sofia.uni.fmi.mjt.todoist.storage.content.task.CollaborationOrdinaryTask;
import bg.sofia.uni.fmi.mjt.todoist.storage.content.task.InboxTask;
import bg.sofia.uni.fmi.mjt.todoist.storage.content.task.OrdinaryTask;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class TaskCreator {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");

    /*
    public boolean addTask(Map<CommandKeywords, String> taskComponents) { // ORDINARY
        String taskName = taskComponents.get(CommandKeywords.NAME);
        LocalDate taskDate = LocalDate.parse(taskComponents.get(CommandKeywords.DATE), formatter);
        if (ordinaryTasks.containsKey(taskDate) && ordinaryTasks.get(taskDate).containsKey(taskName)) {
            return false;
        } else if (ordinaryTasks.containsKey(taskDate) && !ordinaryTasks.get(taskDate).containsKey(taskName)) {
            ordinaryTasks.get(taskDate).put(taskName, TaskCreator.createCollaborationOrdinaryTask(taskComponents));
        } else {
            Map<String, CollaborationOrdinaryTask> tasks = new HashMap<>();
            tasks.put(taskName, TaskCreator.createCollaborationOrdinaryTask(taskComponents));
            ordinaryTasks.put(taskDate, tasks);
        }
        return true;
    }
    
     */

    public static InboxTask createInboxTask(Map<CommandKeywords, String> commandTokens) {
        InboxTask inboxTask = new InboxTask(commandTokens.get(CommandKeywords.NAME));
        for (Map.Entry<CommandKeywords, String> component : commandTokens.entrySet()) {
            switch (component.getKey()) {
                case DUE_DATE -> inboxTask.setDueDate(LocalDate.parse(component.getValue(), formatter));
                case DESCRIPTION -> inboxTask.setDescription(component.getValue());
            }
        }
        return inboxTask;
    }

    public static OrdinaryTask createOrdinaryTask(Map<CommandKeywords, String> commandTokens) {
        OrdinaryTask ordinaryTask = new OrdinaryTask(commandTokens.get(CommandKeywords.NAME));
        for (Map.Entry<CommandKeywords, String> component : commandTokens.entrySet()) {
            switch (component.getKey()) {
                case DATE -> ordinaryTask.setDate(LocalDate.parse(component.getValue(), formatter));
                case DUE_DATE -> ordinaryTask.setDueDate(LocalDate.parse(component.getValue(), formatter));
                case DESCRIPTION -> ordinaryTask.setDescription(component.getValue());
            }
        }
        return ordinaryTask;
    }

    public static CollaborationInboxTask createCollaborationInboxTask(Map<CommandKeywords, String> commandTokens) {
        CollaborationInboxTask inboxTask = new CollaborationInboxTask(commandTokens.get(CommandKeywords.NAME));
        for (Map.Entry<CommandKeywords, String> component : commandTokens.entrySet()) {
            switch (component.getKey()) {
                case DUE_DATE -> inboxTask.setDueDate(LocalDate.parse(component.getValue(), formatter));
                case DESCRIPTION -> inboxTask.setDescription(component.getValue());
            }
        }
        return inboxTask;
    }

    public static CollaborationOrdinaryTask createCollaborationOrdinaryTask(Map<CommandKeywords, String> commandTokens) {
        CollaborationOrdinaryTask ordinaryTask = new CollaborationOrdinaryTask(commandTokens.get(CommandKeywords.NAME));
        for (Map.Entry<CommandKeywords, String> component : commandTokens.entrySet()) {
            switch (component.getKey()) {
                case DATE -> ordinaryTask.setDate(LocalDate.parse(component.getValue(), formatter));
                case DUE_DATE -> ordinaryTask.setDueDate(LocalDate.parse(component.getValue(), formatter));
                case DESCRIPTION -> ordinaryTask.setDescription(component.getValue());
            }
        }
        return ordinaryTask;
    }
}
