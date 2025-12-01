import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InputHandler implements Runnable {

    private final Client client;
    public InputHandler(Client client) { this.client = client; }

    /**
     *
     */
    @Override
    public void run() {
        try {
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

            while (!client.done) {
                System.out.print("LYNX:// ");
                System.out.flush();

                String message = console.readLine();
                if (message == null) break;

                if (message.equals("/quit")) {
                    client.out.println(message);
                    client.shutdown();
                    break;
                }

                client.out.println(message);
            }
        }
        catch (IOException e) {
            System.out.println("Input error: " + e.getMessage());
        }
    }

}