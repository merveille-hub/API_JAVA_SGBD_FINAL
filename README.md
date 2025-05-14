API Java – Connexion multi-SGBD (.jar)

## Objectif

Cette bibliothèque Java propose une API unifiée permettant de se connecter à plusieurs types de Systèmes de Gestion de Bases de Données Relationnelles (SGBDR) comme **MySQL**, **PostgreSQL** et **SQL Server**.

Elle permet d’exécuter des requêtes SQL (SELECT, INSERT, UPDATE, DELETE) sans se soucier des différences techniques entre les SGBD.


## Structure du projet

ma/ensa/
    |
    ├── db/
        ├── DatabaseManager.java
        ├── MySQLDatabaseManager.java
        ├── PostgreSQLDatabaseManager.java
        └── SQLServerDatabaseManager.java
    ├── util/
        ├── DBConfig.java
        └── DBConfigLoader.java
    └── Main.java
    resources/
        └── dbMySql.properties
        └── dbPostgreSql.properties
        └── dbSqlServer.properties
    test/
    └── java/
        └──Test
    └──resources
        └── db.properties
        └── users.csv


## Fonctionnalités

- Connexion/déconnexion à une base de données selon son type
- Exécution de requêtes SQL dynamiques
- Récupération des résultats sous forme de `List<Map<String, Object>>`
- Chargement automatique des paramètres via un fichier `.properties`
- Gestion propre des exceptions SQL
- Génération d’un `.jar` réutilisable dans d'autres projets
- Tests automatisés avec la bibliothèque **JUnit** + chargement de données `.csv`
- Utilisation de la bibliothèque **Lombok** qui produit le BoilderPlate pour simplifier les modèles

## Prérequis

- JDK 11 ou + recommandé
- Maven ou Gradle pour compiler (facultatif mais recommandé)
- Ajoutez les drivers JDBC nécessaires selon le SGBD utilisé :
  - **MySQL** : `mysql-connector-j`
  - **PostgreSQL** : `postgresql`
  - **SQL Server** : `mssql-jdbc`


## Exemple de configuration (`dbMySql.properties`)

db.type=MYSQL
db.host=localhost
db.port=3306
db.name=ma_bd
db.user=root
db.password=

## Exemple d’utilisation (Main.java)
@org.junit.jupiter.api.Test
    void exemple() throws SQLException, IOException {

        //se connecter a la bd qui se trouve dans ressource
        Connection connection = PropertiesReader.connectFromProperties("dbMySql.properties");
        MYSQL mysql = new MYSQL(connection);
        List<Map<String, Object>> results = mysql.select("users");

        results.forEach(System.out::println);

    }
    
## Lancer les tests (JUnit)
Les tests automatisés :
- Créent une table temporaire
- Y insèrent des données à partir d’un fichier .csv
- Vérifient les insertions, lectures et suppression de la table
- Les tests sont situés dans test/Test.java.

## Générer le .jar
Si vous utilisez Maven :
- mvn clean package
Le fichier .jar sera généré dans le dossier target/.

## Technologies utilisées
- Java 11+
- JDBC
- JUnit 5
- Lombok
- Fichiers .properties et .csv
- SQL (standard)

## Auteur
- Étudiante : MAGNE TSAFACK LYDIVINE MERVEILLE
- Formation : 1ère année Génie Informatique – ENSA
- Année Universitaire : 2024/2025

## Licence
Ce projet est à but pédagogique uniquement. Vous pouvez le réutiliser ou le modifier librement dans le cadre de vos études.