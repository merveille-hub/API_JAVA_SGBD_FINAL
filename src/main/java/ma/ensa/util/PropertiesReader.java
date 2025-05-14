package ma.ensa.util;

import ma.ensa.db.DBConfigLoader;
import ma.ensa.db.DBType;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;

public class PropertiesReader {

    /**Se connecter à une bd dont les propriétés sont dans un fichier
     * de type *.properties dans le dossier ressource
     * @param filename nom du fichier de configuration
     */
    public static Connection connectFromProperties(String filename) throws IOException, SQLException {
        Properties props = new Properties();
        //DBConfigLoader.connectFromProperties("dbPostgreSql.properties");

        try (InputStream input = ma.ensa.db.DBConfigLoader.class.getClassLoader().getResourceAsStream(filename)) {
            if (input == null) {
                throw new IOException("Fichier " + filename + " introuvable !");
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
            System.out.println(type + " " + host + " " + port + " " + dbName + " " + user + " " + password);
            return DBConfigLoader.connect(type, host, port, dbName, user, password);
        }
        catch (Exception e){
            System.err.println("Les informations obligatoires de " + filename + " est introuvable !"    );
            e.printStackTrace();
        }


        return null;
    }

    /**utilisée avec les paramètres
     * @param getProperty donne la propriété recherche dans le fichier
     * @param filename
     * @param getProperty peut avoir les valeurs suivantes :
     *      db.type           : type de base de données exple : SQLSERVER, MYSQL, POSTGRESQL
     *      db.host           : nom de l'hébergeur exple : localhost
     *      db.port           : numéro de port exple : 3306, 1433
     *      db.name           : nom de la base de données exple : ma_bd
     *      db.user           : nom de l'utilisateur de la base de données exple : sa, root
     *      db.password       : mot de passe de la base de données
     * */
    public static String get(String getProperty, String filename) throws IOException {
        Properties props = new Properties();
        props.load(ma.ensa.db.DBConfigLoader.class.getClassLoader().getResourceAsStream(filename));
        return props.getProperty(getProperty);
    }

    public static HashMap<String, String> getListProperties(String filename) throws IOException {
        Properties properties = new Properties();

        properties.load(ma.ensa.db.DBConfigLoader.class.getClassLoader().getResourceAsStream(filename));

        HashMap<String, String> listProperties = new HashMap<>();

        properties.forEach((key, value) -> listProperties.put((String) key, (String) value));

        /*for (String key : properties.stringPropertyNames()) {
            listProperties.put(key, properties.getProperty(key));
        }*/
        return listProperties;
    }
}
