package ma.ensa;


import ma.ensa.SGBD.MYSQL;
import ma.ensa.SGBD.POSTGRESQL;
import ma.ensa.SGBD.SQL_SERVER;
import ma.ensa.db.RequeteManager;
import ma.ensa.util.CSVReader;
import ma.ensa.util.CSVUserImporter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static ma.ensa.db.RequeteManager.executeUpdate;

public class Main {

    public static void main(String[] args) throws SQLException, IOException {
        /************************SE CONNECTER SGBDs************************************/
        System.out.println("Connecting to database MySQL.");
        MYSQL mysql = new MYSQL();
        /*System.out.println("Connecting to database POSTGRESQL.");
        POSTGRESQL postgresql = new POSTGRESQL();
        System.out.println("Connecting to database SQLSERVER.");
        //SQL_SERVER sqlserver = new SQL_SERVER();
        System.out.println("Connecting to databases successfully.");
*/

        /***************************CREATE DATABASE***************************************/
        //mysql
        String sqlMYSQL = "CREATE TABLE users (\n" +
                "                             id INT PRIMARY KEY AUTO_INCREMENT, \n" +
                "                             nom VARCHAR(100) NOT NULL,\n" +
                "                             email VARCHAR(150) NOT NULL UNIQUE,\n" +
                "                             age INT NOT NULL\n" +
                ");";

        System.out.println(mysql.createTable(mysql.getConnection(), sqlMYSQL));

        //Postgresql
        /*String sqlPostgreSQL = "CREATE TABLE users (\n" +
                "                             id SERIAL PRIMARY KEY,\n" +
                "                             nom VARCHAR(100) NOT NULL,\n" +
                "                             email VARCHAR(150) NOT NULL UNIQUE,\n" +
                "                             age INT NOT NULL\n" +
                ");";
        System.out.println(postgresql.createTable(postgresql.getConnection(), sqlPostgreSQL));*/
/*
        *//*****************************MYSQL*************************************/
        String sql = "select * from users";
        CSVUserImporter.insertUsersFromCsv(mysql.getConnection(), "utlisateurs.csv", "users");
       // RequeteManager.executeSelect(mysql.getConnection(), sql, );
        List<Utilisateurs> utilisateursList = CSVReader.readUsersFromCSV("utilisateurs.csv");

        /*RequeteManager.insert
                executeUpdate(mysql.getConnection(), sql, utilisateursList);
        *//*mysql.insert("users");

        mysql.update("users");
        mysql.select("users");
        mysql.delete("users");

        *//*****************************POSTGRESQL*************************************//*
        postgresql.insert("users");
        postgresql.update("users");
        postgresql.select("users");
        postgresql.delete("users");

        *//*****************************SQLSERVER*************************************/
        //mysql.insert("users");
        //mysql.update("users");
        //mysql.select("users");
        //mysql.delete("users");



        /*Utilisateurs users1 = new Utilisateurs("Joe", 20, "joe@gmail.com");

        List<Utilisateurs> users = CSVReader.readUsersFromCSV("src/main/resources/utilisateur.csv");

        *//*for (Utilisateurs u : users) {
            System.out.println(u);
        }*/

        /*try (Connection conn = DBConfigLoader.connectFromProperties("dbSqlServer.properties")) {
            System.out.println("Connected to SQL Server");
            // Exécution d’un INSERT
            String insertQuery = \"INSERT INTO utilisateur (nom, email, age) VALUES (?, ?, ?)";

            try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {

                for (Utilisateurs user : users) {
                    stmt.setString(1, user.getNom());
                    stmt.setString(2, user.getEmail());
                    stmt.setInt(3, user.getAge());
                    stmt.executeUpdate();  // ou stmt.addBatch() pour exécuter en lot
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/

        /*try (Connection conn = DBConfigLoader.connectFromProperties("dbPostgreSql.properties")) {
            System.out.println("Connected to PostgreSQL");
            // Exécution d’un INSERT
            String insertQuery = "INSERT INTO utilisateur (nom, email, age) VALUES (?, ?, ?)";

            try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {

                for (Utilisateurs user : users) {
                    stmt.setString(1, user.getNom());
                    stmt.setString(2, user.getEmail());
                    stmt.setInt(3, user.getAge());
                    stmt.executeUpdate();  // ou stmt.addBatch() pour exécuter en lot
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
        /*try (Connection conn = DBConfigLoader.connectFromProperties()) {
            // Exécution d’un INSERT
            String insertQuery = "INSERT INTO utilisateur (nom, email, age) VALUES (?, ?, ?)";

            try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {

                for (Utilisateurs user : users) {
                    stmt.setString(1, user.getNom());
                    stmt.setString(2, user.getEmail());
                    stmt.setInt(3, user.getAge());
                    stmt.executeUpdate();  // ou stmt.addBatch() pour exécuter en lot
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }


            // Exécution d’un SELECT
            String selectQuery = "SELECT * FROM utilisateur WHERE age > ?";
            List<Object> selectParams = List.of(25);
            List<Map<String, Object>> result = DatabaseManager.executeSelect(conn, selectQuery, selectParams);
            for (Map<String, Object> user : result) {
                System.out.println(user);
            }

            *//*Arrays.asList("Diro", "diro@mail.com", 30);
            int insertRows = DatabaseManager.executeUpdate(conn, insertQuery, insertParams);
            System.out.println("Lignes insérées : " + insertRows);*//*

            //Exécution d'un UPDATE
            String updateQuery = "UPDATE utilisateur SET age = ? WHERE nom = ?";
            List<Object> updateParams = Arrays.asList(35, "Ali");
            int updateCount = DatabaseManager.executeUpdate(conn, updateQuery, updateParams);
            System.out.println("Lignes mises à jour : " + updateCount);

            //Exécution de DELETE
            String deleteQuery = "DELETE FROM utilisateur WHERE nom = ?";
            List<Object>deleteParams = Arrays.asList("Helo");
            int deleteRows = DatabaseManager.executeUpdate(conn, deleteQuery, deleteParams);
            System.out.println("Lignes supprimés : " + deleteRows);

        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
}