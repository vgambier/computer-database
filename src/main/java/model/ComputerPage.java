package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mapper.ComputerMapper;
import persistence.ComputerDAO;
import persistence.DatabaseConnection;

public class ComputerPage implements Page {

	private static final int MAX_ITEMS_PER_PAGE = 25;
	private static int nbPages;

	private int pageNumber;
	private List<Computer> list = new ArrayList<Computer>();

	public ComputerPage(int pageNumber) throws Exception {

		// Checking the database to count the number of entries
		int nbEntries = ComputerDAO.getInstance().countEntries();

		nbPages = nbEntries / MAX_ITEMS_PER_PAGE;

		// Checking if the input page number is valid
		checkPageNumber(pageNumber);
		this.pageNumber = pageNumber;

		// Putting computers in the page
		fillList();
	}

	/**
	 * Checks if the given page number is smaller than the total number of pages
	 * 
	 * @param pageNumber
	 *            the page number we want to check
	 * @throws ModelException
	 *             if the page number is not valid
	 * 
	 */
	private void checkPageNumber(int pageNumber) throws ModelException {
		if (pageNumber > nbPages) {
			StringBuilder str = new StringBuilder();
			str.append("Invalid page number. With the current database, there are only ").append(nbPages)
					.append(" pages.");
			throw new ModelException(str.toString());
		}
	}

	/**
	 * Fills the list attribute with all Computer objects that should be on the
	 * current page
	 * 
	 * @throws Exception
	 */
	private void fillList() throws Exception {

		// TODO: move to ComputerDAO
		String sqlList = "SELECT id, name, introduced, discontinued, company_id FROM `computer` LIMIT ? OFFSET ?";
		// This query works even for the last page which only has (nbEntries % MAX_ITEMS_PER_PAGE) entries

		PreparedStatement statementList;

		try (DatabaseConnection dbConnection = DatabaseConnection.getInstance()) {
			statementList = dbConnection.connect().prepareStatement(sqlList);
			statementList.setInt(1, MAX_ITEMS_PER_PAGE);
			statementList.setInt(2, (pageNumber - 1) * MAX_ITEMS_PER_PAGE);
			ResultSet resultSet = statementList.executeQuery();

			list = ComputerMapper.getInstance().makeComputerList(resultSet);

		} catch (SQLException e) {
			throw new ModelException("Couldn't query the database to fill the page!", e);
		}
	}

	// Getters

	public List<Computer> getList() {
		return list;
	}

	public static int getNbPages() {
		return nbPages;
	}

}
