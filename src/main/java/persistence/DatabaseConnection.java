package persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

/* This class uses the Singleton pattern */

public class DatabaseConnection implements AutoCloseable {

    private static DatabaseConnection instance = null;

    private DatabaseConnection() {
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    private static Connection connection;
    // TODO: move this to a local uncommitted properties file, and commit an
    // empty properties file
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/computer-database-db?serverTimezone=UTC";
    private static final String USERNAME = "admincdb";
    private static final String PASSWORD = "qwerty1234";

    private static Logger log = Logger.getLogger(DatabaseConnection.class.getName());

    /**
     * Connects to the database.
     *
     * @return the Connection object if the connection was successful
     */
    public Connection connect() {

        try {
            connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
            log.info("Connecting to the database:\nURL: " + DATABASE_URL + "\nUsername: "
                    + USERNAME);
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
