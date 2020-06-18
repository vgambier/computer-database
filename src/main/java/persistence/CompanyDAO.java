package persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Company;

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
	public List<Company> listAll() throws Exception {

		List<Company> companies = new ArrayList<Company>();
		String sql = "SELECT id, name FROM `company`";
		PreparedStatement statement = null;

		try (DatabaseConnection dbConnection = DatabaseConnection.getInstance()) {

			statement = dbConnection.connect().prepareStatement(sql);

			// Connecting to the database and executing the query
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next())
				companies.add(new Company(resultSet.getInt("id"), resultSet.getString("name")));

		} catch (SQLException e) {
			throw new PersistenceException("Couldn't prepare and execute the SQL statement.", e);
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

		int nbEntries = -1; // The only way the "if" fails is if the query fails, but an exception will be thrown anyway

		try (DatabaseConnection dbConnection = DatabaseConnection.getInstance()) {
			statement = dbConnection.connect().createStatement();
			ResultSet rs = statement.executeQuery(sql);
			if (rs.next())
				nbEntries = rs.getInt(1);
		} catch (SQLException e) {
			throw new PersistenceException("Couldn't create the SQL statement!", e);
		}

		return nbEntries;
	}
}
