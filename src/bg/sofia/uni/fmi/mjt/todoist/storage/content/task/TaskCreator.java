package bg.sofia.uni.fmi.mjt.todoist.storage.content.task;

import bg.sofia.uni.fmi.mjt.todoist.command.enums.CommandKeywords;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class TaskCreator {
    protected static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");

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
