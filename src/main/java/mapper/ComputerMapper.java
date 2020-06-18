package mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/* This class uses the Singleton pattern */

import model.Computer;

public class ComputerMapper extends Mapper<Computer> {

	private static ComputerMapper INSTANCE = null;

	private ComputerMapper() {
	}

	public static ComputerMapper getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ComputerMapper();
		}
		return INSTANCE;
	}

	// TODO: should toModel() and toModelList() be a single method?

	@Override
	public Computer toModel(ResultSet rs) throws SQLException, MapperException {

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

	@Override
	public List<Computer> toModelList(ResultSet rs) throws SQLException {

		List<Computer> computers = new ArrayList<Computer>();

		while (rs.next())
			computers.add(new Computer(rs.getInt("id"), rs.getString("name"),
					rs.getDate("introduced") == null ? null : rs.getDate("introduced").toLocalDate(),
					rs.getDate("discontinued") == null ? null : rs.getDate("discontinued").toLocalDate(),
					rs.getInt("company_id")));

		return computers;
	}
}
