package com.excilys.cdb.dao;

import com.excilys.cdb.config.HibernateConfig;
import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.mapper.ComputerDTOMapper;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.zaxxer.hikari.HikariDataSource;
import com.excilys.cdb.dao.filter.FilterId;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.IDataSet;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

import static org.junit.Assert.*;


@ContextConfiguration(classes = {HibernateConfig.class,ComputerDTOMapper.class, FilterId.class})
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class ,
        TransactionalTestExecutionListener.class})
@DbUnitConfiguration(databaseConnection = "hikariDataSource")
@DatabaseSetup("/dataset.xml")
public class ComputerDAOTest {

    @Autowired
    private ComputerDAO computerDAO;
    @Autowired
    private ComputerDTOMapper computerDTOMapper;
    @Autowired
    private HikariDataSource hikariDataSource;


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
    @ExpectedDatabase(value = "/ComputerDAOTest/testAddComputer.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED,
            columnFilters = FilterId.class)
    public void testAddComputer() {
        Computer newComputer = new Computer.Builder()
                .withName("Computer50")
                .withIntroduced(Date.valueOf("1970-10-09"))
                .withDiscontinued(Date.valueOf("1970-10-09"))
                .withCompany(new Company(5,"Company5")).build();

        computerDAO.add(newComputer);
    }

    @Test
    @ExpectedDatabase(value = "/ComputerDAOTest/testUpdateComputer.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    public void testUpdateComputer() {
        Computer updatedComputer = new Computer.Builder()
                .withId(22)
                .withName("Computer22Improved")
                .withIntroduced(Date.valueOf("1989-05-09"))
                .withDiscontinued(Date.valueOf("2019-01-02"))
                .withCompany(new Company(1,"Company1")).build();

        computerDAO.update(updatedComputer);
    }

    @Test
    @ExpectedDatabase(value = "/ComputerDAOTest/testDeleteComputer.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    public void testDeleteComputer() {
        computerDAO.delete(computerDAO.find(3));
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
