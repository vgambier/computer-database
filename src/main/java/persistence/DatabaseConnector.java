package persistence;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zaxxer.hikari.HikariDataSource;

/* This class uses the Singleton pattern */

public class DatabaseConnector {

    private HikariDataSource hikariDataSource;

    public DatabaseConnector(HikariDataSource hikariDataSource) {
        this.hikariDataSource = hikariDataSource;
    }

    // TODO: add more logging
    private static final Logger LOG = LoggerFactory.getLogger(DatabaseConnector.class);

    /**
     * Connects to the database.
     *
     * @return the Connection object if the connection was successful
     * @throws SQLException
     */
    public Connection connect() throws SQLException {

        LOG.info("Connecting to the database");
        return hikariDataSource.getConnection();
    }
}
