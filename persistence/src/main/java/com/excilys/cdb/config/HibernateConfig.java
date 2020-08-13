package com.excilys.cdb.config;

import com.excilys.cdb.dao.PersistenceException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
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

import javax.sql.DataSource;

/**
 * @author Victor Gambier
 *
 *         This configuration class is responsible for creating and configuring sessions. A session
 *         requires a data source
 */
@Configuration
@EnableTransactionManagement
@ComponentScan("com.excilys.cdb.dao")
public class HibernateConfig {

    private static final Logger LOG = LoggerFactory.getLogger(HibernateConfig.class);

    @Bean("hikariDataSource")
    public HikariDataSource hikariDataSource() throws PersistenceException {
        HikariConfig hikariConfig = new HikariConfig("/.properties");
        hikariConfig.setMaximumPoolSize(10);
        return new HikariDataSource(hikariConfig);
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
