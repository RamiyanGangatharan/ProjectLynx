package projectLynx.server.network;

import projectLynx.server.controller.ServerController;
import projectLynx.server.model.ChatUser;

import java.io.IOException;
import java.net.Socket;

public class ClientConnection implements Runnable {

    private final ServerController controller;
    private final ChatUser user;

    /**
     * Constructs a new ClientConnection for a connected client socket.
     * <p>
     * Initializes a {@link ChatUser} with a default nickname of "UNKNOWN"
     * and associates it with the provided server controller.
     *
     * @param controller the server controller that manages chat logic for this connection
     * @param socket     the socket connected to the client
     * @throws IOException if an I/O error occurs when creating the ChatUser's input/output streams
     */
    public ClientConnection(ServerController controller, Socket socket) throws IOException {
        this.controller = controller;
        this.user = new ChatUser(socket, "UNKNOWN");
    }

    @Override
    public void run() {
        try {
            // FIRST message sent by client = username from LoginScreen
            String username = user.getIn().readLine();

            if (username == null || username.trim().isEmpty()) {
                username = "Guest" + user.getSocket().getPort();
            }

            user.setNickname(username.trim());
            user.logConnected();
            controller.userConnected(user);

            // Main message loop
            String line;
            while ((line = user.getIn().readLine()) != null) {
                controller.handleMessage(user, line.trim());
            }

        } catch (IOException ignored) { }
        finally {
            try { controller.userDisconnected(user); } catch (Exception ignored) {}
            user.logDisconnected();
            user.close();
        }
    }
}
