import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalTime;

public class ConnectionHandler implements Runnable {

    private final Socket client;
    private final Server server;
    private BufferedReader in;
    private PrintWriter out;
    private String nickname = "UNKNOWN";


    public ConnectionHandler(Server server, Socket client) {
        this.server = server;
        this.client = client;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(client.getOutputStream(), true);
            in  = new BufferedReader(new InputStreamReader(client.getInputStream()));

            out.println("Welcome to Project LYNX!");
            out.println("Enter your nickname:");

            String nameInput = in.readLine();
            if (nameInput == null || nameInput.trim().isEmpty()) { nickname = "Guest" + client.getPort(); }
            else { nickname = nameInput.trim(); }

            System.out.println("[" + LocalTime.now() + "] " + nickname + " connected.");
            out.println("You have joined the chat as " + nickname + "!");
            server.broadcast(nickname + " has joined the chat!");

            String message;

            while ((message = in.readLine()) != null) {
                if (message.trim().isEmpty()) continue;
                if (message.startsWith("/nick ")) { handleNickChange(message); }
                else if (message.equals("/quit")) { server.broadcast(nickname + " has left the chat!"); break; }
                else { server.broadcast(nickname + ": " + message); }
            }
        }
        catch (IOException ignored) {}
        finally { shutdown(); }
    }

    private void handleNickChange(String raw) {
        String[] split = raw.split(" ", 2);
        if (split.length < 2) { out.println("Usage: /nick NEW_NAME"); return; }
        String newNick = split[1].trim();
        if (newNick.isEmpty()) { out.println("Invalid nickname."); return; }
        server.broadcast(nickname + " changed their name to " + newNick);
        nickname = newNick;
        out.println("Your nickname is now " + nickname);
    }

    public void sendMessage(String message) { if (out != null) out.println(message); }

    public void shutdown() {
        try {
            System.out.println("[" + LocalTime.now() + "] " + nickname + " disconnected.");
            server.removeConnection(this);

            if (in != null) in.close();
            if (out != null) out.close();
            if (client != null && !client.isClosed()) client.close();

        }
        catch (Exception ignored) {}
    }
}