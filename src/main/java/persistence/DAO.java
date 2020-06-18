package persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import mapper.Mapper;

public abstract class DAO<T> {

	protected static String tableName;

	/**
	 * Returns a Mapper<T> object where T is the the same T as the one in the
	 * current DAO<T> type. For example, if this method is called by an instance
	 * of ComputerDAO, it should return an instance of ComputerMapper
	 * 
	 * @return a Mapper<T> object
	 */
	public abstract Mapper<T> getTypeMapper();

	/**
	 * Count the number of entries in the database. Works for both the computer
	 * database and the company database
	 * 
	 * @return the number of entries in the database
	 * @throws Exception
	 */
	public int countEntries() throws Exception {

		if (tableName == null)
			throw new PersistenceException("Name may not be null");
		String sql = "SELECT COUNT(*) FROM " + tableName; // SQL injection is impossible: the user has no control over tableName

		Statement statement;
		int nbEntries = -1; // The only way the "if" fails is if the query fails, but an exception will be thrown anyway

		try (DatabaseConnection dbConnection = DatabaseConnection.getInstance()) {
			statement = dbConnection.connect().prepareStatement(sql);
			ResultSet rs = statement.executeQuery(sql);
			if (rs.next())
				nbEntries = rs.getInt(1);
		} catch (SQLException e) {
			throw new PersistenceException("Couldn't prepare and execute the SQL statement.", e);
		}

		return nbEntries;
	}

	/**
	 * Returns all entries from the database as Java objects
	 * 
	 * @throws Exception
	 */
	public List<T> listAll() throws Exception {

		List<T> computers = new ArrayList<T>();

		if (tableName == null)
			throw new PersistenceException("Name may not be null");
		String sql = "SELECT * FROM " + tableName; // SQL injection is impossible: the user has no control over tableName

		PreparedStatement statement;
		try (DatabaseConnection dbConnection = DatabaseConnection.getInstance()) {

			statement = dbConnection.connect().prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();

			computers = getTypeMapper().toModelList(resultSet);

		} catch (SQLException e) {
			throw new PersistenceException("Couldn't prepare and execute the SQL statement.", e);
		}

		return computers;
	}

	/**
	 * Checks if there is an entry of the given id number in the table
	 * 
	 * @param id
	 *            the id of the entry to be checked
	 * @return true if and only if there is an entry
	 * @throws Exception
	 */
	public boolean doesEntryExist(int id) throws Exception {

		boolean doesEntryExist = false;

		if (tableName == null)
			throw new PersistenceException("Name may not be null");
		String sql = "SELECT COUNT(1) FROM " + tableName + " WHERE id = ?"; // SQL injection is impossible: the user has no control over tableName

		PreparedStatement statement;

		try (DatabaseConnection dbConnection = DatabaseConnection.getInstance()) {
			statement = dbConnection.connect().prepareStatement(sql);
			statement.setInt(1, id);
			ResultSet resultSet = statement.executeQuery(); // returns either 0 or 1 entry

			doesEntryExist = resultSet.next() && resultSet.getInt(1) == 1; // true if the query returned one entry

		} catch (SQLException e) {
			throw new PersistenceException("Couldn't prepare and execute the SQL statement.", e);
		}
		return doesEntryExist;
	}
}
