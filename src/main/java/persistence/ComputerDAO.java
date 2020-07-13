package persistence;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import mapper.ComputerMapper;
import mapper.MapperException;
import model.Computer;
import model.ModelException;

@Component("computerDAOBean")
public class ComputerDAO extends DAO<Computer> {

    @Autowired
    public ComputerDAO(DatabaseConnector databaseConnector,
            NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(databaseConnector, namedParameterJdbcTemplate);
    }

    /**
     * Finds a computer in the database, and returns a corresponding Java object.
     *
     * @param id
     *            the id of the computer in the database
     * @return a Computer object, with the same attributes as the computer entry in the database
     * @throws PersistenceException
     * @throws MapperException
     */
    public Computer find(int id) throws PersistenceException, MapperException {

        Computer computer = null;

        String sql = "SELECT computer.id AS computer_id, computer.name AS computer_name, "
                + "introduced, discontinued, company.name AS company_name, company_id "
                + "FROM `computer` LEFT JOIN `company` ON computer.company_id = company.id "
                + "WHERE computer.id = ? ORDER BY computer_id";

        try (Connection connection = databaseConnector.connect();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.isBeforeFirst()) {
                    resultSet.next();
                    computer = ComputerMapper.getInstance().toModel(resultSet);
                }

            } catch (SQLException e) {
                throw new PersistenceException("Couldn't execute the SQL statement.", e);
            }

        } catch (SQLException e) {
            throw new PersistenceException("Couldn't prepare the SQL statement.", e);
        }

        return computer;
    }

    /**
     * Adds an entry for a new computer.
     *
     * @param computerName
     *            the name of the new computer - cannot be null
     * @param introducedDate
     *            the date of introduction of the new computer - may be null
     * @param discontinuedDate
     *            the date of discontinuation of the new computer - may be null
     * @param companyID
     *            the ID of the company of the new computer - may be null
     * @throws IOException
     * @throws PersistenceException
     * @throws Exception
     */
    public void add(String computerName, Date introducedDate, Date discontinuedDate,
            Integer companyID) throws PersistenceException {

        String sql = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES (?, ?, ?, ?)";

        // Converting to dates

        try (Connection connection = databaseConnector.connect();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, computerName);
            statement.setDate(2, introducedDate); // possibly null
            statement.setDate(3, discontinuedDate); // possibly null

            if (companyID == null) {
                statement.setNull(4, java.sql.Types.INTEGER);
            } else {
                statement.setInt(4, companyID);
            }

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenceException("Couldn't prepare and execute the SQL statement.", e);
        }
    }

    /**
     * Adds an entry for a new computer.
     *
     * @param id
     *            the id of the existing computer
     * @param newComputerName
     *            the new name of the computer - cannot be null
     * @param newIntroducedDate
     *            the new date of introduction of the computer - may be null
     * @param newDiscontinuedDate
     *            the new date of discontinuation of the computer - may be null
     * @param newCompanyID
     *            the new ID of the company of the computer - may be null
     * @throws IOException
     * @throws PersistenceException
     * @throws Exception
     */
    public void update(int id, String newComputerName, Date newIntroducedDate,
            Date newDiscontinuedDate, Integer newCompanyID) throws PersistenceException {

        String sql = "UPDATE computer SET name = ?, introduced = ?, discontinued = ?, company_id = ? WHERE id = ?";

        // Converting to dates

        try (Connection connection = databaseConnector.connect();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, newComputerName);
            statement.setDate(2, newIntroducedDate); // possibly null
            statement.setDate(3, newDiscontinuedDate); // possibly null

            if (newCompanyID == null) {
                statement.setNull(4, java.sql.Types.INTEGER);
            } else {
                statement.setInt(4, newCompanyID);
            }

            statement.setInt(5, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenceException("Couldn't prepare and execute the SQL statement.", e);
        }
    }

    /**
     * Deletes the entry of the given computer.
     *
     * @param id
     *            the id of the relevant computer
     * @throws IOException
     * @throws PersistenceException
     * @throws Exception
     */
    public void delete(int id) throws PersistenceException {

        String sql = "DELETE FROM `computer` WHERE id = ?";

        try (Connection connection = databaseConnector.connect();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException("Couldn't prepare and execute the SQL statement.", e);
        }
    }

    /**
     * Lists all Computer objects that match the search term, from the given range. This method is
     * meant to be used to fill ComputerPage objects
     *
     * @param limit
     *            the value of the SQL LIMIT parameter
     * @param offset
     *            the value of the SQL OFFSET parameter
     * @param searchTerm
     *            the search term - an entry match if it contains this string anywhere in its name
     *            or its company name
     * @return the corresponding list of Computer objects
     * @throws IOException
     * @throws ModelException
     * @throws MapperException
     * @throws PersistenceException
     */
    public List<Computer> listSomeWhere(int limit, int offset, String searchTerm, String orderBy)
            throws ModelException, MapperException, PersistenceException {

        if (!orderBy.equals("computer_id") && !orderBy.equals("computer_name")
                && !orderBy.equals("introduced") && !orderBy.equals("discontinued")
                && !orderBy.equals("company_name")) {

            throw new PersistenceException("Invalid column name"); // Avoid SQL injections
        }

        List<Computer> computers = new ArrayList<Computer>();

        String sql = "SELECT computer.id AS computer_id, computer.name AS computer_name, "
                + "introduced, discontinued, company.name AS company_name, company_id "
                + "FROM `computer` LEFT JOIN `company` ON computer.company_id = company.id "
                + "WHERE computer.name LIKE ? OR company.name LIKE ? " + "ORDER BY " + orderBy
                + " LIMIT ? OFFSET ?";
        // This query works even for the last page which only has (nbEntries % MAX_ITEMS_PER_PAGE)
        // entries

        try (Connection connection = databaseConnector.connect();
                PreparedStatement statementList = connection.prepareStatement(sql)) {

            statementList.setString(1, "%" + searchTerm + "%");
            statementList.setString(2, "%" + searchTerm + "%");
            statementList.setInt(3, limit);
            statementList.setInt(4, offset);

            try (ResultSet resultSet = statementList.executeQuery()) {

                while (resultSet.next()) {
                    computers.add(ComputerMapper.getInstance().toModel(resultSet));
                }

            } catch (SQLException e) {
                throw new ModelException("Couldn't execute the SQL statement.", e);
            }

        } catch (SQLException e) {
            throw new ModelException("Couldn't prepare the SQL statement.", e);
        }

        return computers;
    }

    @Override
    public ComputerMapper getTypeMapper() {
        return ComputerMapper.getInstance();
    }

    @Override
    protected String getListAllSQLStatement() {

        return "SELECT computer.id AS computer_id, computer.name AS computer_name, "
                + "introduced, discontinued, company.name AS company_name, company_id "
                + "FROM `computer` LEFT JOIN `company` ON computer.company_id = company.id";
    }

    @Override
    protected String getCountEntriesWhereSQLStatement() {

        return "SELECT COUNT(*) FROM `computer` LEFT JOIN `company` "
                + "ON computer.company_id = company.id "
                + "WHERE computer.name LIKE :search_term OR company.name LIKE :search_term";
    }

    @Override
    protected String getTableName() {
        return "computer";
    }

}
