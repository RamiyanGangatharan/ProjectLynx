package projectLynx.client.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginScreen {
    private static final int FRAME_WIDTH = 400;
    private static final int FRAME_HEIGHT = 300;
    private static final int FIELD_WIDTH = 200; // fits inside frame nicely
    private static final int TOP_MARGIN = 10;

    private JFrame frame;
    private JPanel mainPanel;

    public LoginScreen() {
        setupWindow();
        setupMainPanel();
        addContent();
        frame.setVisible(true);
    }

    private void setupWindow() {
        frame = new JFrame("Project Lynx");
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void setupMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new BorderLayout());
        frame.add(mainPanel);
    }

    private void addContent() {
        // Vertical container for everything
        JPanel verticalPanel = new JPanel();
        verticalPanel.setBackground(Color.WHITE);
        verticalPanel.setLayout(new BoxLayout(verticalPanel, BoxLayout.Y_AXIS));
        verticalPanel.setBorder(BorderFactory.createEmptyBorder(TOP_MARGIN, 50, 50, 50)); // top, left, bottom, right

        // Title
        verticalPanel.add(createLabel("Project Lynx", 36));
        verticalPanel.add(Box.createVerticalStrut(20));

        // Username field
        verticalPanel.add(createFieldPanel("Username"));
        verticalPanel.add(Box.createVerticalStrut(10));

        // Password field
        verticalPanel.add(createFieldPanel("Password"));
        verticalPanel.add(Box.createVerticalStrut(40));

        // Submit button
        JButton submitButton = new JButton("Submit");
        submitButton.setBackground(Color.GRAY);
        submitButton.setForeground(Color.BLACK);
        submitButton.setFont(new Font("Helvetica", Font.BOLD, 16));
        submitButton.setMaximumSize(new Dimension(150, 40));
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.setFocusPainted(false);

        submitButton.addActionListener(new ActionListener() {

            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override public void actionPerformed(ActionEvent e) {

            }
        });

        verticalPanel.add(submitButton);

        mainPanel.add(verticalPanel, BorderLayout.CENTER);
    }

    private JPanel createFieldPanel(String labelText) {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel(labelText);
        label.setForeground(Color.BLACK);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);

        panel.add(Box.createVerticalStrut(5));

        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(FIELD_WIDTH, field.getPreferredSize().height));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(field);

        return panel;
    }

    private JLabel createLabel(String text, int fontSize) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setForeground(Color.BLACK);
        label.setFont(new Font("Helvetica", Font.BOLD, fontSize));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }
}
