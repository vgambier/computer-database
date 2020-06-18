package persistence;

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

	/**
	 * Returns all companies from the database as Java objects
	 * 
	 * @throws Exception
	 */
	public ArrayList<Company> listAll() throws Exception {

		ArrayList<Company> companies = new ArrayList<Company>();
		String sql = "SELECT id, name FROM `company`";
		PreparedStatement statement = null;

		try (DatabaseConnection dbConnection = DatabaseConnection.getInstance()) {

			statement = dbConnection.connect().prepareStatement(sql);

			// Connecting to the database and executing the query
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next())
				companies.add(new Company(resultSet.getInt("id"), resultSet.getString("name")));

		} catch (SQLException e) {
			throw new CDBException("Couldn't prepare the SQL statement!");
		}

		return companies;
	}

	// TODO: factorize this (too similar to countComputerEntries)
	/**
	 * Count the number of entries in the company database
	 * 
	 * @return the number of entries in the company database
	 * @throws Exception
	 */
	public int countCompanyEntries() throws Exception {

		String sql = "SELECT COUNT(*) FROM `company`";

		Statement statement;
		try (DatabaseConnection dbConnection = DatabaseConnection.getInstance()) {
			statement = dbConnection.connect().createStatement();
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
