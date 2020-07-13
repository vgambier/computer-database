package mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import model.Company;

/* This class uses the Singleton pattern */

public class CompanyMapper implements RowMapper<Company> {

    private static CompanyMapper instance = null;

    private CompanyMapper() {
    }

    public static CompanyMapper getInstance() {
        if (instance == null) {
            instance = new CompanyMapper();
        }
        return instance;
    }

    // TODO: deprecate
    public Company toModel(ResultSet rs) throws MapperException {

        try {
            return new Company(rs.getInt("id"), rs.getString("name"));
        } catch (SQLException e) {
            throw new MapperException("ResultSet object did not have a first entry!", e);
        }

    }

    @Override
    public Company mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Company(rs.getInt("id"), rs.getString("name"));
    }
}
