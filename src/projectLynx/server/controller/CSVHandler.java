package projectLynx.server.controller;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVHandler {

    private final String filepath = "D:\\Development\\ProjectLynx\\src\\projectLynx\\server\\users.csv";
    public static List<List<String>> records = new ArrayList<>();

    public List<List<String>> readCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = br.readLine()) != null) {
                records.add(Arrays.asList(line.split(",")));
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV: " + e.getMessage());
        }

        return records;
    }

    public void writeHeaderIfMissing() {
        File file = new File(filepath);

        if (!file.exists() || file.length() == 0) {
            try (FileWriter fw = new FileWriter(filepath)) {
                fw.write("username,password" + System.lineSeparator());
            } catch (IOException e) {
                System.out.println("Error writing header.");
            }
        }
    }

    public void appendRow(String username, String password) {
        try (FileWriter fw = new FileWriter(filepath, true)) {
            fw.write(username + "," + password + System.lineSeparator());
        } catch (IOException e) {
            System.out.println("Error writing row.");
        }
    }
}
