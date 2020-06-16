package service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class QueryHub {

	/**
	 * Given a DatabaseConnection object and a PreparedStatement, prints all of
	 * the results. Useful for listComputers(), listCompanies(), and
	 * computerInfo().
	 * 
	 * @param statement
	 *            a PreparedStatement
	 * @param dbConnection
	 *            a generic DatabaseConnection object
	 */
	public static void printResultSet(PreparedStatement statement, DatabaseConnection dbConnection) {

		try {

			// Connecting to the database and executing the query
			ResultSet resultSet = statement.executeQuery();
			ResultSetMetaData rsmd = resultSet.getMetaData();

			// Formatting the dataset
			int columnsNumber = rsmd.getColumnCount();
			while (resultSet.next()) {
				for (int i = 1; i <= columnsNumber; i++) {
					System.out.print(rsmd.getColumnName(i) + ": " + resultSet.getString(i) + "\t");
				}
				System.out.println(""); // linebreak
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbConnection.disconnect();
		}
	}

	/**
	 * Lists all computers from the database, giving their id and name
	 * 
	 * @param dbConnection
	 *            a generic DatabaseConnection object
	 */
	public static void listComputers(DatabaseConnection dbConnection) {

		String sql = "SELECT id, name FROM `computer`";
		PreparedStatement statement = null;
		Connection connection = dbConnection.connect();

		try {
			statement = connection.prepareStatement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		printResultSet(statement, dbConnection);
	}

	/**
	 * Lists all companies from the database, giving their id and name
	 * 
	 * @param dbConnection
	 *            a generic DatabaseConnection object
	 */
	public static void listCompanies(DatabaseConnection dbConnection) {

		String sql = "SELECT id, name FROM `company`";
		PreparedStatement statement = null;
		Connection connection = dbConnection.connect();

		try {
			statement = connection.prepareStatement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		printResultSet(statement, dbConnection);
	}

	/**
	 * Prints all details about the given computer
	 * 
	 * @param dbConnection
	 *            a generic DatabaseConnection object
	 * @param id
	 *            the id of the relevant computer
	 */
	public static void computerInfo(DatabaseConnection dbConnection, int id) {

		String sql = "SELECT * FROM `computer` WHERE id = ?";
		PreparedStatement statement = null;
		Connection connection = dbConnection.connect();

		try {
			statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		printResultSet(statement, dbConnection);
	}

	/**
	 * Adds an entry for a new computer
	 * 
	 * @param dbConnection
	 *            a generic DatabaseConnection object
	 * @param computerName
	 *            the name of the new computer - cannot be null
	 * @param introducedDate
	 *            the date of introduction of the new computer - may be null
	 * @param discontinuedDate
	 *            the date of discontinuation of the new computer - may be null
	 * @param companyID
	 *            the ID of the company of the new computer - may be null
	 */
	public static void addComputer(DatabaseConnection dbConnection, String computerName, Date introducedDate,
			Date discontinuedDate, Integer companyID) {

		String sql = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES (?, ?, ?, ?)";
		PreparedStatement statement = null;
		Connection connection = dbConnection.connect();

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

			statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println("Entry added.");
	}

	/**
	 * Adds an entry for a new computer
	 * 
	 * @param dbConnection
	 *            a generic DatabaseConnection object
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
	 */
	public static void updateComputer(DatabaseConnection dbConnection, int id, String newComputerName,
			Date newIntroducedDate, Date newDiscontinuedDate, Integer newCompanyID) {

		String sql = "UPDATE computer SET name = ?, introduced = ?, discontinued = ?, company_id = ? WHERE id = ?";
		PreparedStatement statement = null;
		Connection connection = dbConnection.connect();

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
			statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println("Entry updated.");

	}

	/**
	 * Deletes the entry of the given computer
	 * 
	 * @param dbConnection
	 *            a generic DatabaseConnection object
	 * @param id
	 *            the id of the relevant computer
	 */
	public static void deleteComputer(DatabaseConnection dbConnection, int id) {

		String sql = "DELETE FROM `computer` WHERE id = ?";
		PreparedStatement statement = null;
		Connection connection = dbConnection.connect();

		try {
			statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println("Entry deleted.");

	}
}
