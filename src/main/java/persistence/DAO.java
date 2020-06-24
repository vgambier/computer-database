package persistence;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import mapper.Mapper;
import mapper.MapperException;

public abstract class DAO<T> {

    protected static String tableName;

    /**
     * Returns a Mapper<T> object where T is the the same T as the one in the current DAO<T> type.
     * For example, if this method is called by an instance of ComputerDAO, it should return an
     * instance of ComputerMapper
     *
     * @return a Mapper<T> object
     */
    public abstract Mapper<T> getTypeMapper();

    /**
     * Count the number of entries in the database. Works for both the computer database and the
     * company database.
     *
     * @return the number of entries in the database
     * @throws Exception
     */
    public int countEntries() throws Exception {

        // SQL injection is impossible: the user has no control over tableName
        if (tableName == null) {
            throw new PersistenceException("Name may not be null");
        }
        String sql = "SELECT COUNT(*) FROM " + tableName;

        int nbEntries = -1; // The only way the "if" fails is if the query
                            // fails, but an exception will be thrown anyway

        try (DatabaseConnector dbConnector = DatabaseConnector.getInstance();
                Statement statement = dbConnector.connect().prepareStatement(sql);
                ResultSet rs = statement.executeQuery(sql)) {

            if (rs.next()) {
                nbEntries = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new PersistenceException("Couldn't prepare and execute the SQL statement.", e);
        }

        return nbEntries;
    }

    /**
     * Returns all entries from the database as Java objects.
     *
     * @return the list of Java objects
     * @throws PersistenceException
     */
    public List<T> listAll() throws PersistenceException {

        List<T> models = new ArrayList<T>();

        // SQL injection is impossible: the user has no control over tableName
        if (tableName == null) {
            throw new PersistenceException("Name may not be null");
        }
        String sql = "SELECT * FROM " + tableName;

        try (DatabaseConnector dbConnector = DatabaseConnector.getInstance();
                PreparedStatement statement = dbConnector.connect().prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                models.add(getTypeMapper().toModel(resultSet));
            }

        } catch (SQLException e) {
            throw new PersistenceException("Couldn't prepare and execute the SQL statement.", e);
        } catch (IOException e) {
            throw new PersistenceException("Couldn't find the database login .properties file.", e);
        } catch (MapperException e) {
            throw new PersistenceException("Couldn't map the database entries to model!", e);
        }

        return models;
    }

    /**
     * Checks if there is an entry of the given id number in the table.
     *
     * @param id
     *            the id of the entry to be checked
     * @return true if and only if there is an entry
     * @throws Exception
     */
    public boolean doesEntryExist(int id) throws Exception {

        boolean doesEntryExist = false;

        // SQL injection is impossible: the user has no control over tableName
        if (tableName == null) {
            throw new PersistenceException("Name may not be null");
        }
        String sql = "SELECT COUNT(1) FROM " + tableName + " WHERE id = ?";

        try (DatabaseConnector dbConnector = DatabaseConnector.getInstance();
                PreparedStatement statement = dbConnector.connect().prepareStatement(sql)) {

            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                // true if the query returned one entry, since the result set returned either 0 or 1
                // entry
                doesEntryExist = resultSet.next() && resultSet.getInt(1) == 1;
            } catch (SQLException e) {
                throw new PersistenceException("Couldn't execute the SQL statement.", e);
            }

        } catch (SQLException e) {
            throw new PersistenceException("Couldn't prepare the SQL statement.", e);
        }
        return doesEntryExist;
    }
}
