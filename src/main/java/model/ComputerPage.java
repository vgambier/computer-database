package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import service.CDBException;

public class ComputerPage {

	private static final int MAX_ITEMS_PER_PAGE = 25;

	private int pageNumber;
	private ArrayList<Computer> list = new ArrayList<Computer>();
	private Connection connection;

	// TODO: factorize this method into 2-3 methods
	public ComputerPage(int pageNumber, Connection connection) throws CDBException, SQLException {

		this.connection = connection;

		// Checking the database to count the number of entries

		String sql = "SELECT COUNT(*) FROM `computer`";

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

		// Checking if the input page number is valid

		int nbPages = nbEntries / MAX_ITEMS_PER_PAGE;
		if (pageNumber > nbPages) {
			StringBuilder str = new StringBuilder();
			str.append("Invalid page number. With the current database, there are only ").append(nbPages)
					.append(" pages.");
			throw new CDBException(str.toString()); // TODO: soft error message instead
		}

		this.pageNumber = pageNumber;

		// Putting computers in the page

		String sqlList = "SELECT * FROM `computer` LIMIT ? OFFSET ?"; // works even for the last page which only has
																		// (nbEntries % MAX_ITEMS_PER_PAGE) entries

		PreparedStatement statementList;
		statementList = connection.prepareStatement(sqlList);
		statementList.setInt(1, MAX_ITEMS_PER_PAGE);
		statementList.setInt(2, (pageNumber - 1) * MAX_ITEMS_PER_PAGE);
		ResultSet resultSet = statementList.executeQuery();

		while (resultSet.next())
			list.add(new Computer(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getDate("introduced"),
					resultSet.getDate("discontinued"), resultSet.getInt("company_id")));

	}

	public ArrayList<Computer> getList() {
		return list;
	}

}
