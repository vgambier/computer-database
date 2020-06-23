package persistence;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

/* This class uses the Singleton pattern */

public class DatabaseConnection implements AutoCloseable {

    private static DatabaseConnection instance = null;

    private DatabaseConnection() throws IOException {

        FileInputStream fis = new FileInputStream("config/db/.properties");
        Properties p = new Properties();
        p.load(fis);
        databaseURL = (String) p.get("DATABASE_URL");
        username = (String) p.get("USERNAME");
        password = (String) p.get("PASSWORD");
    }

    public static DatabaseConnection getInstance() throws IOException {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    private static Connection connection;
    private static String databaseURL;
    private static String username;
    private static String password;

    private static Logger log = Logger.getLogger(DatabaseConnection.class.getName());

    /**
     * Connects to the database.
     *
     * @return the Connection object if the connection was successful
     */
    public Connection connect() {

        try {
            connection = DriverManager.getConnection(databaseURL, username, password);
            log.info(
                    "Connecting to the database:\nURL: " + databaseURL + "\nUsername: " + username);
        } catch (SQLException e) {
            System.out.println("Cannot connect to the database!");
            throw new IllegalStateException("Exception: cannot connect to the database.", e);
        }

        return connection;

    }

    /**
     * Disconnects from the database.
     *
     * @throws PersistenceException
     */
    @Override
    public void close() throws Exception {

        if (connection != null) {
            try {
                connection.close();
                connection = null;
                log.info("Disconnected from the database.");
            } catch (SQLException e) {
                throw new PersistenceException("Couldn't close the connection!", e);
            }
        }
    }
}
