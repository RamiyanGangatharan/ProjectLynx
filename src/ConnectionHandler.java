import java.io.IOException;
import java.net.Socket;
import java.time.LocalTime;

public class ConnectionHandler implements Runnable {

    private final Server server;
    private final User user;

    public ConnectionHandler(Server server, Socket clientSocket) {
        this.server = server;

        try {
            this.user = new User(clientSocket, "UNKNOWN");
        } catch (IOException e) {
            throw new RuntimeException("Unable to create User object", e);
        }
    }

    @Override public void run() {
        try {
            String message;
            while ((message = user.getIn().readLine()) != null) {
                handleIncoming(message.trim());
            }
        } catch (IOException ignored) {
        } finally {
            shutdown();
        }
    }

    // --- INITIALIZATION ---

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
