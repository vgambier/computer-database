package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Company;

public class CompanyDAO {

	private Connection connection;

	public CompanyDAO(Connection connection) {
		this.connection = connection;
	}

	/**
	 * Returns all companies from the database as Java objects
	 */
	public ArrayList<Company> listAll() {

		ArrayList<Company> companies = new ArrayList<Company>();
		String sql = "SELECT id, name FROM `company`";
		PreparedStatement statement = null;

		try {

			statement = connection.prepareStatement(sql);

			// Connecting to the database and executing the query
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next())
				companies.add(new Company(resultSet.getInt("id"), resultSet.getString("name")));

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return companies;
	}

}
