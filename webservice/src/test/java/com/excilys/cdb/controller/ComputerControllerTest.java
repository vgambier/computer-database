package com.excilys.cdb.controller;

import com.excilys.cdb.config.HibernateConfig;
import com.excilys.cdb.config.RESTConfiguration;
import com.excilys.cdb.config.RESTWebAppInitializer;
import com.excilys.cdb.exception.ComputerNotFoundException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.zaxxer.hikari.HikariDataSource;
import org.dbunit.DBTestCase;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.FileInputStream;
import java.time.LocalDate;

@ContextConfiguration(classes = {RESTWebAppInitializer.class, HibernateConfig.class, RESTConfiguration.class})
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class ComputerControllerTest extends DBTestCase {

    @Autowired
    private HikariDataSource hikariDataSource;

    @Autowired
    private ComputerController computerController;

    @Override
    protected IDataSet getDataSet() throws Exception {
        try(FileInputStream fileInputStream = new FileInputStream("src/test/resources/dataset.xml")){
            return new FlatXmlDataSetBuilder().build(fileInputStream);
        }
    }

    @Test
    public void test_get_computer_1 () throws ComputerNotFoundException {
        Computer expected = new Computer(1,"Computer1", LocalDate.parse("2010-10-10"),LocalDate.parse("2010-10-12"),new Company(1,"Company1"));
        Computer result = computerController.getComputerJSON("1");

        assertEquals(expected,result);
    }
}
