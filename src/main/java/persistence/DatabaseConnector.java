package persistence;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

/* This class uses the Singleton pattern */

public class DatabaseConnector implements AutoCloseable {

    private static DatabaseConnector instance = null;

    private DatabaseConnector() throws IOException {

        InputStream inputStream = DatabaseConnector.class.getResourceAsStream("/.properties");
        Properties properties = new Properties();
        properties.load(inputStream);
        inputStream.close();
        databaseURL = properties.getProperty("DATABASE_URL");
        username = properties.getProperty("USERNAME");
        password = properties.getProperty("PASSWORD");
    }

    public static DatabaseConnector getInstance() throws IOException {
        if (instance == null) {
            instance = new DatabaseConnector();
        }
        return instance;
    }

    private static Connection connection;
    private static String databaseURL;
    private static String username;
    private static String password;

    private static Logger log = Logger.getLogger(DatabaseConnector.class.getName());

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
