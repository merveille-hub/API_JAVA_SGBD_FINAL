package ma.ensa.SGBD;

import ma.ensa.db.DBConfigLoader;
import ma.ensa.util.PropertiesReader;

import java.io.IOException;
import java.sql.*;
import java.util.*;

public class MYSQL implements RequetesSGBD {
    private Connection connection = null;
    //todo:retourner l'utilisateur qui a l'id
    public MYSQL(Connection connection) {
        this.connection = connection;
    }

    public MYSQL() throws SQLException, IOException {
        this.connection = getConnection();
    }

    /**
     * Se connecter à la base de données definis dans le fichier de configuration
     */
    public Connection getConnection() throws IOException, SQLException {
        return PropertiesReader.connectFromProperties("dbMySql.properties");
    }

    public void disconnect(Connection connection) throws Exception {
        DBConfigLoader.disconnect(connection);
    }

    public boolean insert(String nomTable) throws SQLException, IOException {
        List<String> columnNames = getColumnNames(this.getConnection(), nomTable);

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
        /*System.out.println("column" + columns);
        System.out.println("param list: " + paramsList);
        System.out.println("sql: " + sql);*/
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

            String sql = "DELETE FROM " + nomTable + " WHERE id = ?";
            System.out.println("Voulez-vous supprimer cet enregistrement (true/false) : ");
            String sqlSearch = "SELECT INTO " + nomTable + " WHERE id = ?";
            boolean reponse = scanner.nextBoolean();
            if (!reponse) {
                System.out.println("Pas de suppression a faire");
            }
            PreparedStatement statement = getConnection().prepareStatement(sqlSearch);
            System.out.println(findById(getConnection(), nomTable, id));


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
















































    public String attributsSansId(String tableName, Connection conn) throws SQLException {
        List<String> columnNames = getColumnNames(conn, tableName);

        if (!columnNames.isEmpty()) {
            columnNames.remove(0); // Supprime l'ID (première colonne)
        }

        return "(" + String.join(", ", columnNames) + ")";
    }


    //todo: select avec condition
    /*public void selectAvecCondition(String nomTable) throws SQLException{*/
    /*    try (Scanner scanner = new Scanner(System.in)) {*/
    /*        String sql = "SELECT * FROM " + nomTable;                                           */
    /*        List<Map<String, Object>> results = selectSQL(getConnection(), sql, List.of()); */
    /*    }*/
    /*        List<String> columnNames = getColumnNames(this.getConnection(), nomTable);*/
    /**/
    /*        System.out.println("Colonnes disponibles pour la mise à jour : ");*/
    /*        columnNames.forEach(column -> System.out.print(column + " "));*/
    /**/
    /*        System.out.println("\nEntrez l'ID de l'enregistrement à mettre à jour : ");*/
    /*        int id = scanner.nextInt();*/
    /*        scanner.nextLine();*/
    /*} catch (IOException e) {*/
    /*        throw new RuntimeException(e);*/
    /*    }*/



    /*public void select(String nomTable) {*/
    /*    try {*/
    /*        String sql = "SELECT * FROM " + nomTable;*/
    /*        List<Map<String, Object>> results = executeQuery(getConnection(), sql, List.of());*/
    /**/
    /*        if (results.isEmpty()) {*/
    /*            System.out.println("⚠️ Aucun enregistrement trouvé dans la table " + nomTable);*/
    /*        } else {*/
    /*            System.out.println("Données de la table " + nomTable + " :");*/
    /*            for (Map<String, Object> row : results) {*/
    /*                System.out.println(row);*/
    /*            }*/
    /*        }*/
    /*    } catch (SQLException e) {*/
    /*        System.err.println("❌ Erreur SQL lors de la récupération des données : " + e.getMessage());*/
    /*    } catch (Exception e) {*/
    /*        System.err.println("⚠️ Une erreur inattendue s'est produite : " + e.getMessage());*/
    /*    }*/
    /*}*/
    /**/







    public String[] splitLine(String line, String delimiter) {
        return line.split(delimiter);
    }/**
     }
     * Cela supprimera les caractères spéciaux et affichera uniquement les mots
     */
    public List<String> extractWordsClean(String line) {
        return Collections.singletonList(line.replaceAll("[^\\p{L}\\p{Nd}]+", " ").trim());
        //return Arrays.asList(line.replaceAll("[^\\p{L}0-9]+", " ").trim().split("\\s+"));
    }

    public static String supprimerVirgule(String sentence) {
        return sentence.replaceAll(",\\s*$", "").trim();
    }

    /*public String formatQuery(List<String> attributes) {
        String query = new String("(");
        for (String attribute : attributes) {
            query += attribute + ",";
        }
        query = MYSQL.supprimerVirgule(query);
        query += ");";
        return query;
    }*/

    public static String formatQuery(List<String> attributes) {
        StringJoiner joiner = new StringJoiner(",", "(", ")");
        attributes.forEach(joiner::add);
        return joiner.toString();
    }




}