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

    private void initializeServer() throws Exception {
        serverSocket = new ServerSocket(9999);
        pool = Executors.newCachedThreadPool();

        System.out.println("=== Project LYNX Server Started ===");
        System.out.println("Listening on port 9999...\n");
    }

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
                    System.out.println("Server error: " + e.getMessage());
                }
            }
        }
    }

    public void broadcast(String message) {
        synchronized (connections) {
            for (ConnectionHandler ch : connections) {
                ch.sendMessage(message);
            }
        }
    }

    public void removeConnection(ConnectionHandler handler) {
        synchronized (connections) {
            connections.remove(handler);
        }
    }

    public void shutdown() {
        done = true;

        System.out.println("\n=== Shutting down server... ===");

        try {
            if (pool != null) { pool.shutdownNow(); }
            if (serverSocket != null && !serverSocket.isClosed()) { serverSocket.close(); }
            synchronized (connections) { for (ConnectionHandler ch : connections) { ch.shutdown(); } }
        }
        catch (Exception ignored) {}

        System.out.println("Server stopped.");
    }

    public static void main(String[] args) { new Thread(new Server()).start(); }
}
