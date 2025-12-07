package projectLynx.server.network;

import projectLynx.server.controller.ServerController;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Main server class that listens for incoming client connections
 * and manages them using a thread pool.
 * <p>
 * Each accepted client connection is wrapped in a {@link ClientConnection}
 * and executed in a separate thread.
 */
public class ServerMain implements Runnable {

    private final int port;
    private volatile boolean done = false;

    /**
     * Constructs a new ServerMain instance listening on the specified port.
     *
     * @param port the TCP port on which the server will listen for client connections
     */
    public ServerMain(int port) { this.port = port; }

    /**
     * Starts the server.
     * <p>
     * Opens a {@link ServerSocket} on the specified port, initializes a
     * {@link ServerController}, and continuously listens for new client connections.
     * Each connection is handled in its own thread via a cached thread pool.
     */
    @Override public void run() {
        ExecutorService pool = Executors.newCachedThreadPool();
        ServerController controller = new ServerController();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("=== Project LYNX Server Started ===");
            System.out.println("Listening on port " + port + "...\n");

            while (!done) {
                Socket client = serverSocket.accept();
                try {
                    ClientConnection conn = new ClientConnection(controller, client);
                    pool.execute(conn);
                }
                catch (Exception e) {
                    System.out.println("Error creating client connection: " + e.getMessage());
                    try { client.close(); } catch (Exception ignored) {}
                }
            }
        }
        catch (Exception e) { System.out.println("Server error: " + e.getMessage()); }
        finally {
            pool.shutdownNow();
            System.out.println("Server stopped.");
        }
    }

    /**
     * Stops the server by setting the internal flag to true.
     * <p>
     * The server will stop accepting new connections and shutdown the thread pool gracefully.
     */
    public void stop() { done = true; }

    /**
     * Entry point for starting the server from the command line.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        new Thread(new ServerMain(9999)).start();
    }
}
