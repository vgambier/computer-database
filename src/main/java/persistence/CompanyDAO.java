package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Company;
import service.CDBException;

/* This class uses the Singleton pattern */

public class CompanyDAO {

	private static CompanyDAO INSTANCE = new CompanyDAO();

	private CompanyDAO() {
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

	// Singleton instance getter
	public static CompanyDAO getInstance() {
		if (INSTANCE == null)
			INSTANCE = new CompanyDAO();
		return INSTANCE;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

}
