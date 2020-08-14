package com.excilys.cdb.controller;

import com.excilys.cdb.config.HibernateConfig;
import com.excilys.cdb.config.RESTConfiguration;
import com.excilys.cdb.config.RESTWebAppInitializer;
import com.excilys.cdb.exception.ComputerNotFoundException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
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
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

@ContextConfiguration(classes = {RESTWebAppInitializer.class, HibernateConfig.class, RESTConfiguration.class})
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class ,
        TransactionalTestExecutionListener.class})
@DbUnitConfiguration(databaseConnection = "hikariDataSource")
@DatabaseSetup("/dataset.xml")
public class ComputerControllerTest {

    @Autowired
    private ComputerController computerController;

    @Test
    public void test_get_computer_1 () throws ComputerNotFoundException {
        Computer expected = new Computer(1,"Computer1", LocalDate.parse("2010-10-10"),LocalDate.parse("2010-10-12"),new Company(1,"Company1"));
        Computer result = computerController.getComputerJSON("1");

        assertEquals(expected,result);
    }
}
