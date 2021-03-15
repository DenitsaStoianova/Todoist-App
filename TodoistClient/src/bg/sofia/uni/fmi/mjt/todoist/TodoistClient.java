package bg.sofia.uni.fmi.mjt.todoist;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class TodoistClient {
    private static final String COMMUNICATION_PROBLEM_MESSAGE = "There is a problem with the network communication";

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_HOST = "localhost";
    private static final String ENCODING = "UTF-8";
    private static final String DISCONNECT_COMMAND = "disconnect";
    private static final String NEW_COMMAND_LINE = "$ ";

    public void start() {
        try (SocketChannel socketChannel = SocketChannel.open();
             PrintWriter writer = new PrintWriter(Channels.newWriter(socketChannel, ENCODING), true);
             Scanner scanner = new Scanner(System.in)) {

            socketChannel.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT));
            Thread client = new Thread(new ServerResponseHandler(socketChannel));
            client.start();

            System.out.print(NEW_COMMAND_LINE);
            while (true) {
                String message = scanner.nextLine();
                if (message.equals(DISCONNECT_COMMAND)) {
                    client.join();
                    break;
                }
                writer.println(message);
            }
        } catch (IOException | InterruptedException e) {
            throw new IllegalStateException(COMMUNICATION_PROBLEM_MESSAGE, e);
        }
    }
}
