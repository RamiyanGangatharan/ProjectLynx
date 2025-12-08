package projectLynx.client.network;

import projectLynx.client.controller.ClientController;
import projectLynx.client.model.ClientModel;
import projectLynx.client.view.ConsoleView;
import projectLynx.client.view.LoginScreen;
import projectLynx.server.controller.CSVHandler;
import projectLynx.server.network.ServerMain;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientApp implements Runnable {

    private final String host;
    private final int port;
    private final ConsoleView view = new ConsoleView();

    public ClientApp(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) {
        new Thread(new ClientApp("127.0.0.1", 9999)).start();
        new LoginScreen();
        CSVHandler handler = new CSVHandler();
        System.out.println(handler.readCSV());
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(host, port);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // ask for nickname on console (mirrors previous behavior)
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter your nickname: ");
            String nick = console.readLine();
            if (nick == null || nick.trim().isEmpty()) nick = "Guest" + (int) (Math.random() * 1000);

            // create model & controller
            ClientModel model = new ClientModel(socket, in, out, nick);
            ClientController controller = new ClientController(model, view);

            // register nickname with server
            controller.sendMessage(nick);

            // start input thread
            Thread inputThread = new Thread(new projectLynx.client.InputHandler(controller, model));
            inputThread.start();

            // read server messages
            String msg;
            while ((msg = in.readLine()) != null) {
                controller.handleServerMessage(msg);
            }
        } catch (Exception e) {
            System.out.println("Client error: " + e.getMessage());
            System.out.println("Make sure your server is running before running clients");
        }
    }
}
