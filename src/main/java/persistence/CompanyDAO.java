package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.Company;
import service.CDBException;

/* This class uses the Singleton pattern */

public class CompanyDAO {

	private static CompanyDAO INSTANCE = null;

	private CompanyDAO() {
	}

	// Singleton instance getter
	public static CompanyDAO getInstance() {
		if (INSTANCE == null)
			INSTANCE = new CompanyDAO();
		return INSTANCE;
	}

	private Connection connection;

	/**
	 * Returns all companies from the database as Java objects
	 * 
	 * @throws CDBException
	 */
	public ArrayList<Company> listAll() throws CDBException {

		if (connection == null)
			throw new CDBException(
					"The DAO's connection attribute must be initialized via setConnection(Connection) method!");

		ArrayList<Company> companies = new ArrayList<Company>();
		String sql = "SELECT id, name FROM `company`";
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
				companies.add(new Company(resultSet.getInt("id"), resultSet.getString("name")));

		} catch (SQLException e) {
			throw new CDBException("Couldn't execute the query!");
		}

		return companies;
	}

	// TODO: factorize this (too similar to countComputerEntries)
	/**
	 * Count the number of entries in the company database
	 * 
	 * @return the number of entries in the company database
	 * @throws CDBException
	 */
	public int countCompanyEntries() throws CDBException {

		String sql = "SELECT COUNT(*) FROM `company`";

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

	// Getters and setters

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

}
