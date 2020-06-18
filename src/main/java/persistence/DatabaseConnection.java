package persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/* This class uses the Singleton pattern */

public class DatabaseConnection implements AutoCloseable {

	private static DatabaseConnection INSTANCE = null;

	private DatabaseConnection() {
	}

	public static DatabaseConnection getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new DatabaseConnection();
		}
		return INSTANCE;
	}

	private static Connection connection;
	// TODO: move this to a local uncommitted properties file, and commit an empty properties file
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
			System.out.println("Cannot connect to the database!");
			throw new IllegalStateException("Exception: cannot connect to the database.", e);
		}

		return connection;

	}

	/**
	 * Disconnects from the database
	 * 
	 * @throws PersistenceException
	 */
	@Override
	public void close() throws Exception {

		if (connection != null) {
			try {
				connection.close();
				connection = null;
			} catch (SQLException e) {
				throw new PersistenceException("Couldn't close the connection!", e);
			}
		}
	}
}
