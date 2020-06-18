package persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class DAO {

	/**
	 * Count the number of entries in the database. Works for both the computer
	 * database and the company database
	 * 
	 * @return the number of entries in the database
	 * @throws Exception
	 */
	public int countEntries() throws Exception {

		String sql;
		if (this instanceof ComputerDAO)
			sql = "SELECT COUNT(*) FROM `computer`";
		else if (this instanceof CompanyDAO)
			sql = "SELECT COUNT(*) FROM `company`";
		else
			throw new PersistenceException("DAO only has 2 children classes: ComputerDAO and CompanyDAO");

		Statement statement;

		int nbEntries = -1; // The only way the "if" fails is if the query fails, but an exception will be thrown anyway

		try (DatabaseConnection dbConnection = DatabaseConnection.getInstance()) {
			statement = dbConnection.connect().prepareStatement(sql);
			ResultSet rs = statement.executeQuery(sql);
			if (rs.next())
				nbEntries = rs.getInt(1);
		} catch (SQLException e) {
			throw new PersistenceException("Couldn't prepare and execute the SQL statement.", e);
		}

		return nbEntries;
	}

}
