package persistence;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

    @Override
    protected String getCountEntriesWhereSQLStatement() {
        return "SELECT COUNT(*) FROM `company` WHERE name LIKE ?";
    }

    public void delete(Integer companyID) throws PersistenceException, IOException {

        // TODO do this properly (rollback, etc.)

        String sql_computers = "DELETE FROM `computer` WHERE company_id = ?";

        try (DatabaseConnector dbConnector = DatabaseConnector.getInstance();
                PreparedStatement statement = dbConnector.connect()
                        .prepareStatement(sql_computers)) {
            statement.setInt(1, companyID);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException("Couldn't prepare and execute the SQL statement.", e);
        }

        System.out.println("Computer entries deleted.");

        String sql_company = "DELETE FROM `company` WHERE id = ?";

        try (DatabaseConnector dbConnector = DatabaseConnector.getInstance();
                PreparedStatement statement = dbConnector.connect().prepareStatement(sql_company)) {
            statement.setInt(1, companyID);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException("Couldn't prepare and execute the SQL statement.", e);
        }

        System.out.println("Company entry deleted.");

    }

}
