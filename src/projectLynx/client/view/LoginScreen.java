package projectLynx.client.view;

import javax.swing.*;

public class LoginScreen {
    public LoginScreen() {
        JFrame frame = new JFrame("Login Screen");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        SoundPlayer player = new SoundPlayer();
        player.loadAndPlay("src/projectLynx/client/view/AppleSounds/Startup/StartupMacQuadraAV.wav");
        player.setVolume(0.8f); // Set volume to 80%


        frame.setVisible(true);
    }
}
