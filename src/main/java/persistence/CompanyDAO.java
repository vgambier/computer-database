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
        return instance;
    }

    @Override
    public CompanyMapper getTypeMapper() {
        return CompanyMapper.getInstance();
    }

    @Override
    protected String getListAllSQLStatement() {
        return "SELECT id, name FROM company";
    }

    @Override
    protected String getTableName() {
        return "company";
    }

}
