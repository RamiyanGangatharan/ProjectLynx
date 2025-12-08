package projectLynx.client.view;

import projectLynx.server.model.ChatUser;

import javax.swing.*;
import java.awt.*;

public class MainScreen {

    private static final Dimension WINDOW_RESOLUTION = new Dimension(1280,720);

    public MainScreen(ChatUser chatUser) {
        JFrame frame = new JFrame("ProjectLynx - Main");
        frame.setSize(WINDOW_RESOLUTION);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        SoundPlayer sound = new SoundPlayer();
        sound.loadAndPlay("D:\\Development\\ProjectLynx\\src\\projectLynx\\client\\view\\Sounds\\Retro8.wav");

        MainScreenLEFT leftPanel = new MainScreenLEFT();
        MainScreenRIGHT rightPanel = new MainScreenRIGHT();

        // Split pane: left = utilPanel, right = chatPanel
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(300); // initial width of left panel
        splitPane.setDividerSize(2); // thin divider
        splitPane.setEnabled(false); // non-draggable if you want
        splitPane.setBorder(null);

        frame.add(splitPane);
        frame.setVisible(true);
    }

}
