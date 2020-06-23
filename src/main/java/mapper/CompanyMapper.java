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
    public Company toModel(ResultSet rs) throws SQLException, MapperException {
        // TODO: Auto-generated method stub - currently not needed
        return null;
    }

}
