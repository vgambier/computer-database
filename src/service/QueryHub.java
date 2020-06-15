package service;

import java.sql.Connection;
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

		String sql = "SELECT * FROM `company` WHERE id = ?";
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

	public static void addComputer(DatabaseConnection dbConnection, int id) {
		// TODO
		// INSERT INTO computer VALUES ('valeur 1', 'valeur 2', ...)
	}

	public static void updateComputer(DatabaseConnection dbConnection, int id) {
		// TODO
		// UPDATE computer SET colonne_1 = 'valeur 1', colonne_2 = * 'valeur 2',
		// colonne_3 = 'valeur 3' WHERE id = %user_input%
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
