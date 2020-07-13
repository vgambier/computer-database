package persistence;

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

    public HikariDataSource getHikariDataSource() {
        return hikariDataSource;
    }
}
