package mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/* This class uses the Singleton pattern */

import model.Computer;

public class ComputerMapper extends Mapper<Computer> {

    private static ComputerMapper instance = null;

    private ComputerMapper() {
    }

    public static ComputerMapper getInstance() {
        if (instance == null) {
            instance = new ComputerMapper();
        }
        return instance;
    }

    @Override
    public Computer toModel(ResultSet rs) throws SQLException, MapperException {

        Computer computer;
        try {
            computer = new Computer(rs.getInt("id"), rs.getString("name"),
                    rs.getDate("introduced") == null
                            ? null
                            : rs.getDate("introduced").toLocalDate(),
                    rs.getDate("discontinued") == null
                            ? null
                            : rs.getDate("discontinued").toLocalDate(),
                    rs.getInt("company_id"));
        } catch (SQLException e) {
            throw new MapperException("ResultSet object did not have a first entry!", e);
        }

        return computer;
    }
}
