package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Company;
import service.CDBException;

public class CompanyDAO {

	private Connection connection;

	public CompanyDAO(Connection connection) {
		this.connection = connection;
	}

	/**
	 * Returns all companies from the database as Java objects
	 * 
	 * @throws CDBException
	 */
	public ArrayList<Company> listAll() throws CDBException {

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

}
