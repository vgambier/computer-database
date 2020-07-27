package com.excilys.cdb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.excilys.cdb.model.Company;

/* This class uses the Singleton pattern */

/**
 * @author Victor Gambier
 *
 */
public class CompanyDTOMapper implements RowMapper<Company> {

    private static CompanyDTOMapper instance = null;

    private CompanyDTOMapper() {
    }

    public static CompanyDTOMapper getInstance() {
        if (instance == null) {
            instance = new CompanyDTOMapper();
        }
        return instance;
    }

    public Company map(ResultSet rs) throws SQLException {
        return new Company(rs.getInt("id"), rs.getString("name"));
    }
}
