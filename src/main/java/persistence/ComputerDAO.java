package persistence;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mapper.ComputerMapper;
import model.Computer;
import model.ModelException;

/* This class uses the Singleton pattern */

public class ComputerDAO extends DAO<Computer> {

    private static ComputerDAO instance = null;

    private ComputerDAO() {
        super();
    }

    // Singleton instance getter
    public static ComputerDAO getInstance() {
        if (instance == null) {
            instance = new ComputerDAO();
        }
        tableName = "computer";
        return instance;
    }

    /**
     * Finds a computer in the database, and returns a corresponding Java
     * object.
     *
     * @param id
     *            the id of the computer in the database
     * @return a Computer object, with the same attributes as the computer entry
     *         in the database
     * @throws Exception
     */
    public Computer find(int id) throws Exception {

        Computer computer;

        String sql = "SELECT id, name, introduced, discontinued, company_id  FROM `computer` WHERE id = ?";
        PreparedStatement statement;

        try (DatabaseConnection dbConnection = DatabaseConnection
                .getInstance()) {
            statement = dbConnection.connect().prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            computer = ComputerMapper.getInstance().toModel(resultSet);

        } catch (SQLException e) {
            throw new PersistenceException(
                    "Couldn't prepare and execute the SQL statement.", e);
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
     * @throws Exception
     */
    public void add(String computerName, Date introducedDate,
            Date discontinuedDate, Integer companyID) throws Exception {

        String sql = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES (?, ?, ?, ?)";
        PreparedStatement statement;

        // Converting to dates

        try (DatabaseConnection dbConnection = DatabaseConnection
                .getInstance()) {
            statement = dbConnection.connect().prepareStatement(sql);
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
            throw new PersistenceException(
                    "Couldn't prepare and execute the SQL statement.", e);
        }

        System.out.println("Entry added.");
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
     * @throws Exception
     */
    public void update(int id, String newComputerName, Date newIntroducedDate,
            Date newDiscontinuedDate, Integer newCompanyID) throws Exception {

        String sql = "UPDATE computer SET name = ?, introduced = ?, discontinued = ?, company_id = ? WHERE id = ?";
        PreparedStatement statement;

        // Converting to dates

        try (DatabaseConnection dbConnection = DatabaseConnection
                .getInstance()) {
            statement = dbConnection.connect().prepareStatement(sql);
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
            throw new PersistenceException(
                    "Couldn't prepare and execute the SQL statement.", e);
        }

        System.out.println("Entry updated.");

    }

    /**
     * Deletes the entry of the given computer.
     *
     * @param id
     *            the id of the relevant computer
     * @throws Exception
     */
    public void delete(int id) throws Exception {

        String sql = "DELETE FROM `computer` WHERE id = ?";
        PreparedStatement statement;

        try (DatabaseConnection dbConnection = DatabaseConnection
                .getInstance()) {
            statement = dbConnection.connect().prepareStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(
                    "Couldn't prepare and execute the SQL statement.", e);
        }

        System.out.println("Entry deleted.");

    }

    /**
     * Lists all Computer objects from the given range. This method is meant to
     * be used to fill ComputerPage objects
     *
     * @param limit
     *            the value of the SQL LIMIT parameter
     * @param offset
     *            the value of the SQL OFFSET parameter
     * @throws Exception
     * @return the corresponding list of Computer objects
     */
    public List<Computer> listSome(int limit, int offset) throws Exception {

        List<Computer> computers = new ArrayList<Computer>();

        String sqlList = "SELECT id, name, introduced, discontinued, company_id FROM `computer` LIMIT ? OFFSET ?";
        // This query works even for the last page which only has (nbEntries %
        // MAX_ITEMS_PER_PAGE) entries

        PreparedStatement statementList;

        try (DatabaseConnection dbConnection = DatabaseConnection
                .getInstance()) {
            statementList = dbConnection.connect().prepareStatement(sqlList);
            statementList.setInt(1, limit);
            statementList.setInt(2, offset);
            ResultSet resultSet = statementList.executeQuery();

            computers = ComputerMapper.getInstance().toModelList(resultSet);

        } catch (SQLException e) {
            throw new ModelException(
                    "Couldn't query the database to fill the page!", e);
        }

        return computers;
    }

    @Override
    public ComputerMapper getTypeMapper() {
        return ComputerMapper.getInstance();
    }
}
