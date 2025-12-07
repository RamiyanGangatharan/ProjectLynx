package projectLynx.client.view;

import javax.swing.*;
import java.awt.*;

public class LoginScreen {

    // Colors & Dimensions
    private static final Color DARK_BACKGROUND = new Color(55, 55, 55);

    private static final Color FIELD_BACKGROUND = new Color(40, 40, 40);
    private static final Color FIELD_FOREGROUND = Color.WHITE;

    private static final Color BUTTON_BACKGROUND = new Color(70, 130, 180);
    private static final Color BUTTON_FOREGROUND = Color.WHITE;

    private static final Dimension FIELD_SIZE = new Dimension(300, 30);
    private static final Dimension BUTTON_SIZE = new Dimension(300, 35);
    private static final Dimension WINDOW_RESOLUTION = new Dimension(500, 500);

    // This is what runs the page
    public LoginScreen() {
        JFrame loginFrame = new JFrame("Project Lynx - Login");
        loginFrame.setSize(WINDOW_RESOLUTION);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setResizable(false);
        loginFrame.setLocationRelativeTo(null);

        // Sound
        SoundPlayer player = new SoundPlayer();
        player.loadAndPlay("src/projectLynx/client/view/AppleSounds/Startup/StartupMacQuadra.wav");
        player.setVolume(0.8f);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(DARK_BACKGROUND);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        mainPanel.add(buildTitlePanel());
        mainPanel.add(Box.createVerticalStrut(30));
        mainPanel.add(buildInputPanel());
        mainPanel.add(Box.createVerticalStrut(30));
        mainPanel.add(buildButtonPanel());

        loginFrame.setContentPane(mainPanel);
        loginFrame.setVisible(true);
    }

    private JPanel buildTitlePanel() {
        JPanel panel = new JPanel();
        panel.setBackground(DARK_BACKGROUND);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(createLabel("PROJECT LYNX", 36, Color.WHITE));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createLabel("Developed and Designed by: Ramiyan Gangatharan", 12, Color.LIGHT_GRAY));

        return panel;
    }

    private JPanel buildInputPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(DARK_BACKGROUND);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(createLabel("Username", 16, Color.WHITE));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createTextField());

        panel.add(Box.createVerticalStrut(20));
        panel.add(createLabel("Password", 16, Color.WHITE));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createPasswordField());

        return panel;
    }

    private JPanel buildButtonPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(DARK_BACKGROUND);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(createButton("Login"));
        panel.add(Box.createVerticalStrut(15));
        panel.add(createButton("Register"));

        return panel;
    }

    // -------------------- Reusable Component Builders --------------------

    /**
     * Creates a centered JLabel with a specified text, font size, and color.
     *
     * @param text     The text to display on the label.
     * @param fontSize The font size of the label text.
     * @param color    The color of the label text.
     * @return A JLabel configured with the specified text, font size, color, and centered alignment.
     */
    private JLabel createLabel(String text, int fontSize, Color color) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Helvetica", Font.BOLD, fontSize));
        label.setForeground(color);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        return getjTextField(field);
    }

    /**
     * Configures a given JTextField with consistent styling for the login screen.
     *
     * This includes font, foreground and background colors, caret color, border,
     * maximum size, and center alignment.
     *
     * @param field The JTextField to style.
     * @return The same JTextField instance with the applied styling.
     */
    private JTextField getjTextField(JTextField field) {
        field.setFont(new Font("Helvetica", Font.PLAIN, 20));
        field.setForeground(FIELD_FOREGROUND);
        field.setBackground(FIELD_BACKGROUND);
        field.setCaretColor(FIELD_FOREGROUND);
        field.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        field.setMaximumSize(FIELD_SIZE);
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        return field;
    }

    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField();
        return (JPasswordField) getjTextField(field);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Helvetica", Font.BOLD, 20));
        button.setForeground(BUTTON_FOREGROUND);
        button.setBackground(BUTTON_BACKGROUND);
        button.setFocusPainted(false);
        button.setMaximumSize(BUTTON_SIZE);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        return button;
    }
}
