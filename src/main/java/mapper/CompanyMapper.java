package mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import model.Company;

/* This class uses the Singleton pattern */

public class CompanyMapper extends Mapper<Company> {

    private static CompanyMapper instance = null;

    private CompanyMapper() {
    }

    public static CompanyMapper getInstance() {
        if (instance == null) {
            instance = new CompanyMapper();
        }
        return instance;
    }

    @Override
    public Company toModel(ResultSet rs) throws MapperException {

        try {
            return new Company(rs.getInt("id"), rs.getString("name"));
        } catch (SQLException e) {
            throw new MapperException("ResultSet object did not have a first entry!", e);
        }

    }
}
