package bg.sofia.uni.fmi.mjt.todoist;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;

public class ServerResponseHandler implements Runnable {
    private static final String COMMUNICATION_PROBLEM_MESSAGE = "There is a problem with the network communication";

    private static final String ENCODING = "UTF-8";
    private final SocketChannel socketChannel;
    private static final String NEW_COMMAND_LINE = "$ ";
    private static final String MSG_END = "#F#";

    public ServerResponseHandler(final SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(Channels.newReader(socketChannel, ENCODING))) {
            while (true) {
                String reply = readServerReply(reader);
                if (reply == null) {
                    break;
                }
                System.out.print(reply);
                System.out.print(NEW_COMMAND_LINE);
            }
        } catch (IOException e) {
            throw new IllegalStateException(COMMUNICATION_PROBLEM_MESSAGE, e);
        }
    }

    public String readServerReply(BufferedReader reader) throws IOException {
        StringBuilder replyMessageBuilder = new StringBuilder();
        String serverReplyLine;
        while (!(serverReplyLine = reader.readLine()).equals(MSG_END)) {
            replyMessageBuilder.append(serverReplyLine).append(System.lineSeparator());
        }
        return replyMessageBuilder.toString();
    }
}