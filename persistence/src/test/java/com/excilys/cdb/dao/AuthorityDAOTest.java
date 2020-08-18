package com.excilys.cdb.dao;

import com.excilys.cdb.config.HibernateConfig;
import com.excilys.cdb.model.Authority;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.util.List;

import static org.junit.Assert.assertEquals;

@ContextConfiguration(classes = {HibernateConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class ,
        TransactionalTestExecutionListener.class})
@DbUnitConfiguration(databaseConnection = "hikariDataSource")
@DatabaseSetup("/dataset.xml")
public class AuthorityDAOTest {

    @Autowired
    private AuthorityDAO authorityDAO;

    @Test
    public void test_listAll (){
        List<Authority> authoritySet = authorityDAO.listAll();

        assertEquals(authoritySet.get(0).getAuthority(), "ROLE_ADMIN");
        assertEquals(authoritySet.get(1).getAuthority(), "ROLE_USER");
        assertEquals(authoritySet.get(2).getAuthority(), "ROLE_TEST");
    }
}
