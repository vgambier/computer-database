package com.excilys.cdb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.excilys.cdb.model.Computer;

/**
 * @author Victor Gambier
 *
 */
public class ComputerDTOMapper implements RowMapper<Computer> {

    private static ComputerDTOMapper instance = null;

    private ComputerDTOMapper() {
    }

    public static ComputerDTOMapper getInstance() {
        if (instance == null) {
            instance = new ComputerDTOMapper();
        }
        return instance;
    }

    public Computer map(ResultSet rs) throws SQLException {

        return new Computer(rs.getInt("computer_id"), rs.getString("computer_name"),

                rs.getDate("introduced") == null ? null : rs.getDate("introduced").toLocalDate(),

                rs.getDate("discontinued") == null
                        ? null
                        : rs.getDate("discontinued").toLocalDate(),

                rs.getString("company_name"),

                rs.getInt("company_id"));

    }
}
