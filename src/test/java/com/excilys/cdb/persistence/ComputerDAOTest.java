package com.excilys.cdb.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.dbunit.DBTestCase;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.excilys.cdb.config.hibernate.HibernateConfig;
import com.excilys.cdb.config.spring.AppConfiguration;
import com.excilys.cdb.config.spring.JdbcConfiguration;
import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.mapper.ComputerDTOMapper;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.ComputerPageTest;
import com.excilys.cdb.service.ComputerService;

public class ComputerDAOTest extends DBTestCase {

    private static AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
            AppConfiguration.class, JdbcConfiguration.class, HibernateConfig.class);
    private ComputerDAO computerDAO;
    private ComputerDTOMapper computerDTOMapper;
    private ComputerService computerService;

    public ComputerDAOTest(String name) throws IOException {

        super(name);

        InputStream inputStream = ComputerPageTest.class.getResourceAsStream("/.properties");
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

        computerDAO = (ComputerDAO) context.getBean("computerDAOBean");
        computerDTOMapper = (ComputerDTOMapper) context.getBean("computerDTOMapperBean");
        computerService = (ComputerService) context.getBean("computerServiceBean");

    }

    @Test
    public void testFindComputer() {

        Computer foundComputer = computerDAO.find(999);

        Computer expectedComputer = computerDTOMapper.fromDTOtoModel(new ComputerDTO.Builder()
                .withId("999").withName("Computer999").withIntroduced("1980-11-11")
                .withDiscontinued("2099-01-01").withCompany("5").build());

        assertEquals(expectedComputer, foundComputer);

    }

    @Test
    public void testAddComputer() {

        Computer newComputer = computerDTOMapper.fromDTOtoModel(new ComputerDTO.Builder()
                .withId("50").withName("Computer50").withIntroduced("1970-10-09")
                .withDiscontinued("2039-01-01").withCompany("5").build());

        computerDAO.add(newComputer); // TODO: method works, but test fails?

        assertEquals(newComputer, computerDAO.find(50));

    }

    @Test
    public void testDataLoaded() throws Exception {
        IDataSet dataSet = getDatabaseDataSet();
        assertNotNull(dataSet);

        int rowCount = computerService.countComputerEntries();
        assertEquals(8, rowCount);
    }

    @Override
    protected IDataSet getDataSet() throws Exception {
        return new FlatXmlDataSetBuilder()
                .build(ComputerDAOTest.class.getResourceAsStream("/db-setup/dataset.xml"));
    }

    protected QueryDataSet getDatabaseDataSet() throws Exception {
        QueryDataSet loadedDataSet = new QueryDataSet(getConnection());
        loadedDataSet.addTable("company");
        return loadedDataSet;
    }
}