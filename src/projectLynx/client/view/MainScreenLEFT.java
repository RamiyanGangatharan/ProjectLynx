package projectLynx.client.view;

import javax.swing.*;
import java.awt.*;

public class MainScreenLEFT extends JPanel {

    public MainScreenLEFT() {
        setBackground(Color.DARK_GRAY);

        JLabel utilLabel = new JLabel("CHAT");
        utilLabel.setForeground(Color.WHITE);
        add(utilLabel);
    }
}
