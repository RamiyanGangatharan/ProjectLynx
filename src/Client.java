import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Runnable {

    private Socket client;
    BufferedReader in;
    PrintWriter out;
    volatile boolean done = false;

    /**
     *
     */
    @Override public void run() {
        try {
            String msg;
            client = new Socket("127.0.0.1", 9999);
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            Thread inputThread = new Thread(new InputHandler(this));
            inputThread.start();

            // read messages from server
            while (!done && (msg = in.readLine()) != null) {
                System.out.println(msg);
                System.out.print("USER :// ");
                System.out.flush();
            }
        }
        catch (Exception e) { shutdown();}
    }

    /**
     *
     */
    public void shutdown() {
        done = true;

        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (client != null && !client.isClosed()) client.close();
        }
        catch (Exception ignored) {}
    }

    public static void main(String[] args) { new Client().run(); }
}
