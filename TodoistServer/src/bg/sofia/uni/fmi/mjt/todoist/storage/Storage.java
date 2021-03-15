package bg.sofia.uni.fmi.mjt.todoist.storage;

import java.io.IOException;

public interface Storage {
    void loadContent(String user) throws IOException, ClassNotFoundException;

    void saveContent(String user) throws IOException;
}
