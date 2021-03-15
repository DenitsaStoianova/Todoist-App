package bg.sofia.uni.fmi.mjt.todoist;

import bg.sofia.uni.fmi.mjt.todoist.command.type.StartupCommand;
import bg.sofia.uni.fmi.mjt.todoist.exceptions.EmptyCommandException;
import bg.sofia.uni.fmi.mjt.todoist.exceptions.InvalidCommandArgumentsException;
import bg.sofia.uni.fmi.mjt.todoist.exceptions.InvalidCommandNameException;
import bg.sofia.uni.fmi.mjt.todoist.storage.user.UserStorage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class TodoistServer {
    private static final String ERROR_OCCURRED_MSG = "An error has occurred: ";
    private static final String SOCKET_EXCEPTION_MSG = "Exception thrown by close socket: ";
    private static final String SENDING_REPLY_EXCEPTION_MSG = "There is a problem with sending a message: ";
    private static final String EXCEPTION_START_MSG = "ERROR: ";
    private static final String MSG_END = "#F#";

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 7777;
    private static final int BUFFER_SIZE = 1024;

    private final ServerSocketChannel serverSocketChannel;
    private final ByteBuffer commandByteBuffer;
    private Selector selector;

    private final StartupCommand startupCommandExecutor;
    private boolean isClosed;

    public TodoistServer(ServerSocketChannel serverSocketChannel) {
        this.serverSocketChannel = serverSocketChannel;
        this.commandByteBuffer = ByteBuffer.allocate(BUFFER_SIZE);
        this.startupCommandExecutor = new StartupCommand(new UserStorage());
    }

    public void start() {
        try {
            serverSocketChannel.bind(new InetSocketAddress(SERVER_HOST, SERVER_PORT));
            serverSocketChannel.configureBlocking(false);

            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (!isClosed) {
                int readyChannels = selector.select();
                if (readyChannels == 0) {
                    continue;
                }
                handleReadyChannels();
            }
        } catch (IOException e) {
            System.err.println(ERROR_OCCURRED_MSG + e.getMessage());
        } finally {
            try {
                serverSocketChannel.close();
            } catch (IOException e) {
                System.err.println(SOCKET_EXCEPTION_MSG + e.getMessage());
            }
        }
    }

    private void handleReadyChannels() throws IOException {
        Set<SelectionKey> selectedKeys = selector.selectedKeys();
        Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
        while (keyIterator.hasNext()) {
            final SelectionKey key = keyIterator.next();
            if (key.isReadable()) {
                SocketChannel client = (SocketChannel) key.channel();
                if (client == null) {
                    continue;
                }
                executeClientCommand(client);
            } else if (key.isAcceptable()) {
                accept(key);
            }

            keyIterator.remove();
        }
    }

    private void executeClientCommand(SocketChannel client) throws IOException {
        try {
            String clientMessage = getCommand(client).trim();
            String serverReply = startupCommandExecutor
                    .execute(clientMessage.replace(System.lineSeparator(), ""));
            sendMessage(serverReply, client);
        } catch (EmptyCommandException | InvalidCommandArgumentsException
                | InvalidCommandNameException | ClassNotFoundException e) {
            sendMessage(EXCEPTION_START_MSG + e.getMessage(), client);
        }
    }

    private String getCommand(SocketChannel client) throws IOException {
        commandByteBuffer.clear();
        client.read(commandByteBuffer);
        commandByteBuffer.flip();
        byte[] bytes = new byte[commandByteBuffer.limit()];
        commandByteBuffer.get(bytes);

        return new String(bytes);
    }

    private void sendMessage(String clientMessage, SocketChannel client) {
        String message = clientMessage + System.lineSeparator() + MSG_END + System.lineSeparator();
        ByteBuffer byteBuffer = ByteBuffer.allocate(message.getBytes().length);
        byteBuffer.put(message.getBytes());
        byteBuffer.flip();
        try {
            client.write(byteBuffer);
        } catch (IOException e) {
            try {
                client.close();
            } catch (IOException ex) {
                System.err.println(SENDING_REPLY_EXCEPTION_MSG + e.getMessage());
            }
        }
    }

    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
        SocketChannel accept = sockChannel.accept();
        accept.configureBlocking(false);
        accept.register(selector, SelectionKey.OP_READ);
    }

    public void stop() {
        this.isClosed = true;
        startupCommandExecutor.saveDatabase();
        if (selector.isOpen()) {
            selector.wakeup();
        }
    }

}