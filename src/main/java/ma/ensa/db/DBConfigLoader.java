package ma.ensa.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;


public class DBConfigLoader {

    public static Connection connect(DBType type, String host, int port, String dbName, String user, String password) throws SQLException {

        if (type == null || host == null || dbName == null || user == null || password == null) {

            return null;
        }

        String url = "";

        switch (type) {
            case MYSQL:
                url = "jdbc:mysql://" + host + ":" + port + "/" + dbName + "?useSSL=false&serverTimezone=UTC";
                break;
            case POSTGRESQL:
                url = "jdbc:postgresql://" + host + ":" + port + "/" + dbName;
                break;
            case SQLSERVER:
                url = "jdbc:sqlserver://" + host + ":" + port + ";databaseName=" + dbName;
                break;
            case ORACLE:
                url = "jdbc:oracle:thin:@" + host + ":" + port + ":" + dbName;
                break;
            default:
                throw new IllegalArgumentException("Type de base de données non supporté.");
        }
        return DriverManager.getConnection(url, user, password);
    }

    /**permet de fermer une connexion à une base de données en vérifiant d’abord qu’elle est non nulle et ouverte avant de la fermer.
     */
    public static void disconnect(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
            }
        }
    }


    /**se connecter à une bd dont les proprietes sont dans le fichier
     * dbMySql.properties dans le dossier ressource
     */
    /*public static Connection connectFromProperties() throws IOException, SQLException {
        Properties props = new Properties();

        try (InputStream input = com.mervy.root.db.DBConfigLoader.class.getClassLoader().getResourceAsStream("dbMySql.properties")) {
            if (input == null) {
                throw new IOException("Fichier dbMySql.properties introuvable !");
            }
            props.load(input);
        }

        // Lecture des propriétés
        try{
            DBType type = DBType.valueOf(props.getProperty("db.type").toUpperCase());
            String host = props.getProperty("db.host");
            int port = Integer.parseInt(props.getProperty("db.port"));
            String dbName = props.getProperty("db.name");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");
            return connect(type, host, port, dbName, user, password);
        }
        catch (Exception e){
            System.err.println("Les informations obligatoires de \" dbMySql.properties \" est introuvable !"    );
            e.printStackTrace();
        }


        return null;
    }

    public static Connection connectFromProperties(String filename) throws IOException, SQLException {
        Properties props = new Properties();

        try (InputStream input = com.mervy.root.db.DBConfigLoader.class.getClassLoader().getResourceAsStream(filename)) {
            if (input == null) {
                throw new IOException("Fichier dbMySql.properties introuvable !");
            }
            props.load(input);
        }

        // Lecture des propriétés
        try{
            DBType type = DBType.valueOf(props.getProperty("db.type").toUpperCase());
            String host = props.getProperty("db.host");
            int port = Integer.parseInt(props.getProperty("db.port"));
            String dbName = props.getProperty("db.name");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");
            return connect(type, host, port, dbName, user, password);
        }
        catch (Exception e){
            System.err.println("Les informations obligatoires de \" dbMySql.properties \" est introuvable !"    );
            e.printStackTrace();
        }


        return null;
    }*/






}
