package fr.fms.trainingstore.config;


import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class ConnectionFactory {

    // 1) Chemin du fichier dans le classpath (src/main/resources)
    private static final String PROPERTIES_FILE = "/db.properties";

    // 2) Instance unique (Singleton)
    private static ConnectionFactory instance;

    // 3) Paramètres de connexion lus depuis db.properties
    private final String url;
    private final String user;
    private final String password;

    // 4) Constructeur privé : interdit "new ConnectionFactory()"
    private ConnectionFactory() {
        Properties props = new Properties();

        // 5) On charge le fichier depuis src/main/resources
        try (InputStream is = ConnectionFactory.class.getResourceAsStream(PROPERTIES_FILE)) {
            if (is == null) {
                throw new IllegalStateException("db.properties introuvable dans src/main/resources.");
            }
            props.load(is);
        } catch (IOException e) {
            throw new IllegalStateException("Impossible de lire db.properties.", e);
        }

        // 6) On récupère les valeurs
        this.url = props.getProperty("db.url");
        this.user = props.getProperty("db.user");
        this.password = props.getProperty("db.password");

        // 7) On charge le driver JDBC
        String driver = props.getProperty("db.driver");
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Driver JDBC introuvable : " + driver, e);
        }
    }

    // 8) Point d’accès unique au Singleton
    public static ConnectionFactory getInstance() {
        if (instance == null) {
            instance = new ConnectionFactory();
        }
        return instance;
    }

    // 9) Fournit une connexion JDBC prête à l'emploi
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
