package projectLynx.server.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalTime;

/**
 * Represents a user connected to the chat server.
 * <p>
 * Maintains the user's socket, nickname, input/output streams,
 * and provides methods to send messages, close connections,
 * and log connection events.
 */
public class ChatUser {
    private String nickname;
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;

    /**
     * Constructs a new ChatUser with the given socket and default nickname.
     *
     * @param socket      the socket connected to the client
     * @param defaultNick the initial nickname for the user
     * @throws IOException if an I/O error occurs when creating input/output streams
     */
    public ChatUser(Socket socket, String defaultNick) throws IOException {
        this.socket = socket;
        this.nickname = defaultNick;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public BufferedReader getIn() { return in; }
    public PrintWriter getOut() { return out; }
    public Socket getSocket() { return socket; }

    /**
     * Sends a message to this user.
     *
     * @param msg the message to send
     */
    public void send(String msg) {
        out.println(msg);
        out.flush();
    }

    /**
     * Closes the user's connection, including input/output streams and socket.
     * Any exceptions during closing are ignored.
     */
    public void close() {
        try { in.close(); }
        catch (IOException ignored) {}
        out.close();
        try { if (!socket.isClosed()) socket.close(); }
        catch (IOException ignored) {}
    }

    public void logConnected() {
        System.out.println("[" + LocalTime.now() + "] " + nickname + " connected.");
    }
    public void logDisconnected() {
        System.out.println("[" + LocalTime.now() + "] " + nickname + " disconnected.");
    }
}
