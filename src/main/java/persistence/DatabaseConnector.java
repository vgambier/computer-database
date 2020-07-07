package persistence;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.zaxxer.hikari.HikariDataSource;

/* This class uses the Singleton pattern */

public class DatabaseConnector {

    private static HikariDataSource hikariDataSource;
    private static String databaseURL;
    private static String username;
    private static String password;

    private static DatabaseConnector instance = null;

    private DatabaseConnector() throws IOException {

        InputStream inputStream = DatabaseConnector.class.getResourceAsStream("/.properties");
        Properties properties = new Properties();
        properties.load(inputStream);
        inputStream.close();

        databaseURL = properties.getProperty("DATABASE_URL");
        username = properties.getProperty("USERNAME");
        password = properties.getProperty("PASSWORD");

        hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(databaseURL);
        hikariDataSource.setUsername(username);
        hikariDataSource.setPassword(password);
        hikariDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariDataSource.setMaximumPoolSize(100);
    }

    public static DatabaseConnector getInstance() throws IOException {
        if (instance == null) {
            instance = new DatabaseConnector();
        }
        return instance;
    }

    // TODO: add more logging
    private static final Logger LOG = Logger.getLogger(DatabaseConnector.class.getName());

    /**
     * Connects to the database.
     *
     * @return the Connection object if the connection was successful
     * @throws SQLException
     */
    public Connection connect() throws SQLException {

        BasicConfigurator.configure(); // configuring the Logger
        LOG.info("Connecting to the database:\nURL: " + databaseURL + "\nUsername: " + username);

        return hikariDataSource.getConnection();
    }
}
