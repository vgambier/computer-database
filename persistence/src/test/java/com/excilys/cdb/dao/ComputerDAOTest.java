package com.excilys.cdb.dao;

import com.excilys.cdb.config.HibernateConfig;
import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.mapper.ComputerDTOMapper;
import com.excilys.cdb.model.Computer;
import com.zaxxer.hikari.HikariDataSource;
import org.dbunit.DBTestCase;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileInputStream;
import java.sql.Connection;
import java.util.List;

@ContextConfiguration(classes = {HibernateConfig.class,ComputerDTOMapper.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class ComputerDAOTest extends DBTestCase {

    @Autowired
    private ComputerDAO computerDAO;
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private ComputerDTOMapper computerDTOMapper;
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
    public void testFindComputer() {

        Computer foundComputer = computerDAO.find(999);

        CompanyDTO companyDTO = new CompanyDTO.Builder().withId("5").withName("Company5").build();

        Computer expectedComputer = computerDTOMapper.fromDTOtoModel(new ComputerDTO.Builder()
                .withId("999").withName("Computer999").withIntroduced("1980-11-11")
                .withDiscontinued("2099-01-01").withCompany(companyDTO).build());

        assertEquals(expectedComputer, foundComputer);
    }

    @Test
    public void testAddComputer() {

        CompanyDTO companyDTO = new CompanyDTO.Builder().withId("5").withName("Company5").build();

        Computer newComputer = computerDTOMapper.fromDTOtoModel(
                (new ComputerDTO.Builder().withName("Computer50").withIntroduced("1970-10-09")
                        .withDiscontinued("2039-01-01").withCompany(companyDTO).build()));

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

        CompanyDTO companyDTO = new CompanyDTO.Builder().withId("1").withName("Company1").build();

        Computer updatedComputer = computerDTOMapper.fromDTOtoModel((new ComputerDTO.Builder()
                .withId("22").withName("Computer22Improved").withIntroduced("1989-05-09")
                .withDiscontinued("2019-01-02").withCompany(companyDTO).build()));

        computerDAO.update(updatedComputer);

        assertEquals(updatedComputer, computerDAO.find(22));
    }

    @Test
    public void testDeleteComputer() {
        assertEquals(10, computerDAO.countEntries());
        Computer toBeDeleted=computerDAO.find(3);
        computerDAO.delete(toBeDeleted);
        assertEquals(9,computerDAO.countEntries());
        assertNull(computerDAO.find(3));
    }

    @Test
    public void testFindMatchesWithinRange() throws PersistenceException {
        int limit=20;
        int offset=0;
        String searchTerm="mac";
        String orderBy="introduced";
        List<Computer> actual=computerDAO.findMatchesWithinRange(limit, offset,searchTerm,orderBy);
        assertEquals(2,actual.size());
    }

    @Test(expected = PersistenceException.class)
    public void testFindMatchesThrowsException() throws PersistenceException {
        int limit=20;
        int offset=0;
        String searchTerm="mac";
        String orderBy="lol";
        computerDAO.findMatchesWithinRange(limit, offset, searchTerm, orderBy);
    }

    @Test
    public void testDataLoaded() throws Exception {
        IDataSet dataSet = getDatabaseDataSet();
        assertNotNull(dataSet);

        int rowCount = computerDAO.countEntries();
        assertEquals(10, rowCount);
    }



    private QueryDataSet getDatabaseDataSet() throws Exception {
        try(Connection connection = hikariDataSource.getConnection()){
            QueryDataSet loadedDataSet = new QueryDataSet(new DatabaseConnection(connection));
            loadedDataSet.addTable("company");
            return loadedDataSet;
        }


    }
}
