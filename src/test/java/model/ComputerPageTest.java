package model;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

import org.dbunit.DBTestCase;
import org.dbunit.DatabaseUnitException;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import config.AppConfiguration;
import config.JdbcConfiguration;
import persistence.DatabaseConnector;
import service.Service;

// TODO: add more unit tests everywhere

public class ComputerPageTest extends DBTestCase {

    public ComputerPageTest(String name) throws IOException {

        super(name);

        InputStream inputStream = DatabaseConnector.class.getResourceAsStream("/.properties");
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
    }

    @Test
    public void testDataLoaded() throws Exception {
        IDataSet dataSet = getDatabaseDataSet();
        assertNotNull(dataSet);

        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfiguration.class,
                JdbcConfiguration.class);
        Service service = (Service) context.getBean("serviceBean");

        double rowCount = service.countComputerEntries();
        assertEquals(8.0, rowCount);

        ((ConfigurableApplicationContext) context).close();

    }

    @Test(expected = ModelException.class)
    public void invalidNegativeIDConstructor() throws Exception {
        new ComputerPage(15, -1); // Assuming there are 15 entries
    }

    @Test(expected = ModelException.class)
    public void invalidBigIDConstructor() throws Exception {
        new ComputerPage(15, Integer.MAX_VALUE); // Assuming there are 15 entries
    }

    @Override
    protected IDataSet getDataSet() throws Exception {
        return new FlatXmlDataSetBuilder()
                .build(ComputerPageTest.class.getResourceAsStream("/db-setup/dataset.xml"));
    }

    @Override
    protected DatabaseOperation getSetUpOperation()
            throws DatabaseUnitException, SQLException, Exception {
        DatabaseOperation.CLEAN_INSERT.execute(getConnection(), getDataSet());
        return DatabaseOperation.NONE;
    }

    protected QueryDataSet getDatabaseDataSet() throws Exception {
        QueryDataSet loadedDataSet = new QueryDataSet(getConnection());
        loadedDataSet.addTable("company");
        return loadedDataSet;
    }

}
