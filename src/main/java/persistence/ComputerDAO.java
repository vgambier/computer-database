package persistence;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import mapper.ComputerMapper;
import model.Computer;

/* This class uses the Singleton pattern */

public class ComputerDAO {

	private static ComputerDAO INSTANCE = null;

	private ComputerDAO() {
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

		Computer computer = new Computer();

		String sql = "SELECT id, name, introduced, discontinued, company_id  FROM `computer` WHERE id = ?";
		PreparedStatement statement = null;

		try (DatabaseConnection dbConnection = DatabaseConnection.getInstance()) {
			statement = dbConnection.connect().prepareStatement(sql);
			statement.setInt(1, id);

			ResultSet resultSet = statement.executeQuery();

			computer = ComputerMapper.getInstance().makeComputer(resultSet);

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
		PreparedStatement statement = null;

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
		PreparedStatement statement = null;

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
		PreparedStatement statement = null;

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
	 * Returns all computers from the database as Java objects
	 * 
	 * @throws Exception
	 */
	public List<Computer> listAll() throws Exception {

		List<Computer> computers = new ArrayList<Computer>();
		String sql = "SELECT id, name, introduced, discontinued, company_id FROM `computer`";
		PreparedStatement statement = null;

		try (DatabaseConnection dbConnection = DatabaseConnection.getInstance()) {

			statement = dbConnection.connect().prepareStatement(sql);

			// Connecting to the database and executing the query
			ResultSet resultSet = statement.executeQuery();

			computers = ComputerMapper.getInstance().makeComputerList(resultSet);

		} catch (SQLException e) {
			throw new PersistenceException("Couldn't prepare and execute the SQL statement.", e);
		}

		return computers;
	}

	/**
	 * Count the number of entries in the computer database
	 * 
	 * @return the number of entries in the computer database
	 * @throws Exception
	 */
	public int countComputerEntries() throws Exception {

		String sql = "SELECT COUNT(*) FROM `computer`";
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
}
