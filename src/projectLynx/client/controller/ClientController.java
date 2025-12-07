package projectLynx.client.controller;

import projectLynx.client.model.ClientModel;
import projectLynx.client.view.ConsoleView;

/**
 * The {@code ClientController} class acts as the controller in the MVC pattern
 * for the client-side chat application. It mediates between the {@link ClientModel}
 * (data and connection) and the {@link ConsoleView} (user interface), handling
 * user commands, sending messages to the server, and updating the view with
 * incoming messages.
 */
public class ClientController {

    private final ClientModel model;
    private final ConsoleView view;


    /**
     * Constructs a new {@code ClientController} with the given model and view.
     *
     * @param model the {@link ClientModel} representing the client's connection and state
     * @param view  the {@link ConsoleView} used to display messages and prompts
     */
    public ClientController(ClientModel model, ConsoleView view) {
        this.model = model;
        this.view = view;
    }

    /**
     * Handles an incoming message from the server.
     * <p>
     * The message is forwarded to the view for display, and the client's
     * current nickname is used to format the prompt.
     *
     * @param msg the message received from the server
     */
    public void handleServerMessage(String msg) { view.printIncoming(msg, model.getNickname()); }

    /**
     * Sends a message from the client to the server.
     * <p>
     * If the client's output stream is not available, this method does nothing.
     *
     * @param message the message to send to the server
     */
    public void sendMessage(String message) { if (model.getOut() != null) { model.getOut().println(message); }}

    /**
     * Updates the client's nickname locally.
     * <p>
     * This does not automatically notify the server; use {@link #sendMessage(String)}
     * with a "/nick" or "/setname" command to update the server-side nickname.
     *
     * @param newNick the new nickname to set for this client
     */
    public void setNickname(String newNick) { model.setNickname(newNick); }
}
