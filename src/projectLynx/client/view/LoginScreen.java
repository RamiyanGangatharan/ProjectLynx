package projectLynx.client.view;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import projectLynx.server.controller.AuthenticationController;

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

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginScreen() {

        JFrame frame = new JFrame("Project Lynx - Login");
        frame.setSize(WINDOW_RESOLUTION);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        SoundPlayer soundPlayer = new SoundPlayer();
        soundPlayer.loadAndPlay("D:\\Development\\ProjectLynx\\src\\projectLynx\\client\\view\\Sounds\\StartupMacQuadra.wav");
        soundPlayer.setVolume(0.8f);

        // MAIN PANEL
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(DARK_BACKGROUND);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // ---------- TITLE ----------
        JLabel title = new JLabel("PROJECT LYNX", SwingConstants.CENTER);
        title.setFont(new Font("Helvetica", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Developed and Designed by: Ramiyan Gangatharan", SwingConstants.CENTER);
        subtitle.setFont(new Font("Helvetica", Font.PLAIN, 12));
        subtitle.setForeground(Color.LIGHT_GRAY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(title);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(subtitle);
        mainPanel.add(Box.createVerticalStrut(40));

        // ---------- USERNAME ----------
        JLabel userLabel = new JLabel("Username", SwingConstants.CENTER);
        userLabel.setFont(new Font("Helvetica", Font.BOLD, 16));
        userLabel.setForeground(Color.WHITE);
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        usernameField = new JTextField();
        styleTextField(usernameField);

        mainPanel.add(userLabel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(usernameField);
        mainPanel.add(Box.createVerticalStrut(20));

        // ---------- PASSWORD ----------
        JLabel passLabel = new JLabel("Password", SwingConstants.CENTER);
        passLabel.setFont(new Font("Helvetica", Font.BOLD, 16));
        passLabel.setForeground(Color.WHITE);
        passLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        passwordField = new JPasswordField();
        styleTextField(passwordField);

        mainPanel.add(passLabel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(passwordField);
        mainPanel.add(Box.createVerticalStrut(30));

        // ---------- BUTTONS ----------
        JButton loginButton = new JButton("Login");
        styleButton(loginButton);

        loginButton.addActionListener(e -> {
            try {
                handleLogin();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        JButton registerButton = new JButton("Register");
        styleButton(registerButton);

        registerButton.addActionListener(e -> handleRegister());

        mainPanel.add(loginButton);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(registerButton);

        // Set and show
        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("Helvetica", Font.PLAIN, 20));
        field.setForeground(FIELD_FOREGROUND);
        field.setBackground(FIELD_BACKGROUND);
        field.setCaretColor(FIELD_FOREGROUND);
        field.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        field.setMaximumSize(FIELD_SIZE);
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Helvetica", Font.BOLD, 20));
        button.setForeground(BUTTON_FOREGROUND);
        button.setBackground(BUTTON_BACKGROUND);
        button.setFocusPainted(false);
        button.setMaximumSize(BUTTON_SIZE);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private void handleLogin() throws IOException {
        AuthenticationController.login(usernameField.getText(), new String(passwordField.getPassword()));
    }

    private void handleRegister() {
        AuthenticationController.register(usernameField.getText(), new String(passwordField.getPassword()));
    }
}
