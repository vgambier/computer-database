package persistence;

import mapper.CompanyMapper;
import model.Company;

/* This class uses the Singleton pattern */

public class CompanyDAO extends DAO<Company> {

    private static CompanyDAO instance = null;

    private CompanyDAO() {
    }

    // Singleton instance getter
    public static CompanyDAO getInstance() {
        if (instance == null) {
            instance = new CompanyDAO();
        }
        tableName = "company";
        return instance;
    }

    @Override
    public CompanyMapper getTypeMapper() {
        return CompanyMapper.getInstance();
    }

}
