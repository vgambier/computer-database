package com.excilys.cdb.dao;

import com.excilys.cdb.config.HibernateConfig;
import com.excilys.cdb.model.Company;
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

import java.io.FileInputStream;
import java.sql.Connection;


@ContextConfiguration(classes = {HibernateConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class CompanyDAOTest extends DBTestCase {

    @Autowired
    private CompanyDAO companyDAO;

    @Autowired
    private HikariDataSource hikariDataSource;

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
    public void testDelete() {
        assertEquals(6, companyDAO.countEntries());
        Company toBeDeleted=companyDAO.find(6);
        companyDAO.delete(toBeDeleted);
        assertEquals(companyDAO.countEntries(),5);
        assertNull(companyDAO.find(6));
    }

    @Test
    public void testListAll() {
        assertEquals(6,companyDAO.listAll().size());
    }



}
