package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariDataSource;

import persistence.DatabaseConnector;
import persistence.PersistenceException;

@Configuration
public class JdbcConfiguration {

    private static DatabaseConnector databaseConnector;

    @Bean
    public HikariDataSource hikariDataSource() throws PersistenceException {

        InputStream inputStream = DatabaseConnector.class.getResourceAsStream("/.properties");
        Properties properties = new Properties();

        try {
            properties.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            throw new PersistenceException("Couldn't find the database login .properties file.", e);
        }

        String databaseURL = properties.getProperty("DATABASE_URL");
        String username = properties.getProperty("USERNAME");
        String password = properties.getProperty("PASSWORD");

        Logger LOG = LoggerFactory.getLogger(DatabaseConnector.class);
        LOG.info("Initializing Hikari DataSource to database:\nURL: " + databaseURL + "\nUsername: "
                + username);

        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(databaseURL);
        hikariDataSource.setUsername(username);
        hikariDataSource.setPassword(password);
        hikariDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariDataSource.setMaximumPoolSize(10);

        return hikariDataSource;
    }

    @Bean(name = "databaseConnectorBean")
    public DatabaseConnector databaseConnector() throws PersistenceException {

        if (databaseConnector == null) {
            databaseConnector = new DatabaseConnector(hikariDataSource());
        }
        return databaseConnector;
    }

}