package projectLynx.client.view;

import projectLynx.server.model.ChatUser;

import javax.swing.*;
import java.awt.*;

public class MainScreen {

    private static final Dimension WINDOW_RESOLUTION = new Dimension(1280,720);

    public MainScreen(ChatUser chatUser) {
        JFrame frame = new JFrame();
        frame.setSize(WINDOW_RESOLUTION);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        SoundPlayer sound = new SoundPlayer();
        sound.loadAndPlay("D:\\Development\\ProjectLynx\\src\\projectLynx\\client\\view\\Sounds\\Retro8.wav");

        frame.setVisible(true);
    }
}
