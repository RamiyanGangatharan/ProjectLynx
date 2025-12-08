package projectLynx.client.view;

import javax.swing.*;
import java.awt.*;

public class MainScreenRIGHT extends JPanel {

    public MainScreenRIGHT() {
        setBackground(Color.DARK_GRAY);

        JLabel chatLabel = new JLabel("CHATTER");
        chatLabel.setForeground(Color.WHITE);
        add(chatLabel);
    }
}
