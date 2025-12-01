import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {

    private final ArrayList<ConnectionHandler> connections;
    private ServerSocket serverSocket;
    private ExecutorService pool;
    private volatile boolean done = false;

    public Server() { connections = new ArrayList<>(); }

    /**
     *
     */
    @Override public void run() {
        try {
            serverSocket = new ServerSocket(9999);
            pool = Executors.newCachedThreadPool();

            System.out.println("Server running on port 9999...");

            while (!done) {
                Socket client = serverSocket.accept();
                ConnectionHandler handler = new ConnectionHandler(this, client);
                synchronized (connections) { connections.add(handler); }
                pool.execute(handler);
            }
        }
        catch (Exception e) { shutdown(); }
    }

    /**
     *
     * @param message
     */
    public void broadcast(String message) {
        synchronized (connections) { for (ConnectionHandler ch : connections) { ch.sendMessage(message); } }
    }

    /**
     *
     * @param handler
     */
    public void removeConnection(ConnectionHandler handler) {
        synchronized (connections) { connections.remove(handler); }
    }

    /**
     *
     */
    public void shutdown() {
        done = true;

        try {
            if (pool != null) pool.shutdownNow();
            if (serverSocket != null && !serverSocket.isClosed()) serverSocket.close();
            synchronized (connections) { for (ConnectionHandler ch : connections) { ch.shutdown(); } }
        }
        catch (Exception ignored) {}
    }

    public static void main(String[] args) { new Thread(new Server()).start(); }
}
