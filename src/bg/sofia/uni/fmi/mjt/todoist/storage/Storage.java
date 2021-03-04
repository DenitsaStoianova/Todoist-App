package bg.sofia.uni.fmi.mjt.todoist.storage;

public interface Storage {
    void loadContent(String user);

    void saveContent(String user);
}
