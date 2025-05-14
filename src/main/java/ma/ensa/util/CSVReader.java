package ma.ensa.util;

import ma.ensa.Utilisateurs;

import java.io.*;
import java.util.*;

public class CSVReader {

    public static List<Utilisateurs> readUsersFromCSV(String filePath) {
        List<Utilisateurs> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firsttwoLine = true;
            int counter = 0;
            while ((line = br.readLine()) != null) {
                if (counter < 2) {
                    counter++;
                    continue;
                }
                if (firsttwoLine) {
                    firsttwoLine = false; // Skip header
                }

                String[] parts = line.split(";;");
                usersParts(users, parts);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static List<Utilisateurs> readUsersFromCSV(String filePath, String delimiter) throws FileNotFoundException {
        List<Utilisateurs> users = new ArrayList<>();
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))){
            String line;
            boolean firsttwoLine = true;
            int counter = 0;
            while ((line = bufferedReader.readLine()) != null) {
                if (counter < 2) {
                    counter++;
                    continue;
                }if (firsttwoLine) {
                    firsttwoLine = false; // Skip header
                }

                String[] parts = line.split(delimiter);
                usersParts(users, parts);
            }
        }
        catch (FileNotFoundException e) {
            e.getMessage();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    private static void usersParts(List<Utilisateurs> users, String[] parts) {
        if (parts.length == 4) {
            int id = Integer.parseInt(parts[0]);
            String username = parts[1];
            int age = Integer.parseInt(parts[3]);
            String email = parts[2];
            users.add(new Utilisateurs(id, username, email, age));
        }
    }

}
