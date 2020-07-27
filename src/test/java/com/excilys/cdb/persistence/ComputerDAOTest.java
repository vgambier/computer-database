package com.excilys.cdb.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Properties;

import org.dbunit.DBTestCase;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.excilys.cdb.config.hibernate.HibernateConfig;
import com.excilys.cdb.config.spring.AppConfiguration;
import com.excilys.cdb.config.spring.JdbcConfiguration;
import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.mapper.ComputerDTOMapper;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.ComputerPageTest;
import com.excilys.cdb.service.ComputerService;

public class ComputerDAOTest extends DBTestCase {

    private static AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
            AppConfiguration.class, JdbcConfiguration.class, HibernateConfig.class);
    private ComputerDAO computerDAO;
    private ComputerDTOMapper computerDTOMapper;
    private ComputerService computerService;
    private SessionFactory sessionFactory;

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
        sessionFactory = (SessionFactory) context.getBean("sessionFactoryBean");

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

        Computer newComputer = new Computer.Builder().withName("Computer50")
                .withIntroduced(LocalDate.of(1970, 10, 9))
                .withDiscontinued(LocalDate.of(2039, 01, 01))
                .withCompany(new Company(5, "Company5")).build();

        computerDAO.add(newComputer);

        // Determine max ID currently existing in database

        Session session = sessionFactory.openSession();

        @SuppressWarnings("unchecked")
        Query<Integer> query = session.createQuery("select id from Computer order by id desc");
        query.setMaxResults(1);
        int maxId = query.uniqueResult();

        session.close();

        assertEquals(newComputer, computerDAO.find(maxId));
    }

    @Test
    public void testUpdateComputer() {

        Computer updatedComputer = computerDTOMapper.fromDTOtoModel(new ComputerDTO.Builder()
                .withId("22").withName("Computer22Improved").withIntroduced("1989-05-09")
                .withDiscontinued("2019-01-02").withCompany("1").build());

        computerDAO.update(updatedComputer);

        assertEquals(updatedComputer, computerDAO.find(22));
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