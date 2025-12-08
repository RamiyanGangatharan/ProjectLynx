package projectLynx.server.controller;

import projectLynx.client.view.MainScreen;
import projectLynx.client.view.SoundPlayer;
import projectLynx.server.model.Authentication;
import projectLynx.server.model.ChatUser;

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;

public class AuthenticationController {

    private static Socket socket;

    public AuthenticationController() throws IOException {
        socket = new Socket("localhost", 9999);
    }

    public static void login(String username, String password) {
        boolean success = Authentication.login(username, password);
        if (success) {
            try {
                Socket socket = new Socket("localhost", 9999);
                ChatUser chatUser = new ChatUser(socket, username);
                chatUser.send(username);
                new MainScreen(chatUser);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Could not connect to server.");
                e.printStackTrace();
            }
        } else {
            SoundPlayer soundPlayer = new SoundPlayer();
            soundPlayer.loadAndPlay("D:\\Development\\ProjectLynx\\src\\projectLynx\\client\\view\\Sounds\\sonar-ping.wav");
            soundPlayer.setVolume(0.8f);
            JOptionPane.showMessageDialog(null, "Invalid credentials");
        }
    }

    public static void register(String username, String password) {
        boolean success = Authentication.register(username, password);
        JOptionPane.showMessageDialog(null, success ? "Account created!" : "Registration failed.");
    }
}
