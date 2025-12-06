package projectLynx.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {

    private final List<ConnectionHandler> connections = new ArrayList<>();
    private ServerSocket serverSocket;
    private ExecutorService pool;
    private volatile boolean done = false;

    @Override public void run() {
        try {
            initializeServer();
            listenLoop();
        }
        catch (Exception ignored) {}
        finally { shutdown(); }
    }

    /**
     * Initializes the server by creating a ServerSocket on port 9999
     * and setting up a cached thread pool for handling client connections.
     *
     * @throws Exception if the server socket cannot be created or the thread pool fails to initialize
     */
    private void initializeServer() throws Exception {
        serverSocket = new ServerSocket(9999);
        pool = Executors.newCachedThreadPool();

        System.out.println("=== Project LYNX projectLynx.server.Server Started ===");
        System.out.println("Listening on port 9999...\n");
    }

    /**
     * Continuously listens for incoming client connections.
     * For each accepted connection, a new projectLynx.server.ConnectionHandler is created,
     * added to the list of active connections, and executed using the thread pool.
     *
     * @throws Exception if an error occurs while accepting client connections
     */
    private void listenLoop() throws Exception {
        while (!done) {
            try {
                Socket client = serverSocket.accept();
                ConnectionHandler handler = new ConnectionHandler(this, client);

                synchronized (connections) {
                    connections.add(handler);
                }

                pool.execute(handler);

            } catch (Exception e) {
                if (!done) {
                    System.out.println("projectLynx.server.Server error: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Sends a message to all currently connected clients.
     * Iterates over the list of active connections and calls `sendMessage`
     * on each projectLynx.server.ConnectionHandler in a thread-safe manner.
     *
     * @param message the message to be broadcasted to all clients
     */
    public void broadcast(String message) {
        synchronized (connections) {
            for (ConnectionHandler ch : connections) {
                ch.sendMessage(message);
            }
        }
    }

    /**
     * Removes a connection from the list of active connections.
     * This is typically called when a client disconnects to ensure
     * the server no longer attempts to send messages to it.
     *
     * @param handler the projectLynx.server.ConnectionHandler representing the client to remove
     */
    public void removeConnection(ConnectionHandler handler) {
        synchronized (connections) {
            connections.remove(handler);
        }
    }

    /**
     * Shuts down the server gracefully.
     * <p>
     * This method stops accepting new connections, shuts down the thread pool,
     * closes the server socket, and terminates all active client connections.
     * </p>
     */
    public void shutdown() {
        done = true;

        System.out.println("\n=== Shutting down server... ===");

        try {
            if (pool != null) { pool.shutdownNow(); }
            if (serverSocket != null && !serverSocket.isClosed()) { serverSocket.close(); }
            synchronized (connections) { for (ConnectionHandler ch : connections) { ch.shutdown(); } }
        }
        catch (Exception ignored) {}

        System.out.println("projectLynx.server.Server stopped.");
    }

    public static void main(String[] args) { new Thread(new Server()).start(); }
}
