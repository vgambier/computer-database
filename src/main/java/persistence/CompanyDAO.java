package persistence;

import java.io.IOException;
import java.sql.Connection;
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

    public void delete(Integer companyID) throws PersistenceException {

        String sqlComputers = "DELETE FROM `computer` WHERE company_id = ?";
        String sqlCompany = "DELETE FROM `company` WHERE id = ?";

        try (Connection connection = DatabaseConnector.getInstance().connect()) {

            connection.setAutoCommit(false);

            try (PreparedStatement computersStatement = connection.prepareStatement(sqlComputers);
                    PreparedStatement companyStatement = connection.prepareStatement(sqlCompany)) {

                computersStatement.setInt(1, companyID);
                computersStatement.executeUpdate();

                companyStatement.setInt(1, companyID);
                companyStatement.executeUpdate();

                connection.commit();
            } finally {
                if (connection != null) {
                    try {
                        connection.rollback();
                    } catch (SQLException e) {
                        throw new PersistenceException("Couldn't rollback transacton.", e);
                    }
                }
            }

            System.out.println("Company succesfully deleted.");

        } catch (SQLException e) {
            throw new PersistenceException("Couldn't prepare and execute the SQL statements.", e);
        } catch (IOException e) {
            throw new PersistenceException("Couldn't prepare and execute the SQL statements.", e);

        }
    }
}