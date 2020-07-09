package persistence;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mapper.Mapper;
import mapper.MapperException;

public abstract class DAO<T> {

    protected DatabaseConnector databaseConnector;

    public DAO(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

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
     * @throws PersistenceException
     */
    public int countEntries() throws PersistenceException {
        return countEntriesWhere("");
    }

    /**
     * Count the number of entries in the database that match the search term. Works for both the
     * computer database and the company database.
     *
     * @param searchTerm
     *            the search term - an entry match if it contains this string anywhere in its name
     *            or its company name
     * @return the number of entries in the database
     * @throws PersistenceException
     */
    public int countEntriesWhere(String searchTerm) throws PersistenceException {

        String sql = getCountEntriesWhereSQLStatement();

        int nbEntries = -1; // The only way the "if" fails is if the query
                            // fails, but an exception will be thrown anyway

        try (Connection connection = databaseConnector.connect();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, "%" + searchTerm + "%");
            statement.setString(2, "%" + searchTerm + "%");

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    nbEntries = rs.getInt(1);
                }
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

        String sql = getListAllSQLStatement();

        try (Connection connection = databaseConnector.connect();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                models.add(getTypeMapper().toModel(resultSet));
            }

        } catch (SQLException e) {
            throw new PersistenceException("Couldn't prepare and execute the SQL statement.", e);
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
     * @throws IOException
     * @throws PersistenceException
     * @throws Exception
     */
    public boolean doesEntryExist(int id) throws PersistenceException {

        boolean doesEntryExist = false;

        // SQL injection is impossible: the user has no control over tableName
        String sql = "SELECT COUNT(1) FROM " + getTableName() + " WHERE id = ?";

        try (Connection connection = databaseConnector.connect();
                PreparedStatement statement = connection.prepareStatement(sql)) {

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

    protected abstract String getTableName();

    protected abstract String getListAllSQLStatement();

    protected abstract String getCountEntriesWhereSQLStatement();

}
