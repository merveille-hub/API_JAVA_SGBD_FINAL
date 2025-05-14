package ma.ensa.SGBD;

import ma.ensa.db.DBConfigLoader;
import ma.ensa.db.RequeteManager;

import java.io.IOException;
import java.sql.*;
import java.util.*;

public interface RequetesSGBD {


    public Connection getConnection() throws IOException, SQLException;
    void disconnect(Connection connection) throws Exception;

    //todo : creer table avec fichier de configuration

    public default boolean createTable(Connection connection, String sqlCreerTable) throws SQLException, IOException{
        sqlCreerTable = sqlCreerTable.trim().toLowerCase();
        if (sqlCreerTable.isEmpty()) {
            throw new SQLException("Create table est nulle.");
        }
        sqlCreerTable = sqlCreerTable.trim().toLowerCase();
        if (!sqlCreerTable.contains("create table")) {
            throw new IllegalArgumentException("La requête Create table n'est pas valide.");
        }
        return RequeteManager.createTable(connection, sqlCreerTable);
    }

    public default boolean dropTable(Connection connection, String sqlDropTable) throws SQLException, IOException{
        sqlDropTable = sqlDropTable.trim().toLowerCase();
        if (sqlDropTable.isEmpty()) {
            throw new SQLException("Drop table est nulle.");
        }
        if (!sqlDropTable.contains("create table")) {
            throw new IllegalArgumentException("La requête Drop table n'est pas valide.");
        }
        return RequeteManager.dropTable(connection, sqlDropTable);
    }

    public default int insertionSQL(Connection conn, String query, List<Object> params) throws SQLException {
        if (query == null || query.isEmpty()){
            throw new SQLException("La requête Insertion est vide.");
        }
        if (!query.toLowerCase().contains("insert into") || !query.toLowerCase().contains("values")){
            throw new IllegalArgumentException("La requête Insertion n'est pas valide.");
        }

        return RequeteManager.executeUpdate(conn, query, params);
    }

    public default int updateSQL(Connection conn, String query, List<Object> params) throws SQLException {
        if (query == null || query.isEmpty()){
            throw new SQLException("La requête Update est vide.");
        }
        if (!query.toLowerCase().contains("update") || !query.toLowerCase().contains("set")){
            throw new IllegalArgumentException("La requête Update n'est pas valide.");
        }
        return RequeteManager.executeUpdate(conn, query, params);
    }

    public default int deleteSQL(Connection conn, String query, List<Object> params) throws SQLException {
        if (query == null || query.isEmpty()){
            throw new SQLException("La requête delete est vide.");
        }
        if (!query.toLowerCase().contains("delete from") || !query.toLowerCase().contains("where")){
            throw new IllegalArgumentException("La requête Delete n'est pas valide.");
        }
        return RequeteManager.executeUpdate(conn, query, params);
    }

    public default ResultSetMetaData getMetaData(Connection connection, String nomTable) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + nomTable);
        ResultSetMetaData metaData = resultSet.getMetaData();
        return metaData;
    }

    /**1er Object -> column name
     * 2eme Object -> column type*/
    public default Map<Object, Object> getMetaDataMap(Connection connection, String nomTable) throws SQLException {
        ResultSetMetaData metaData = getMetaData(connection, nomTable);
        Map<Object, Object> map = new HashMap<>();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            map.put(metaData.getColumnName(i), metaData.getColumnTypeName(i));
        }
        return map;
    }

    public default List<String> getColumnNames(ResultSetMetaData resultSetMetaData) throws SQLException {
        List<String> columnNames = new ArrayList<>();

        int columnCount = resultSetMetaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            columnNames.add(resultSetMetaData.getColumnLabel(i)); // Récupérer le nom de chaque colonne
        }
        return columnNames;
    }
    public default List<String> getColumnNames(Connection connection, String nomTable) throws SQLException {
        ResultSetMetaData resultSetMetaData = getMetaData(connection, nomTable);
        return getColumnNames(resultSetMetaData);
    }

    public default Map<String, Object> findById(Connection conn, String nomTable, int id) throws SQLException {
        String sql = "SELECT * FROM " + nomTable + " WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                ResultSetMetaData metaData = rs.getMetaData();

                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    row.put(metaData.getColumnLabel(i), rs.getObject(i));
                }

                return row;
            } else {
                System.out.println("Aucun résultat trouvé pour l'ID : " + id);
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL : " + e.getMessage());
        }

        return Collections.emptyMap(); // Retourne une map vide si aucun résultat
    }

    public default List<Map<String, Object>> selectSQL(Connection connection, String query, List<Object> params) throws SQLException {
        if (query == null || query.isEmpty()){
            throw new SQLException("La requête select est vide.");
        }
        if (!query.toLowerCase().contains("select") || !query.toLowerCase().contains("from")){
            throw new IllegalArgumentException("La requête select n'est pas valide.");
        }

        List<Map<String, Object>> results = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            for (int i = 0; i < params.size(); i++) stmt.setObject(i + 1, params.get(i));
            try (ResultSet rs = stmt.executeQuery()) {
                results = extractResults(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL : " + e.getMessage());
        }
        return results;
    }

    private List<Map<String, Object>> extractResults(ResultSet rs) throws SQLException {
        List<Map<String, Object>> results = new ArrayList<>();
        ResultSetMetaData meta = rs.getMetaData();
        int columnCount = meta.getColumnCount();

        while (rs.next()) {
            Map<String, Object> row = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) row.put(meta.getColumnLabel(i), rs.getObject(i));
            results.add(row);
        }
        return results;
    }

    public default void afficherResultat(List<Map<String, Object>> results) throws SQLException {
        // Affichage des résultats
        for (Map<String, Object> ligne : results) {
            System.out.println("----- Nouvelle ligne -----");
            for (Map.Entry<String, Object> entry : ligne.entrySet()) {
                System.out.println(entry.getKey() + " : " + entry.getValue());
            }
        }
    }


}