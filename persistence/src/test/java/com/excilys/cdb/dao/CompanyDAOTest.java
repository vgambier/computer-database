package com.excilys.cdb.dao;

import com.excilys.cdb.config.HibernateConfig;
import com.excilys.cdb.model.Company;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import static org.junit.Assert.assertEquals;


@ContextConfiguration(classes = {HibernateConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class ,
        TransactionalTestExecutionListener.class})
@DbUnitConfiguration(databaseConnection = "hikariDataSource")
@DatabaseSetup("/dataset.xml")
public class CompanyDAOTest  {

    @Autowired
    private CompanyDAO companyDAO;

    @Autowired
    private HikariDataSource hikariDataSource;


    @Test
    @ExpectedDatabase(value = "/CompanyDAOTest/testDelete.xml", table = "company" )
    public void testDelete() {
        companyDAO.delete(6);
    }

    @Test
    public void testListAll() {
        assertEquals(6,companyDAO.listAll().size());
    }



}
