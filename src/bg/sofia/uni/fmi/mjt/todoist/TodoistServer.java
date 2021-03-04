package bg.sofia.uni.fmi.mjt.todoist;

import bg.sofia.uni.fmi.mjt.todoist.command.StartupCommand;
import bg.sofia.uni.fmi.mjt.todoist.exceptions.InvalidCommandArgumentsException;
import bg.sofia.uni.fmi.mjt.todoist.exceptions.NullCommandException;
import bg.sofia.uni.fmi.mjt.todoist.storage.user.UserStorage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class TodoistServer {
    private static final String SERVER_OPERATIONS_EXCEPTION_MSG =
            "A problem occurred while trying to execute operations from server.";
    private static final String CLIENT_REQUEST_PROCESSING_MSG = "A problem occurred while processing client request.";
    private static final String PROBLEMS_FILE_DIR = "database/Todoist-Problems-Database/problems.txt";

    private static final String SERVER_HOST = "localhost";
    private static final int BUFFER_SIZE = 1024;
    private final int serverPort;
    private boolean isClosed;

    private ByteBuffer commandByteBuffer;
    private Selector selector;

    private final StartupCommand startupCommandExecutor;

    public TodoistServer(int port) {
        this.serverPort = port;
        this.startupCommandExecutor = new StartupCommand(new UserStorage());
    }

    public void start() throws InvalidCommandArgumentsException, NullCommandException, IOException { // TODO
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            selector = Selector.open();
            this.configureServerSocketChannel(serverSocketChannel, selector);
            commandByteBuffer = ByteBuffer.allocate(BUFFER_SIZE);
            isClosed = false;
            while (!isClosed) {
                try {
                    int readyChannels = selector.select();
                    if (readyChannels == 0) {
                        continue;
                    }
                    Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                    while (keyIterator.hasNext()) {
                        SelectionKey key = keyIterator.next();
                        if (key.isReadable()) {
                            this.read(key);
                        } else if (key.isAcceptable()) {
                            this.accept(key, selector);
                        }
                        keyIterator.remove();
                    }
                } catch (IOException e) {
                    saveProblemToFile(e);
                    throw new UncheckedIOException(CLIENT_REQUEST_PROCESSING_MSG, e); // TODO
                }
            }
        } catch (IOException e) {
            saveProblemToFile(e);
            throw new UncheckedIOException(SERVER_OPERATIONS_EXCEPTION_MSG, e);
        }
    }

    private void saveProblemToFile(IOException e) throws IOException {
        try (Writer fileWriter = new FileWriter(PROBLEMS_FILE_DIR, true)) {
            e.printStackTrace(new PrintWriter(new BufferedWriter(fileWriter)));
        }
    }

    private void configureServerSocketChannel(ServerSocketChannel serverSocketChannel,
                                              Selector selector) throws IOException {
        serverSocketChannel.bind(new InetSocketAddress(SERVER_HOST, serverPort));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    private void accept(SelectionKey key, Selector selector) throws IOException {
        ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
        SocketChannel accept = sockChannel.accept();

        accept.configureBlocking(false);
        accept.register(selector, SelectionKey.OP_READ);
    }

    private void read(SelectionKey key) throws IOException, NullCommandException, InvalidCommandArgumentsException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        commandByteBuffer.clear();
        int readBytes = socketChannel.read(commandByteBuffer);
        if (readBytes < 0) {
            socketChannel.close();
            return;
        }
        this.executeBuffer();
        socketChannel.write(commandByteBuffer);
    }

    private void executeBuffer() throws InvalidCommandArgumentsException, NullCommandException {
        commandByteBuffer.flip();
        String clientMessage = StandardCharsets.UTF_8.decode(commandByteBuffer).toString();
        String serverReply = startupCommandExecutor.execute(clientMessage
                .replace(System.lineSeparator(), ""));
        System.out.println(serverReply);
        commandByteBuffer.clear();
        commandByteBuffer.put((serverReply + System.lineSeparator()).getBytes());
        commandByteBuffer.flip();
    }

    public void stop() {
        this.isClosed = true;
        startupCommandExecutor.saveDatabase();
        if (selector.isOpen()) {
            selector.wakeup();
        }
    }

    public static void main(String[] args) throws NullCommandException, InvalidCommandArgumentsException, IOException {
        TodoistServer todoistServer = new TodoistServer(7777);
        todoistServer.start();
    }
}