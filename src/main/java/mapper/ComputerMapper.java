package mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/* This class uses the Singleton pattern */

import model.Computer;

public class ComputerMapper {

	private static ComputerMapper INSTANCE = null;

	private ComputerMapper() {
	}

	public static ComputerMapper getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ComputerMapper();
		}
		return INSTANCE;
	}

	/**
	 * @param rs
	 *            a ResultSet object, that should come from a query on the
	 *            computer table. Only the first item of the query will be
	 *            considered.
	 * @return a Computer object that modelizes the computer entry from the
	 *         database
	 * @throws MapperException
	 *             - if the input ResultSet had 0 entries
	 * @throws SQLException
	 */
	public Computer makeComputer(ResultSet rs) throws SQLException, MapperException {

		Computer computer;

		if (rs.first())
			computer = new Computer(rs.getInt("id"), rs.getString("name"),
					rs.getDate("introduced") == null ? null : rs.getDate("introduced").toLocalDate(),
					rs.getDate("discontinued") == null ? null : rs.getDate("discontinued").toLocalDate(),
					rs.getInt("company_id"));
		else
			throw new MapperException("ResultSet object did not have a first entry!");

		return computer;
	}

	/**
	 * @param rs
	 *            a ResultSet object, that should come from a query on the
	 *            computer table.
	 * @return a List<Computer> object that modelizes the computer entries from
	 *         the database. Possibly empty if the ResultSet had 0 entries.
	 * @throws SQLException
	 */
	public List<Computer> makeComputerList(ResultSet rs) throws SQLException {

		List<Computer> computers = new ArrayList<Computer>();

		while (rs.next())
			computers.add(new Computer(rs.getInt("id"), rs.getString("name"),
					rs.getDate("introduced") == null ? null : rs.getDate("introduced").toLocalDate(),
					rs.getDate("discontinued") == null ? null : rs.getDate("discontinued").toLocalDate(),
					rs.getInt("company_id")));

		return computers;
	}
}
