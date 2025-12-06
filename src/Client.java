import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Runnable {

    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private volatile boolean done = false;

    private User user;

    @Override public void run() {
        try {
            String host = "127.0.0.1";
            int port = 9999;
            connect(host, port);
            askForNickname();
            startInputHandler();
            listenForMessages();
        }
        catch (Exception e) { shutdown(); }
    }

    /**
     * Establishes a connection to a server at the specified host and port.
     * Initializes the input and output streams for communication.
     *
     * @param host the hostname or IP address of the server to connect to
     * @param port the port number on the server to connect to
     * @throws Exception if an I/O error occurs when creating the socket or streams
     */
    private void connect(String host, int port) throws Exception {
        client = new Socket(host, port);
        out = new PrintWriter(client.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
    }

    private void startInputHandler() {
        Thread inputThread = new Thread(new InputHandler(this));
        inputThread.start();
    }

    /**
     * Prompts the user to enter a nickname via the console.
     * If the user does not enter a valid nickname, a random guest nickname is generated.
     * Creates a new {@link User} object with the chosen nickname and registers it with the server.
     *
     * @throws Exception if an I/O error occurs while reading input from the console
     */
    private void askForNickname() throws Exception {
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter your nickname: ");
        String nick = console.readLine();

        if (nick == null || nick.trim().isEmpty()) {
            nick = "Guest" + (int) (Math.random() * 1000);
        }

        user = new User(client, nick.trim());
        send("/setname " + user.getNickname()); // send to server so nickname is registered
    }

    /**
     * Continuously listens for incoming messages from the server.
     * Reads messages from the {@link User}'s input stream and
     * passes each message to {@link #printIncoming(String)} for display.
     * The loop continues until the client is marked as done or the
     * input stream is closed.
     *
     * @throws Exception if an I/O error occurs while reading messages
     */
    private void listenForMessages() throws Exception {
        String msg;
        while (!done && (msg = user.getIn().readLine()) != null) {
            printIncoming(msg);
        }
    }

    /**
     * Prints an incoming message to the console in a formatted manner.
     * <p>
     * The method clears the current input line, displays the message
     * with a timestamp in yellow, and then restores the user's prompt
     * with their nickname in cyan.
     *
     * @param msg the message received from the server to be displayed
     */
    private void printIncoming(String msg) {
        // Clear current input line
        System.out.print("\r\u001B[2K");

        // Format incoming message nicely
        String formatted = String.format(
                "\u001B[33m[%tH:%tM]\u001B[0m %s",
                System.currentTimeMillis(),
                System.currentTimeMillis(),
                msg
        );

        System.out.println(formatted);

        // Restore user prompt using the User's nickname
        System.out.print("\u001B[36m" + user.getNickname() + "\u001B[0m> ");
        System.out.flush();
    }


    /**
     * Shuts down the client by closing all I/O streams and the socket.
     * <p>
     * Sets the {@code done} flag to {@code true} to stop any ongoing loops,
     * and safely closes the input stream, output stream, and the client socket.
     * Any exceptions encountered during closing are caught and ignored.
     */
    public void shutdown() {
        done = true;

        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (client != null && !client.isClosed()) client.close();
        } catch (Exception ignored) {}
    }

    public boolean isDone() {
        return done;
    }

    public void send(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    public User getUser() {
        return user;
    }

    public PrintWriter getOut() {
        return out;
    }


    public static void main(String[] args) {
        new Client().run();
    }
}
