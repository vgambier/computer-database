package persistence;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.Computer;
import service.CDBException;
import service.DatabaseConnection;

/* This class uses the Singleton pattern */

public class ComputerDAO {

	private static ComputerDAO INSTANCE = new ComputerDAO();
	private Connection connection;

	public ComputerDAO() {
		connection = new DatabaseConnection().connect();
	}

	/**
	 * Finds a computer in the database, and returns a corresponding Java object
	 * 
	 * @param id
	 *            the id of the computer in the database
	 * @return a Computer object, with the same attributes as the computer entry
	 *         in the database
	 * @throws CDBException
	 */
	public Computer find(int id) throws CDBException {

		if (connection == null)
			throw new CDBException(
					"The DAO's connection attribute must be initialized via setConnection(Connection) method!");

		Computer computer = new Computer();

		String sql = "SELECT * FROM `computer` WHERE id = ?";
		PreparedStatement statement = null;

		try {
			statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
		} catch (SQLException e) {
			throw new CDBException("Couldn't prepare the SQL statement!");
		}

		try {
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.first()) {
				computer = new Computer(id, resultSet.getString("name"), resultSet.getDate("introduced"),
						resultSet.getDate("discontinued"), resultSet.getInt("company_id"));
			}

		} catch (SQLException e) {
			throw new CDBException("Couldn't execute the query!");
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
	 * @throws CDBException
	 */
	public void add(String computerName, Date introducedDate, Date discontinuedDate, Integer companyID)
			throws CDBException {

		if (connection == null)
			throw new CDBException(
					"The DAO's connection attribute must be initialized via setConnection(Connection) method!");

		String sql = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES (?, ?, ?, ?)";
		PreparedStatement statement = null;

		// Converting to dates

		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, computerName);
			statement.setDate(2, introducedDate); // possibly null
			statement.setDate(3, discontinuedDate); // possibly null

			if (companyID == null)
				statement.setNull(4, java.sql.Types.INTEGER);
			else
				statement.setInt(4, companyID);
		} catch (SQLException e) {
			throw new CDBException("Couldn't prepare the SQL statement!");
		}

		try {
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new CDBException("Couldn't execute the query!");
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
	 * @throws CDBException
	 */
	public void update(int id, String newComputerName, Date newIntroducedDate, Date newDiscontinuedDate,
			Integer newCompanyID) throws CDBException {

		if (connection == null)
			throw new CDBException(
					"The DAO's connection attribute must be initialized via setConnection(Connection) method!");

		String sql = "UPDATE computer SET name = ?, introduced = ?, discontinued = ?, company_id = ? WHERE id = ?";
		PreparedStatement statement = null;

		// Converting to dates

		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, newComputerName);
			statement.setDate(2, newIntroducedDate); // possibly null
			statement.setDate(3, newDiscontinuedDate); // possibly null

			if (newCompanyID == null)
				statement.setNull(4, java.sql.Types.INTEGER);
			else
				statement.setInt(4, newCompanyID);

			statement.setInt(5, id);

		} catch (SQLException e) {
			throw new CDBException("Couldn't prepare the SQL statement!");
		}

		try {
			statement.executeUpdate();

		} catch (SQLException e) {
			throw new CDBException("Couldn't execute the query!");
		}

		System.out.println("Entry updated.");

	}

	/**
	 * Deletes the entry of the given computer
	 * 
	 * @param id
	 *            the id of the relevant computer
	 * @throws CDBException
	 */
	public void delete(int id) throws CDBException {

		if (connection == null)
			throw new CDBException(
					"The DAO's connection attribute must be initialized via setConnection(Connection) method!");

		String sql = "DELETE FROM `computer` WHERE id = ?";
		PreparedStatement statement = null;

		try {
			statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new CDBException("Couldn't prepare the SQL statement!");
		}

		System.out.println("Entry deleted.");

	}

	/**
	 * Returns all computers from the database as Java objects
	 * 
	 * @throws CDBException
	 */
	public ArrayList<Computer> listAll() throws CDBException {

		if (connection == null)
			throw new CDBException(
					"The DAO's connection attribute must be initialized via setConnection(Connection) method!");

		ArrayList<Computer> computers = new ArrayList<Computer>();
		String sql = "SELECT id, name, introduced, discontinued, company_id FROM `computer`";
		PreparedStatement statement = null;

		try {
			statement = connection.prepareStatement(sql);
		} catch (SQLException e) {
			throw new CDBException("Couldn't prepare the SQL statement!");
		}

		try {
			// Connecting to the database and executing the query
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next())
				computers.add(new Computer(resultSet.getInt("id"), resultSet.getString("name"),
						resultSet.getDate("introduced"), resultSet.getDate("discontinued"),
						resultSet.getInt("company_id")));

		} catch (SQLException e) {
			throw new CDBException("Couldn't execute the query!");
		}

		return computers;
	}

	// Singleton instance getter
	public static ComputerDAO getInstance() {
		if (INSTANCE == null)
			INSTANCE = new ComputerDAO();
		return INSTANCE;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	/**
	 * Count the number of entries in the computer database
	 * 
	 * @return the number of entries in the computer database
	 * @throws CDBException
	 */
	public int countComputerEntries() throws CDBException {

		String sql = "SELECT COUNT(*) FROM `computer`";

		Statement statement;
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			throw new CDBException("Couldn't create the SQL statement!");
		}

		int nbEntries = -1; // Initializing

		try {
			ResultSet rs = statement.executeQuery(sql);
			if (rs.next())
				nbEntries = rs.getInt(1);
		} catch (SQLException e) {
			throw new CDBException("Failed to gather the entry count!");
		}

		return nbEntries;
	}
}
