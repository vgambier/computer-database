package com.excilys.cdb.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.excilys.cdb.dao.PersistenceException;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
@ComponentScan("com.excilys.cdb.dao")
public class HibernateConfig {

    private static final Logger LOG = LoggerFactory.getLogger(HibernateConfig.class);

    @Bean("hikariDataSource")
    public HikariDataSource hikariDataSource() throws PersistenceException {

        InputStream inputStream = HibernateConfig.class.getResourceAsStream("/.properties");
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

        // TODO: add more logging
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

    @Bean("sessionFactoryBean")
    public LocalSessionFactoryBean sessionFactory(HikariDataSource hikariDataSource) {

        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(hikariDataSource);
        sessionFactory.setPackagesToScan("com.excilys.cdb.model");

        return sessionFactory;
    }

    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {

        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory);

        return txManager;
    }

}