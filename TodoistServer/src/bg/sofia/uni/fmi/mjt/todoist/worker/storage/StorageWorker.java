package bg.sofia.uni.fmi.mjt.todoist.worker.storage;

import bg.sofia.uni.fmi.mjt.todoist.storage.Storage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StorageWorker {
    private final UserWorkspace userWorkspace;
    private static final List<Storage> storages = new ArrayList<>();

    public StorageWorker(UserWorkspace userWorkspace) {
        this.userWorkspace = userWorkspace;
        initializeStorages();
    }

    public void loadStorages(String user) throws IOException, ClassNotFoundException {
        for(Storage storage : storages){
            storage.loadContent(user);
        }
    }

    public void saveStorages(String user) throws IOException {
        for(Storage storage : storages){
            storage.saveContent(user);
        }
    }

    private void initializeStorages() {
       storages.add(userWorkspace.getTaskStorage());
       storages.add(userWorkspace.getCollaborationStorage());
       storages.add(userWorkspace.getLabelStorage());
    }
}
