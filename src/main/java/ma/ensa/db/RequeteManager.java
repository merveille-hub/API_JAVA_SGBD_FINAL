package ma.ensa.db;

import java.io.IOException;
import java.sql.*;
import java.util.*;

public class RequeteManager {
    /**todo: créer une table*/
    public static boolean createTable(Connection connection, String sqlCreerTable) throws SQLException, IOException {
        sqlCreerTable = sqlCreerTable.trim().toLowerCase();
        if (sqlCreerTable.isEmpty()) {
            throw new IllegalArgumentException("sqlCreerTable est nulle ou vide.");
        }

        if (!sqlCreerTable.contains("create table")) {
            throw new SQLException("Ce n'est pas une requête CREATE sqlCreerTable.");
        }

        try(connection) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sqlCreerTable);
            System.out.println("Table créée avec succès.");
            return true;
        }
        catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        }
        return false;
    }

    /**todo: supprimer une table*/
    public static boolean dropTable(Connection connection, String sqlSupprimerTable) throws SQLException, IOException {
        sqlSupprimerTable = sqlSupprimerTable.trim().toLowerCase();

        if (sqlSupprimerTable.isEmpty()) {
            throw new IllegalArgumentException("La requête sql est nulle ou vide.");
        }

        if (!sqlSupprimerTable.contains("drop table")) {
            throw new SQLException("Ce n'est pas une requête DELETE sql.");
        }

        try(connection) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sqlSupprimerTable);
            System.out.println("Table 'utilisateurs' supprimées avec succès.");
            return true;
        }catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        }
        return false;
    }

    /**
     * Exécute une requête SELECT et retourne le résultat sous forme de liste de maps.
     */
    public static List<Map<String, Object>> executeSelect(Connection conn, String query) throws SQLException {
        List<Map<String, Object>> results = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            //setParameters(stmt, params);

            try (ResultSet rs = stmt.executeQuery()) {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                while (rs.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        row.put(metaData.getColumnLabel(i), rs.getObject(i));
                    }
                    results.add(row);
                }
            }
        }

        return results;
    }
    public static List<Map<String, Object>> executeSelect(Connection conn, String query, List<Object> params) throws SQLException {
        List<Map<String, Object>> results = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            setParameters(stmt, params);

            try (ResultSet rs = stmt.executeQuery()) {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                while (rs.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        row.put(metaData.getColumnLabel(i), rs.getObject(i));
                    }
                    results.add(row);
                }
            }
        }

        return results;
    }

    public List<Map<String, Object>> executeQuery(Connection conn, String sql, List<Object> params) throws Exception {
        if (conn == null) throw new IllegalStateException("La connexion est nulle !");

        List<Map<String, Object>> results = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Assigner les paramètres à la requête SQL
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                ResultSetMetaData meta = rs.getMetaData();
                int columnCount = meta.getColumnCount();

                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        row.put(meta.getColumnLabel(i), rs.getObject(i));
                    }
                    results.add(row);
                }
            }
        }
        return results;
    }

    /**
     * Exécute une requête INSERT, UPDATE ou DELETE. Retourne le nombre de lignes affectées.
     */
    public static int executeUpdate(Connection conn, String query) throws SQLException {
        if (query == null || query.isEmpty()) {
            throw new IllegalArgumentException("La requête sql est nulle ou vide.");
        }
        if (conn == null || conn.isClosed()) {
            throw new IllegalArgumentException("La connexion est nulle ou fermée.");
        }

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            //Utilitaire pour injecter les paramètres dans la requête préparée.
            //setParameters(stmt, params);
            return stmt.executeUpdate();
        }
    }
    public static int executeUpdate(Connection conn, String query, List<Object> params) throws SQLException {
        if (query == null || query.isEmpty()) {
            throw new IllegalArgumentException("La requête sql est nulle ou vide.");
        }

        if (conn == null || conn.isClosed()) {
            throw new IllegalArgumentException("La connexion est nulle ou fermée.");
        }

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            //Utilitaire pour injecter les paramètres dans la requête préparée.
            setParameters(stmt, params);
            /*stmt.executeQuery()*/
            return stmt.executeUpdate();
        }
    }

    /**
     * Utilitaire pour injecter les paramètres dans la requête préparée.
     */
    private static void setParameters(PreparedStatement stmt, List<Object> params) throws SQLException {
        if (params != null) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
        }
    }


    /**
     * DBType est une enumeration et contient les valeurs suivante
     * MYSQL,
     * POSTGRESQL,
     * SQLSERVER,
     * ORACLE*/
    private static void setParameters(PreparedStatement stmt, List<Object> params, int typeDB) throws SQLException {
        if (params != null) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i), typeDB);
            }
        }
    }

    /**Pour injecter les parametres dans une requete prepares en saisissant le type de la BD*/
    private int getIntDBType(PreparedStatement stmt, List<Object> params, DBType dbType) throws SQLException {
        Map<Integer, String> enumToInt = new HashMap<>();

        //todo: ajouter les elements d'une enumeration dans une liste

        if (params != null) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i), dbType.ordinal() + 1);
            }
        }
        return 0;
    };
}
