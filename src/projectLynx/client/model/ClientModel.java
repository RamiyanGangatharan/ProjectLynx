package projectLynx.client.model;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * The {@code ClientModel} class represents the data and connection state
 * of a client in the chat application. It holds the client's nickname,
 * network socket, and input/output streams for communication with the server.
 * <p>
 * This class serves as the "Model" in the MVC pattern, storing client data
 * and providing access to communication channels.
 */
public class ClientModel {
    private String nickname;
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;


    /**
     * Constructs a new {@code ClientModel} with the given network resources and nickname.
     *
     * @param socket   the socket connected to the server
     * @param in       the input stream to receive messages from the server
     * @param out      the output stream to send messages to the server
     * @param nickname the initial nickname for the client
     */
    public ClientModel(Socket socket, BufferedReader in, PrintWriter out, String nickname) {
        this.socket = socket;
        this.in = in;
        this.out = out;
        this.nickname = nickname;
    }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public BufferedReader getIn() { return in; }
    public PrintWriter getOut() { return out; }
    public Socket getSocket() { return socket; }
}
