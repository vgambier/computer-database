package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

// TODO: move everything to DAO and delete this class

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
	public static void printResultSet(PreparedStatement statement) {

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

		printResultSet(statement);
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
		printResultSet(statement);
	}
}
