package projectLynx.server.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a chat room that maintains a list of connected users and
 * allows broadcasting messages to all users.
 */
public class ChatRoom {
    private final List<ChatUser> users = new ArrayList<>();

    /**
     * Adds a user to the chat room.
     *
     * @param user the {@link ChatUser} to add to the room
     */
    public synchronized void addUser(ChatUser user) {
        users.add(user);
    }

    /**
     * Removes a user from the chat room.
     *
     * @param user the {@link ChatUser} to remove from the room
     */
    public synchronized void removeUser(ChatUser user) {
        users.remove(user);
    }

    /**
     * Broadcasts a message to all users in the chat room.
     * <p>
     * Iterates over a copy of the user list to avoid
     * {@link java.util.ConcurrentModificationException} if users are added/removed
     * during broadcasting.
     *
     * @param message the message to send to all users
     */
    public synchronized void broadcast(String message) {
        for (ChatUser user : new ArrayList<>(users)) {
            try { user.send(message); }
            catch (Exception ignored) {}
        }
    }
}
