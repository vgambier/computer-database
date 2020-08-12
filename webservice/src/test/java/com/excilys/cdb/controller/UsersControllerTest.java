package com.excilys.cdb.controller;

import com.excilys.cdb.config.*;
import com.zaxxer.hikari.HikariDataSource;
import org.dbunit.DBTestCase;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.FileInputStream;
import java.sql.Connection;

@ContextConfiguration(classes = {RESTWebAppInitializer.class,HibernateConfig.class,RESTConfiguration.class})
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class UsersControllerTest extends DBTestCase {

    @Autowired
    private HikariDataSource hikariDataSource;

    @Autowired
    private UsersController usersController;

    @Override
    protected IDataSet getDataSet() throws Exception {
        try(FileInputStream fileInputStream = new FileInputStream("src/test/resources/dataset.xml")){
            return new FlatXmlDataSetBuilder().build(fileInputStream);
        }
    }

    @Before
    public void setUp() throws Exception {
        try(Connection connection = hikariDataSource.getConnection()){
            getSetUpOperation().execute(new DatabaseConnection(connection), getDataSet());
        }
    }

    @Test
    public void test1 (){
        assertEquals(true,true);
    }


    @Test
    public void test_enable_user(){
        assertEquals(false, usersController.jdbcUserDetailsManager.loadUserByUsername("testDisable").isEnabled());
        usersController.enableUser("testDisable");
        assertEquals(true, usersController.jdbcUserDetailsManager.loadUserByUsername("testDisable").isEnabled());
    }
}
