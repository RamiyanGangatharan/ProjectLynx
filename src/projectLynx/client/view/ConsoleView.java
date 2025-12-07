package projectLynx.client.view;

public class ConsoleView {
    public void print(String s) { System.out.println(s); }

    /**
     * Prints an incoming message to the console in a formatted manner.
     * <p>
     * This method clears the current input line, prints the message with a
     * timestamp in yellow, and then restores the user's prompt with their
     * nickname in cyan.
     *
     * @param msg  the message received from the server to be displayed
     * @param nick the nickname of the user, used to display the prompt
     */
    public void printIncoming(String msg, String nick) {
        // Clear line then print with timestamp and prompt
        System.out.print("\r\u001B[2K");
        String formatted = String.format("\u001B[33m[%tH:%tM]\u001B[0m %s", System.currentTimeMillis(), System.currentTimeMillis(), msg);
        System.out.println(formatted);
        System.out.print("\u001B[36m" + nick + "\u001B[0m> ");
        System.out.flush();
    }
}
