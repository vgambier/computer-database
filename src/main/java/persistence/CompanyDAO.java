package persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * Returns all companies from the database as Java objects.
     *
     * @throws Exception
     * @return the list of Company objects
     */
    @Override
    public List<Company> listAll() throws Exception {

        List<Company> companies = new ArrayList<Company>();
        String sql = "SELECT id, name FROM `company`";

        try (DatabaseConnection dbConnection = DatabaseConnection.getInstance();
                PreparedStatement statement = dbConnection.connect().prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {

            // Connecting to the database and executing the query

            while (resultSet.next()) {
                companies.add(new Company(resultSet.getInt("id"), resultSet.getString("name")));
            }

        } catch (SQLException e) {
            throw new PersistenceException("Couldn't prepare and execute the SQL statement.", e);
        }

        return companies;
    }

    @Override
    public CompanyMapper getTypeMapper() {
        return CompanyMapper.getInstance();
    }

}
