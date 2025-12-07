package projectLynx.client;

import projectLynx.client.controller.ClientController;
import projectLynx.client.model.ClientModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Console input handler that forwards user input to the ClientController.
 */
public class InputHandler implements Runnable {

    private final ClientController controller;
    private final ClientModel model;

    /**
     * Constructs a new InputHandler for handling user input in the client application.
     *
     * @param controller the ClientController responsible for processing and sending messages
     * @param model      the ClientModel containing the user's socket, streams, and nickname
     */
    public InputHandler(ClientController controller, ClientModel model) {
        this.controller = controller;
        this.model = model;
    }

    @Override public void run() {
        try (BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {
            while (!model.getSocket().isClosed()) {
                System.out.print("\u001B[36m" + model.getNickname() + "\u001B[0m> ");
                System.out.flush();

                String message = console.readLine();
                if (message == null) break;
                if (message.equals("/quit")) {
                    controller.sendMessage(message);
                    break;
                }
                if (message.startsWith("/nick ") || message.startsWith("/setname ")) {
                    String[] parts = message.split(" ", 2);
                    if (parts.length == 2 && !parts[1].trim().isEmpty()) { controller.setNickname(parts[1].trim()); }
                }
                controller.sendMessage(message);
            }
        }
        catch (IOException e) { System.out.println("Input error: " + e.getMessage()); }
        finally {
            try { model.getSocket().close(); }
            catch (Exception ignored) {}
        }
    }
}