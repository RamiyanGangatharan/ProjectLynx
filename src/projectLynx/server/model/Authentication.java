package projectLynx.server.model;

import projectLynx.server.controller.CSVHandler;

import java.util.List;

public class Authentication {

    private static final CSVHandler handler = new CSVHandler();

    public static boolean login(String username, String password) {
        List<List<String>> users = handler.readCSV();

        for (List<String> row : users) {
            if (row.size() == 2 && row.get(0).equals(username) && row.get(1).equals(password)) { return true; }
        }
        return false;
    }

    public static boolean register(String username, String password) {
        for (List<String> row : handler.readCSV()) { if (row.get(0).equals(username)) return false; }
        handler.appendRow(username, password);
        return true;
    }
}
