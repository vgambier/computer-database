package persistence;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import mapper.ComputerMapper;
import model.Computer;

/* This class uses the Singleton pattern */

public class ComputerDAO extends DAO<Computer> {

	private static ComputerDAO INSTANCE = null;

	private ComputerDAO() {
		super();
	}

	// Singleton instance getter
	public static ComputerDAO getInstance() {
		if (INSTANCE == null)
			INSTANCE = new ComputerDAO();
		return INSTANCE;
	}

	/**
	 * Finds a computer in the database, and returns a corresponding Java object
	 * 
	 * @param id
	 *            the id of the computer in the database
	 * @return a Computer object, with the same attributes as the computer entry
	 *         in the database
	 * @throws Exception
	 */
	public Computer find(int id) throws Exception {

		Computer computer;

		String sql = "SELECT id, name, introduced, discontinued, company_id  FROM `computer` WHERE id = ?";
		PreparedStatement statement;

		try (DatabaseConnection dbConnection = DatabaseConnection.getInstance()) {
			statement = dbConnection.connect().prepareStatement(sql);
			statement.setInt(1, id);
			ResultSet resultSet = statement.executeQuery();
			computer = ComputerMapper.getInstance().toModel(resultSet);

		} catch (SQLException e) {
			throw new PersistenceException("Couldn't prepare and execute the SQL statement.", e);
		}

		return computer;
	}

	/**
	 * Adds an entry for a new computer
	 * 
	 * @param computerName
	 *            the name of the new computer - cannot be null
	 * @param introducedDate
	 *            the date of introduction of the new computer - may be null
	 * @param discontinuedDate
	 *            the date of discontinuation of the new computer - may be null
	 * @param companyID
	 *            the ID of the company of the new computer - may be null
	 * @throws Exception
	 */
	public void add(String computerName, Date introducedDate, Date discontinuedDate, Integer companyID)
			throws Exception {

		String sql = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES (?, ?, ?, ?)";
		PreparedStatement statement;

		// Converting to dates

		try (DatabaseConnection dbConnection = DatabaseConnection.getInstance()) {
			statement = dbConnection.connect().prepareStatement(sql);
			statement.setString(1, computerName);
			statement.setDate(2, introducedDate); // possibly null
			statement.setDate(3, discontinuedDate); // possibly null

			if (companyID == null)
				statement.setNull(4, java.sql.Types.INTEGER);
			else
				statement.setInt(4, companyID);

			statement.executeUpdate();

		} catch (SQLException e) {
			throw new PersistenceException("Couldn't prepare and execute the SQL statement.", e);
		}

		System.out.println("Entry added.");
	}

	/**
	 * Adds an entry for a new computer
	 * 
	 * @param id
	 *            the id of the existing computer
	 * @param newComputerName
	 *            the new name of the computer - cannot be null
	 * @param newIntroducedDate
	 *            the new date of introduction of the computer - may be null
	 * @param newDiscontinuedDate
	 *            the new date of discontinuation of the computer - may be null
	 * @param newCompanyID
	 *            the new ID of the company of the computer - may be null
	 * @throws Exception
	 */
	public void update(int id, String newComputerName, Date newIntroducedDate, Date newDiscontinuedDate,
			Integer newCompanyID) throws Exception {

		String sql = "UPDATE computer SET name = ?, introduced = ?, discontinued = ?, company_id = ? WHERE id = ?";
		PreparedStatement statement;

		// Converting to dates

		try (DatabaseConnection dbConnection = DatabaseConnection.getInstance()) {
			statement = dbConnection.connect().prepareStatement(sql);
			statement.setString(1, newComputerName);
			statement.setDate(2, newIntroducedDate); // possibly null
			statement.setDate(3, newDiscontinuedDate); // possibly null

			if (newCompanyID == null)
				statement.setNull(4, java.sql.Types.INTEGER);
			else
				statement.setInt(4, newCompanyID);

			statement.setInt(5, id);

			statement.executeUpdate();

		} catch (SQLException e) {
			throw new PersistenceException("Couldn't prepare and execute the SQL statement.", e);
		}

		System.out.println("Entry updated.");

	}

	/**
	 * Deletes the entry of the given computer
	 * 
	 * @param id
	 *            the id of the relevant computer
	 * @throws Exception
	 */
	public void delete(int id) throws Exception {

		String sql = "DELETE FROM `computer` WHERE id = ?";
		PreparedStatement statement;

		try (DatabaseConnection dbConnection = DatabaseConnection.getInstance()) {
			statement = dbConnection.connect().prepareStatement(sql);
			statement.setInt(1, id);
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new PersistenceException("Couldn't prepare and execute the SQL statement.", e);
		}

		System.out.println("Entry deleted.");

	}

	/**
	 * Checks if there is an entry of the given id number in the computer
	 * database
	 * 
	 * @param id
	 *            the id of the entry to be checked
	 * @return true if and only if there is an entry
	 * @throws Exception
	 */
	public boolean doesEntryExist(int id) throws Exception {

		boolean doesEntryExist = false;

		String sql = "SELECT COUNT(1) FROM `computer` WHERE id = ?";
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

	@Override
	public ComputerMapper getTypeMapper() {
		return ComputerMapper.getInstance();
	}

}
