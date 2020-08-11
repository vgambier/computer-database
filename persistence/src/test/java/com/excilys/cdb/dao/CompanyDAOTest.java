package com.excilys.cdb.dao;

import com.excilys.cdb.config.HibernateConfig;
import com.excilys.cdb.model.Company;
import org.dbunit.DBTestCase;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class CompanyDAOTest extends DBTestCase {


    private static AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
            HibernateConfig.class);
    private CompanyDAO companyDAO;

    public CompanyDAOTest(String name) throws IOException {

        super(name);

        InputStream inputStream = CompanyDAOTest.class.getResourceAsStream("/.properties");
        Properties properties = new Properties();
        properties.load(inputStream);
        inputStream.close();

        String databaseURL = properties.getProperty("DATABASE_URL");
        String username = properties.getProperty("USERNAME");
        String password = properties.getProperty("PASSWORD");

        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS,
                "com.mysql.cj.jdbc.Driver");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL, databaseURL);
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME, username);
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD, password);

        // Beans
        companyDAO = (CompanyDAO) context.getBean("companyDAOBean");

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

    @Override
    protected IDataSet getDataSet() throws Exception {
        return new FlatXmlDataSetBuilder()
                .build(CompanyDAOTest.class.getResourceAsStream("/dataset.xml"));
    }

}
