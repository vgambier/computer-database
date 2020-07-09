package persistence;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zaxxer.hikari.HikariDataSource;

@Component
public class DatabaseConnector {

    private HikariDataSource hikariDataSource;

    @Autowired
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
