package projectLynx.server.controller;

import projectLynx.server.model.ChatRoom;
import projectLynx.server.model.ChatUser;

/**
 * The ServerController class manages chat room interactions on the server side.
 * It handles user connections, disconnections, nickname changes, and message broadcasting.
 */
public class ServerController {

    private final ChatRoom room = new ChatRoom();

    /**
     * Handles a new user connecting to the chat.
     * Adds the user to the chat room and broadcasts a join message to all connected users.
     *
     * @param user the ChatUser that connected
     */
    public void userConnected(ChatUser user) {
        room.addUser(user);
        room.broadcast("[+] " + user.getNickname() + " joined the chat");
    }

    /**
     * Handles a user disconnecting from the chat.
     * Removes the user from the chat room and broadcasts a leave message to all connected users.
     *
     * @param user the ChatUser that disconnected
     */
    public void userDisconnected(ChatUser user) {
        room.removeUser(user);
        room.broadcast("[-] " + user.getNickname() + " left the chat");
    }

    /**
     * Processes a message received from a user.
     * <p>
     * - Empty messages are ignored.
     * - Commands "/nick" or "/setname" trigger a nickname change.
     * - "/quit" triggers user disconnection.
     * - Otherwise, broadcasts the message to all users in the chat room.
     *
     * @param user    the ChatUser who sent the message
     * @param message the content of the message
     */
    public void handleMessage(ChatUser user, String message) {
        // empty message -> re-prompt (client handles prompt)
        if (message.isEmpty()) return;

        if (message.startsWith("/nick ") || message.startsWith("/setname ")) {
            handleNickChange(user, message);
            return;
        }

        if (message.equals("/quit")) {
            user.send("Goodbye!");
            userDisconnected(user);
            return;
        }

        room.broadcast(user.getNickname() + ": " + message);
    }

    /**
     * Handles a nickname change request from a user.
     * <p>
     * - If the nickname is missing or invalid, sends usage instructions.
     * - If the old nickname was "UNKNOWN", treats this as the first assignment and broadcasts join.
     * - Otherwise, broadcasts the nickname change and confirms to the user.
     *
     * @param user the ChatUser requesting the nickname change
     * @param raw  the raw command string (e.g., "/nick NewName")
     */
    private void handleNickChange(ChatUser user, String raw) {
        String[] split = raw.split(" ", 2);
        if (split.length < 2 || split[1].trim().isEmpty()) {
            user.send("Usage: /nick NEWNAME");
            return;
        }

        String newNick = split[1].trim();
        String oldNick = user.getNickname();
        user.setNickname(newNick);

        if ("UNKNOWN".equals(oldNick)) {
            // first time name assignment
            user.send("You are now known as: " + newNick);
            userConnected(user);
        }
        else {
            room.broadcast("[*] " + oldNick + " is now " + newNick);
            user.send("Nickname updated: " + newNick);
        }
    }

    // Boxing helper for broadcasting from server code
    public void broadcast(String message) {
        room.broadcast(message);
    }
}
