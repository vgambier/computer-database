package com.excilys.cdb.controller;

import com.excilys.cdb.config.*;
import com.excilys.cdb.dto.UserUpdateRoleDTO;
import com.zaxxer.hikari.HikariDataSource;
import org.dbunit.DBTestCase;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.FileInputStream;
import java.sql.Connection;
import java.util.*;

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
        assertFalse(usersController.jdbcUserDetailsManager.loadUserByUsername("testDisable").isEnabled());
        usersController.enableUser("testDisable");
        assertTrue(usersController.jdbcUserDetailsManager.loadUserByUsername("testDisable").isEnabled());
    }

    @Test
    public void test_manage_role(){
        GrantedAuthority roleUser = new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return "ROLE_USER";
            }
        };
        GrantedAuthority roleAdmin = new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return "ROLE_ADMIN";
            }
        };
        UserUpdateRoleDTO userUpdateRoleDTO = new UserUpdateRoleDTO();
        userUpdateRoleDTO.setUserName("user");
        userUpdateRoleDTO.setRoles(new String[]{"ROLE_USER","ROLE_ADMIN"});

        Collection roleBefore = new ArrayList();
        Collection roleAfter = new ArrayList();

        roleBefore.add(roleUser);
        roleAfter.add(roleUser);
        roleAfter.add(roleAdmin);

        assertTrue(usersController.jdbcUserDetailsManager.loadUserByUsername("user").getAuthorities().containsAll(roleBefore));
        assertFalse(usersController.jdbcUserDetailsManager.loadUserByUsername("user").getAuthorities().containsAll(roleAfter));

        usersController.manageRole(userUpdateRoleDTO);

        assertTrue(usersController.jdbcUserDetailsManager.loadUserByUsername("user").getAuthorities().containsAll(roleAfter));

    }
}
