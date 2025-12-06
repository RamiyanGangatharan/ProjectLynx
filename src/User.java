import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class User {

    private String nickname;
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;

    /**
     * Constructs a new User object with the specified socket and default nickname.
     * <p>
     * Initializes input and output streams for communication over the given socket.
     *
     * @param socket      the socket connected to the client
     * @param defaultNick the default nickname to assign to the user
     * @throws IOException if an I/O error occurs when creating input or output streams
     */
    public User(Socket socket, String defaultNick) throws IOException {
        this.socket = socket;
        this.nickname = defaultNick;

        this.in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    // Getters
    public String getNickname() { return nickname; }
    public BufferedReader getIn() { return in; }
    public PrintWriter getOut() { return out; }
    public Socket getSocket() { return socket; }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Sends a message to this user over the associated socket.
     *
     * @param msg the message to send
     */
    public void send(String msg) {
        out.println(msg);
        out.flush();
    }
}
