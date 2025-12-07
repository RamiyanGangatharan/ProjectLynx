package projectLynx.server.model;

/**
 * The Authentication class provides simple authentication logic for server users.
 * <p>
 * Currently, this is a placeholder implementation. In a real system, this class
 * would validate credentials against a database or other secure storage.
 */
public class Authentication {

    /**
     * Attempts to log in a user with the given username and password.
     * <p>
     * This implementation currently always returns {@code true} as a placeholder.
     *
     * @param username the username of the user attempting to log in
     * @param password the password of the user attempting to log in
     * @return {@code true} if the login is successful, {@code false} otherwise
     */
    public static boolean login(String username, String password) {
        // placeholder: always returns true for now (or implement real check)
        return true;
    }
}
