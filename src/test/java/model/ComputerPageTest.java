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

import persistence.ComputerDAO;
import persistence.DatabaseConnector;

public class ComputerPageTest extends DBTestCase {

    // TODO: Implement DBUnit to avoid querying the live database.

    public ComputerPageTest(String name) throws IOException {

        super(name);

        InputStream inputStream = DatabaseConnector.class
                .getResourceAsStream("/db-setup/.properties");
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
        double rowCount = ComputerDAO.getInstance().countEntries();
        assertEquals(8.0, rowCount);
    }

    @Test(expected = ModelException.class)
    public void invalidNegativeIDConstructor() throws Exception {
        new ComputerPage(-1);
    }

    @Test(expected = ModelException.class)
    public void invalidBigIDConstructor() throws Exception {
        new ComputerPage(Integer.MAX_VALUE);
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
