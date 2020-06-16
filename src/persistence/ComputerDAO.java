package persistence;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Computer;

public class ComputerDAO {

	private Connection connection;

	public ComputerDAO(Connection connection) {
		this.connection = connection;
	}

	/**
	 * Finds a computer in the database, and returns a corresponding Java object
	 * 
	 * @param id
	 *            the id of the computer in the database
	 * @return a Computer object, with the same attributes as the computer entry
	 *         in the database
	 */
	public Computer find(int id) {

		Computer computer = new Computer();

		String sql = "SELECT * FROM `computer` WHERE id = ?";
		PreparedStatement statement = null;

		try {
			statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.first()) {
				computer = new Computer(id, resultSet.getString("name"), resultSet.getDate("introduced"),
						resultSet.getDate("discontinued"), resultSet.getInt("company_id"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return computer;

	}

	// TODO change this function

	/**
	 * Adds an entry for a new computer
	 * 
	 * @param computerName
	 *            the name of the new computer - cannot be null
	 * @param introducedDate
	 *            the date of introduction of the new computer - may be null
	 * @param discontinuedDate
	 *            the date of discontinuation of the new computer - may be null
	 * @param companyID
	 *            the ID of the company of the new computer - may be null
	 */
	public void add(String computerName, Date introducedDate, Date discontinuedDate, Integer companyID) {

		String sql = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES (?, ?, ?, ?)";
		PreparedStatement statement = null;

		// Converting to dates

		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, computerName);
			statement.setDate(2, introducedDate); // possibly null
			statement.setDate(3, discontinuedDate); // possibly null

			if (companyID == null)
				statement.setNull(4, java.sql.Types.INTEGER);
			else
				statement.setInt(4, companyID);

			statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println("Entry added.");
	}

	/**
	 * Adds an entry for a new computer
	 * 
	 * @param id
	 *            the id of the existing computer
	 * @param newComputerName
	 *            the new name of the computer - cannot be null
	 * @param newIntroducedDate
	 *            the new date of introduction of the computer - may be null
	 * @param newDiscontinuedDate
	 *            the new date of discontinuation of the computer - may be null
	 * @param newCompanyID
	 *            the new ID of the company of the computer - may be null
	 */
	public void update(int id, String newComputerName, Date newIntroducedDate, Date newDiscontinuedDate,
			Integer newCompanyID) {

		String sql = "UPDATE computer SET name = ?, introduced = ?, discontinued = ?, company_id = ? WHERE id = ?";
		PreparedStatement statement = null;

		// Converting to dates

		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, newComputerName);
			statement.setDate(2, newIntroducedDate); // possibly null
			statement.setDate(3, newDiscontinuedDate); // possibly null

			if (newCompanyID == null)
				statement.setNull(4, java.sql.Types.INTEGER);
			else
				statement.setInt(4, newCompanyID);

			statement.setInt(5, id);
			statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println("Entry updated.");

	}

	/**
	 * Deletes the entry of the given computer
	 * 
	 * @param id
	 *            the id of the relevant computer
	 */
	public void delete(int id) {

		String sql = "DELETE FROM `computer` WHERE id = ?";
		PreparedStatement statement = null;

		try {
			statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println("Entry deleted.");

	}

	// TODO
	public ArrayList<Computer> listAll() {
		return null;
	}

}
