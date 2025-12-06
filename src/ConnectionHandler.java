import java.io.IOException;
import java.net.Socket;
import java.time.LocalTime;

public class ConnectionHandler implements Runnable {

    private final Server server;
    private final User user;

    /**
     * Constructs a new {@code ConnectionHandler} for a client socket.
     * <p>
     * Initializes a {@link User} object associated with the given client socket.
     * If the {@link User} cannot be created due to an {@link IOException}, a
     * {@link RuntimeException} is thrown.
     *
     * @param server the server instance that manages this connection
     * @param clientSocket the socket connected to the client
     */
    public ConnectionHandler(Server server, Socket clientSocket) {
        this.server = server;
        try { this.user = new User(clientSocket, "UNKNOWN"); }
        catch (IOException e) { throw new RuntimeException("Unable to create User object", e); }
    }

    @Override public void run() {
        try {
            String message;
            while ((message = user.getIn().readLine()) != null) { handleIncoming(message.trim()); }
        }
        catch (IOException ignored) {}
        finally {shutdown();}
    }

    // --- INITIALIZATION ---

    /**
     * Prompts the connected user to enter a nickname.
     * <p>
     * Sends a welcome message and asks the user to input their desired nickname.
     * If the input is empty or null, a default nickname is generated based on
     * the user's socket port. The nickname is then set in the {@link User} object.
     * Finally, a confirmation message is sent back to the user.
     *
     * @throws IOException if an I/O error occurs while reading from or writing to the user's socket
     */
    private void requestNickname() throws IOException {
        user.send("=== Welcome to Project LYNX ===");
        user.send("Enter a nickname:");

        String nameInput = user.getIn().readLine();

        if (nameInput == null || nameInput.trim().isEmpty()) {
            user.setNickname("Guest" + user.getSocket().getPort());
        } else {
            user.setNickname(nameInput.trim());
        }

        // Confirm to the user only, no broadcast
        user.send("You are now known as: " + user.getNickname());
    }

    private void broadcastJoin() {
        // Server log
        System.out.println("[" + LocalTime.now() + "] " + user.getNickname() + " connected.");

        // Broadcast to all other users
        server.broadcast("[+] " + user.getNickname() + " joined the chat.");
    }

    // --- MESSAGE HANDLING ---

    /**
     * Processes an incoming message from the connected user.
     * <p>
     * - If the message is empty, simply re-displays the prompt.
     * - If the message starts with "/setname" or "/nick", it triggers a nickname change.
     * - If the message is "/quit", broadcasts that the user has left the chat.
     * - Otherwise, broadcasts the message to all connected users with the user's current nickname.
     *
     * @param message the message received from the user
     */
    private void handleIncoming(String message) {
        if (message.isEmpty()) {
            prompt();
            return;
        }

        if (message.startsWith("/setname ") || message.startsWith("/nick ")) {
            handleNickChange(message);
            return;
        }

        if (message.equals("/quit")) {
            server.broadcast("[-] " + user.getNickname() + " left the chat.");
            return;
        }

        server.broadcast(user.getNickname() + ": " + message);
        prompt();
    }

    /**
     * Handles a nickname change request from the user.
     * <p>
     * - If the input is invalid or missing a new nickname, sends usage instructions.
     * - If this is the first time the user sets a nickname (previously "UNKNOWN"), broadcasts
     *   that the user has joined the chat.
     * - Otherwise, broadcasts that the user has changed their nickname.
     * - Confirms the nickname update to the user and re-displays the prompt.
     *
     * @param raw the raw command string received from the user (e.g., "/nick NewName")
     */
    private void handleNickChange(String raw) {
        String[] split = raw.split(" ", 2);

        if (split.length < 2 || split[1].trim().isEmpty()) {
            user.send("Usage: /nick NEWNAME");
            prompt();
            return;
        }

        String newNick = split[1].trim();
        String oldNick = user.getNickname();

        // Assign nickname before broadcasting
        user.setNickname(newNick);

        if (oldNick.equals("UNKNOWN")) {
            // First-time nickname assignment → broadcast join
            server.broadcast("[+] " + newNick + " joined the chat");
            user.send("You are now known as: " + newNick);
        } else {
            // Subsequent nickname changes
            server.broadcast("[*] " + oldNick + " is now " + newNick);
            user.send("Nickname updated: " + newNick);
        }

        prompt();
    }

    public void sendMessage(String message) {
        user.send(message);
    }

    private void prompt() {
        String nick = user.getNickname();
        user.getOut().print("\r\u001B[36m" + nick + "\u001B[0m> ");
        user.getOut().flush();
    }

    // --- SHUTDOWN ---

    /**
     * Shuts down the connection for this user.
     * <p>
     * - Logs the disconnection to the server console.
     * - Removes this connection handler from the server's active connections.
     * - Closes the user's input and output streams, as well as the socket.
     * - Any {@link IOException} encountered during closing is ignored.
     */
    public void shutdown() {
        try {
            System.out.println("[" + LocalTime.now() + "] " + user.getNickname() + " disconnected.");
            server.removeConnection(this);

            user.getIn().close();
            user.getOut().close();
            user.getSocket().close();

        } catch (IOException ignored) {}
    }
}
