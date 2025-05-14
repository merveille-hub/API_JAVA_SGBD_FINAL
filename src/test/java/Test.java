import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;



import java.sql.*;


class Test {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/ma_bd_test" + "?useSSL=false&serverTimezone=UTC";
    private static final String USERNAME;
    private static final String PASSWORD;

    private int count = 101;
    private Connection connection;

    /*static {
        try {
            JDBC_URL = DBConfigLoader.get("db.url");
            USERNAME = DBConfigLoader.get("db.user");
            PASSWORD = DBConfigLoader.get("db.password");
            System.out.println(JDBC_URL + ";" + USERNAME + ";" + PASSWORD);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
*/
    static {
        try {
            Properties props = new Properties();
            // Charge depuis src/main/resources/dbMySql.properties
            try (InputStream input = ClassLoader.getSystemResourceAsStream("db.properties")) {
                if (input == null) {
                    throw new RuntimeException("Fichier dbMySql.properties introuvable");
                }
                props.load(input);
            }

            //JDBC_URL = props.getProperty("db.url");
            USERNAME = props.getProperty("db.user");
            PASSWORD = props.getProperty("db.password");

            if (/*JDBC_URL == null || */USERNAME == null) {
                throw new RuntimeException("Configuration incomplète");
            }

        } catch (IOException e) {
            throw new RuntimeException("Erreur de chargement de configuration", e);
        }
    }



    /**ce test s'execute avant tous les autres tests*/
    @BeforeEach
    void connect() throws SQLException {
        connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        // Initialiser la base de test
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS utilisateur (" +
                    "id INT , " +
                    "username VARCHAR(50), " +

                    "age INT, " +
                    "email VARCHAR(100))");

            stmt.execute("INSERT INTO utilisateur VALUES (102, 'testuser', 30, 'test@example.com')");
            count++;
        }
    }

    @org.junit.jupiter.api.Test
    @DisplayName("La connexion à la base de données devrait être valide")
    void connectToDatabase() throws SQLException {
        assertNotNull(connection);
        assertFalse(connection.isClosed());
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Devrait pouvoir lire des données de la base")
    void testReadDataFromDatabase() throws SQLException {
        String sql = "SELECT * FROM utilisateur WHERE id = 1";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            assertTrue(rs.next());
            assertEquals("testuser", rs.getString("nom"));
            assertEquals(30, rs.getInt("age"));
            assertEquals("test@example.com", rs.getString("email"));
        }
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Devrait pouvoir écrire des données dans la base")
    void testWriteDataToDatabase() throws SQLException {
        String insertSQL = "INSERT INTO utilisateur VALUES (2, 'newuser', 24, 'new@example.com')";

        try (Statement stmt = connection.createStatement()) {
            int rowsAffected = stmt.executeUpdate(insertSQL);
            assertEquals(1, rowsAffected);
        }

        // Vérifier que les données ont bien été insérées
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM utilisateur WHERE id = 2")) {

            assertTrue(rs.next());
            assertEquals("newuser", rs.getString("nom"));
        }
    }


    /*String sql = "SELECT * FROM users WHERE id = 1";
        try (Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {
            assertTrue(rs.next());
            assertEquals("testuser", rs.getString("nom"));
            assertEquals("test@example.com", rs.getString("email"));
        }*/

    @org.junit.jupiter.api.Test
    @DisplayName("commande select devrait fonctionner")
    void executeSelect() throws SQLException {
        // Exécution d’un SELECT
        String selectQuery = "SELECT * FROM utilisateur WHERE age > ?";
        List<Object> selectParams = List.of(20);
        List<Map<String, Object>> result = DatabaseManager.executeSelect(connection, selectQuery, selectParams);
        for (Map<String, Object> user : result) {
            System.out.println(user);
            assertNotEquals(user, null);
        }
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Insertion dans la base de données devrait marcher")
    void executeInsert() throws SQLException {
        // Exécution d’un INSERT
        String insertQuery = "INSERT INTO utilisateur (id, nom, email, age) VALUES (?, ?, ?, ?)";
        List<Object>insertParams = Arrays.asList(3, "jeir", "diro@mail.com", 30);
        int insertRows = DatabaseManager.executeUpdate(connection, insertQuery, insertParams);
        System.out.println("Lignes insérées : " + insertRows);
    }

    @org.junit.jupiter.api.Test
    void executeUpdate() throws SQLException {
        executeSelect();
        executeInsert();
        executeUpdater();
    }

    @org.junit.jupiter.api.Test
    @DisplayName("mise à jour de la base de données")
    void executeUpdater() throws SQLException {
        //Exécution d'un UPDATE
        String updateQuery = "UPDATE utilisateur SET age = ? WHERE nom = ?";
        List<Object> updateParams = Arrays.asList(30, "testuser");
        int updateCount = DatabaseManager.executeUpdate(connection, updateQuery, updateParams);
        System.out.println("Lignes mises à jour : " + updateCount);
        assertNotEquals(0, updateCount);
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Supprimer un utilisateur de la base de données")
    void executeDelete() throws SQLException {
        //Exécution de DELETE
        String deleteQuery = "DELETE FROM utilisateur WHERE nom = ?";
        List<Object>deleteParams = Arrays.asList("newuser");
        int deleteRows = DatabaseManager.executeUpdate(connection, deleteQuery, deleteParams);
        System.out.println("Lignes supprimés : " + deleteRows);
        assertNotEquals(0, deleteRows);
    }


    @AfterEach
    void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
