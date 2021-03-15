package bg.sofia.uni.fmi.mjt.todoist;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;

public class Main {
    private static final String COMMUNICATION_PROBLEM_MESSAGE = "There is a problem with the network communication";

    public static void main(String[] args) {
        ServerSocketChannel socketChannel;
        try {
            socketChannel = ServerSocketChannel.open();
        } catch (IOException e) {
            throw new IllegalStateException(COMMUNICATION_PROBLEM_MESSAGE, e);
        }
        TodoistServer todoistServer = new TodoistServer(socketChannel);
        todoistServer.start();
        todoistServer.stop();
    }
}
