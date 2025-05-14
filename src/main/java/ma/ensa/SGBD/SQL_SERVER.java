package ma.ensa.SGBD;

import ma.ensa.db.DBConfigLoader;
import ma.ensa.util.PropertiesReader;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class SQL_SERVER implements RequetesSGBD {
    Connection connection = null;

    public SQL_SERVER(Connection connection) {
        this.connection = connection;
    }

    public SQL_SERVER() throws SQLException, IOException {
        this.connection = getConnection();
    }

    /**Se connecter à la base de données definis dans le fichier de configuration*/
    public Connection getConnection() throws IOException, SQLException {
        return PropertiesReader.connectFromProperties("dbSqlServer.properties");
    }

    public void disconnect(Connection connection) throws Exception{
        DBConfigLoader.disconnect(connection);
    }

    public boolean insert(String nomTable) throws SQLException, IOException {
        List<String> columnNames = getColumnNames(this.getConnection(), nomTable);
        System.out.println("nombre attributs colunmname" + columnNames.size());
        System.out.println("Afficher les noms des colonnes : ");
        for (String columnName : columnNames) {
            System.out.print(columnName + " ");
        }
        if (!columnNames.isEmpty()) {
            columnNames.remove(0); // Supprime l'ID (première colonne)
        }

        String columns = "(" + String.join(", ", columnNames) + ")";
        //columnNames = "(" + String.join(", ", columnNames) + ")";


        System.out.println("Entrer les valeurs a insérer \n exple (\"Ali\" \"Ali@email.com\" 30) : \nl'ID est généré automatiquement");
        Map<Object, Object> params = getMetaDataMap(getConnection(), nomTable);

        List<Object> paramsList;
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        paramsList = Arrays.asList(line.split(" "));
        String sql = "INSERT INTO " + nomTable + " " + columns + " VALUES " + "(?, ?, ?);";
        System.out.println("column" + columns);
        System.out.println("param list: " + paramsList);
        System.out.println("sql: " + sql);
        insertionSQL(getConnection(), sql, paramsList);
        return true;
    }

    public boolean update(String nomTable) {
        try (Scanner scanner = new Scanner(System.in)) {
            List<String> columnNames = getColumnNames(this.getConnection(), nomTable);

            System.out.println("Colonnes disponibles pour la mise à jour : ");
            columnNames.forEach(column -> System.out.print(column + " "));

            System.out.println("\nEntrez l'ID de l'enregistrement à mettre à jour : ");
            int id = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Entrez les nouvelles valeurs sous forme : nom='Ali', email='Ali@email.com' ");
            String line = scanner.nextLine().trim();

            System.out.println("Voulez-vous modifier cet enregistrement (true/false) : ");
            String sqlSearch = "SELECT INTO " + nomTable + " WHERE id = ?";
            boolean reponse = scanner.nextBoolean();
            if (!reponse) {
                System.out.println("Pas de modification a faire");
            }

            String sql = "UPDATE " + nomTable + " SET " + line + " WHERE id = ?";
            System.out.println("sql: " + sql);
            updateSQL(getConnection(), sql, List.of(id));

            System.out.println("Mise à jour réussie !");
            return true;
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la mise à jour : " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Une erreur inattendue s'est produite : " + e.getMessage());
        }
        return false;
    }

    public List<Map<String, Object>> select(String nomTable) {
        try {
            String sql = "SELECT * FROM " + nomTable;
            List<Map<String, Object>> results = selectSQL(getConnection(), sql, List.of());

            if (results.isEmpty()) {
                System.out.println("Aucun résultat trouvé dans la table " + nomTable);
            } else {
                results.forEach(System.out::println);
            }
            return results;
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la récupération des données : " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Une erreur inattendue s'est produite : " + e.getMessage());
        }
        return null;
    }

    public boolean delete(String nomTable) {
        try (Scanner scanner = new Scanner(System.in)) {
            List<String> columnNames = getColumnNames(this.getConnection(), nomTable);

            System.out.println("Colonnes disponibles pour la mise à jour : ");
            columnNames.forEach(column -> System.out.print(column + " "));

            System.out.println("\nEntrez l'ID de l'enregistrement à supprimer : ");
            int id = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Voulez-vous supprimer cet enregistrement (true/false) : ");
            String sqlSearch = "SELECT INTO " + nomTable + " WHERE id = ?";
            boolean reponse = scanner.nextBoolean();
            if (!reponse) {
                System.out.println("Pas de suppression a faire");
            }

            String sql = "DELETE FROM " + nomTable + " WHERE id = ?";
            int affectedRows = deleteSQL(getConnection(), sql, List.of(id));

            if (affectedRows > 0) {
                System.out.println("Suppression réussie !");
                return true;
            } else {
                System.out.println("Aucun enregistrement trouvé avec l'ID : " + id);
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la suppression : " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Une erreur inattendue s'est produite : " + e.getMessage());
        }
        return false;
    }
}
