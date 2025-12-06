import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InputHandler implements Runnable {

    private final Client client;

    public InputHandler(Client client) {
        this.client = client;
    }

    /**
     * Main loop that reads input from the console.
     * <p>
     * - Displays a prompt with the current nickname.
     * - Updates the nickname locally if a /nick or /setname command is entered.
     * - Sends all input to the server.
     * - Handles /quit command to terminate the client session.
     */
    @Override public void run() {
        try (BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {

            while (!client.isDone()) {
                printPrompt();

                String message = console.readLine();
                if (message == null) {
                    client.shutdown();
                    break;
                }

                if (message.equals("/quit")) {
                    client.send(message);
                    client.shutdown();
                    break;
                }

                // Handle nickname change locally before sending
                if (message.startsWith("/nick ")) {
                    String[] parts = message.split(" ", 2);
                    if (parts.length == 2 && !parts[1].trim().isEmpty()) {
                        client.getUser().setNickname(parts[1].trim());
                    }
                } else if (message.startsWith("/setname ")) {
                    String[] parts = message.split(" ", 2);
                    if (parts.length == 2 && !parts[1].trim().isEmpty()) {
                        client.getUser().setNickname(parts[1].trim());
                    }
                }

                // Send everything to the server
                client.send(message);
            }

        } catch (IOException e) {
            System.out.println("Input error: " + e.getMessage());
        }
    }

    private void printPrompt() {
        System.out.print("\r\u001B[2K"); // clear line
        System.out.print("\u001B[36m" + client.getUser().getNickname() + "\u001B[0m> "); // prompt with current nickname
        System.out.flush();
    }
}
