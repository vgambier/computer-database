package persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import mapper.Mapper;

public abstract class DAO<T> {

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

		// TODO: can we fix this somehow?
		String sql;
		if (this instanceof ComputerDAO)
			sql = "SELECT COUNT(*) FROM `computer`";
		else if (this instanceof CompanyDAO)
			sql = "SELECT COUNT(*) FROM `company`";
		else
			throw new PersistenceException("DAO only has 2 children classes: ComputerDAO and CompanyDAO");

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

		// TODO: can we fix this somehow?
		String sql;
		if (this instanceof ComputerDAO)
			sql = "SELECT id, name, introduced, discontinued, company_id FROM `computer`";
		else if (this instanceof CompanyDAO)
			sql = "SELECT id, name FROM `company`";
		else
			throw new PersistenceException("DAO only has 2 children classes: ComputerDAO and CompanyDAO");

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
}
