package service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

	private static Connection connection;
	private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/computer-database-db?serverTimezone=UTC";
	private static final String USERNAME = "admincdb";
	private static final String PASSWORD = "qwerty1234";

	/**
	 * Connects to the database
	 * 
	 * @return the Connection object if the connection was successful
	 */
	public Connection connect() {

		try {
			connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
		} catch (SQLException e) {
			System.out.println("Cannot connect to the database");
			throw new IllegalStateException("Exception: cannot connect to the database.", e);
		}

		return connection;

	}

	/**
	 * Disconnects from the database
	 */
	public void disconnect() {

		if (connection != null) {
			try {
				connection.close();
				connection = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
