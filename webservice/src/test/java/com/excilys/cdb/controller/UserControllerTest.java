package com.excilys.cdb.controller;

import com.excilys.cdb.config.HibernateConfig;
import com.excilys.cdb.config.RESTConfiguration;
import com.excilys.cdb.config.RESTWebAppInitializer;
import com.excilys.cdb.dto.UserUpdateRoleDTO;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;


@ContextConfiguration(classes = {RESTWebAppInitializer.class,HibernateConfig.class,RESTConfiguration.class})
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class ,
        TransactionalTestExecutionListener.class})
@DbUnitConfiguration(databaseConnection = "hikariDataSource")
@DatabaseSetup("/dataset.xml")
public class UserControllerTest {

    @Autowired
    private UsersController usersController;


    @Test
    @ExpectedDatabase(value = "/UsersControllerTest/test_enable_user.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    public void test_enable_user(){
        usersController.enableUser("testDisable");
    }

    @Test
    @ExpectedDatabase(value = "/UsersControllerTest/test_manage_role.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    public void test_manage_role(){
        UserUpdateRoleDTO userUpdateRoleDTO = new UserUpdateRoleDTO();
        userUpdateRoleDTO.setUserName("user");
        userUpdateRoleDTO.setRoles(new String[]{"ROLE_USER","ROLE_ADMIN"});

        usersController.manageRole(userUpdateRoleDTO);
    }


}
