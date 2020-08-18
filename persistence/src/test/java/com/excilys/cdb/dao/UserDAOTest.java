package com.excilys.cdb.dao;

import com.excilys.cdb.config.HibernateConfig;
import com.excilys.cdb.dao.filter.FilterId;
import com.excilys.cdb.dao.filter.FilterPassword;
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


@ContextConfiguration(classes = {HibernateConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class ,
        TransactionalTestExecutionListener.class})
@DbUnitConfiguration(databaseConnection = "hikariDataSource")
@DatabaseSetup("/dataset.xml")
public class UserDAOTest {

    @Autowired
    private UserDAO userDAO;


    @Test
    @ExpectedDatabase(value = "/UsersDaoTest/setEnable/test_enable_user.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    public void test_enable_user(){
        userDAO.setEnable("userDisable","1");
    }

    @Test
    @ExpectedDatabase(value = "/UsersDaoTest/setEnable/test_disable_user.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    public void test_disable_user(){
        userDAO.setEnable("userEnable","0");
    }

    @Test
    @ExpectedDatabase(value = "/UsersDaoTest/manageRole/test_user_GET_ROLE_ADMIN.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    public void test_user_GET_ROLE_ADMIN(){
        userDAO.manageRole("user",new String[] {"ROLE_USER","ROLE_ADMIN"});
    }

    @Test
    @ExpectedDatabase(value = "/UsersDaoTest/manageRole/test_admin_GET_ONLY_ROLE_TEST.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    public void test_admin_GET_ONLY_ROLE_TEST(){
        userDAO.manageRole("admin",new String[] {"ROLE_TEST"});
    }

    @Test
    @ExpectedDatabase(value = "/UsersDaoTest/add/add_user_toto.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT,
            columnFilters = {FilterId.class,FilterPassword.class})
    public void test_add_user_toto(){
        userDAO.add("toto","toto");
    }

}
