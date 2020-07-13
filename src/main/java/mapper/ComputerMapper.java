package mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

/* This class uses the Singleton pattern */

import model.Computer;

public class ComputerMapper implements RowMapper<Computer> {

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
    public Computer mapRow(ResultSet rs, int rowNum) throws SQLException {

        return new Computer(rs.getInt("computer_id"), rs.getString("computer_name"),

                rs.getDate("introduced") == null ? null : rs.getDate("introduced").toLocalDate(),

                rs.getDate("discontinued") == null
                        ? null
                        : rs.getDate("discontinued").toLocalDate(),

                rs.getString("company_name"),

                rs.getInt("company_id"));

    }
}
