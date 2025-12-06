import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Runnable {

    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private volatile boolean done = false;

    private User user;

    @Override public void run() {
        try {
            String host = "127.0.0.1";
            int port = 9999;
            connect(host, port);
            askForNickname();
            startInputHandler();
            listenForMessages();
        }
        catch (Exception e) { shutdown(); }
    }

    /* -----------------------------------------
       Connection setup
    ------------------------------------------ */

    private void connect(String host, int port) throws Exception {
        client = new Socket(host, port);
        out = new PrintWriter(client.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
    }

    private void startInputHandler() {
        Thread inputThread = new Thread(new InputHandler(this));
        inputThread.start();
    }

    /* -----------------------------------------
       Prompt for nickname
    ------------------------------------------ */

    private void askForNickname() throws Exception {
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter your nickname: ");
        String nick = console.readLine();

        if (nick == null || nick.trim().isEmpty()) {
            nick = "Guest" + (int) (Math.random() * 1000);
        }

        user = new User(client, nick.trim());
        send("/setname " + user.getNickname()); // send to server so nickname is registered
    }

    /* -----------------------------------------
       Main loop: read messages from server
    ------------------------------------------ */

    private void listenForMessages() throws Exception {
        String msg;
        while (!done && (msg = user.getIn().readLine()) != null) {
            printIncoming(msg);
        }
    }

    private void printIncoming(String msg) {
        // Clear current input line
        System.out.print("\r\u001B[2K");

        // Format incoming message nicely
        String formatted = String.format(
                "\u001B[33m[%tH:%tM]\u001B[0m %s",
                System.currentTimeMillis(),
                System.currentTimeMillis(),
                msg
        );

        System.out.println(formatted);

        // Restore user prompt using the User's nickname
        System.out.print("\u001B[36m" + user.getNickname() + "\u001B[0m> ");
        System.out.flush();
    }

    /* -----------------------------------------
       Accessors & Helpers
    ------------------------------------------ */

    public boolean isDone() {
        return done;
    }

    public void send(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    public User getUser() {
        return user;
    }

    public PrintWriter getOut() {
        return out;
    }

    /* -----------------------------------------
       Shutdown logic
    ------------------------------------------ */

    public void shutdown() {
        done = true;

        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (client != null && !client.isClosed()) client.close();
        } catch (Exception ignored) {}
    }

    public static void main(String[] args) {
        new Client().run();
    }
}
