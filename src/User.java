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

    // Set nickname
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    // Send a message to the user
    public void send(String msg) {
        out.println(msg);
        out.flush();
    }
}
