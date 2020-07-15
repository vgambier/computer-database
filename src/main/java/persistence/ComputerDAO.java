package persistence;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import mapper.ComputerMapper;
import model.Computer;

@Component("computerDAOBean")
public class ComputerDAO extends DAO<Computer> {

    private static final String FIND_ENTRY_QUERY = "SELECT computer.id AS computer_id, computer.name AS computer_name, "
            + "introduced, discontinued, company.name AS company_name, company_id "
            + "FROM `computer` LEFT JOIN `company` ON computer.company_id = company.id "
            + "WHERE computer.id = :id ORDER BY computer_id";

    private static final String ADD_ENTRY_QUERY = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES (:name, :introduced, :discontinued, :company_id)";
    private static final String UPDATE_ENTRY_QUERY = "UPDATE computer SET name = :name, introduced = :introduced, discontinued = :discontinued, company_id = :company_id WHERE id = :id";
    private static final String DELETE_ENTRY_QUERY = "DELETE FROM `computer` WHERE id = :id";

    @Autowired
    public ComputerDAO(DatabaseConnector databaseConnector,
            NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(databaseConnector, namedParameterJdbcTemplate, ComputerMapper.getInstance());
    }

    /**
     * Finds a computer in the database, and returns a corresponding Java object.
     *
     * @param id
     *            the id of the computer in the database
     * @return a Computer object, with the same attributes as the computer entry in the database
     */
    public Computer find(int id) {

        SqlParameterSource namedParameters = new MapSqlParameterSource("id", id);
        return namedParameterJdbcTemplate.query(FIND_ENTRY_QUERY, namedParameters, mapper).get(0);
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
     */
    public void add(String computerName, Date introducedDate, Date discontinuedDate,
            Integer companyID) {

        MapSqlParameterSource namedParameters = new MapSqlParameterSource("name", computerName);
        namedParameters.addValue("introduced", introducedDate); // Possibly null
        namedParameters.addValue("discontinued", discontinuedDate); // Possibly null
        namedParameters.addValue("company_id", companyID); // Possibly null

        namedParameterJdbcTemplate.update(ADD_ENTRY_QUERY, namedParameters);
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
     */
    public void update(int id, String newComputerName, Date newIntroducedDate,
            Date newDiscontinuedDate, Integer newCompanyID) {

        MapSqlParameterSource namedParameters = new MapSqlParameterSource("name", newComputerName);
        namedParameters.addValue("introduced", newIntroducedDate); // Possibly null
        namedParameters.addValue("discontinued", newDiscontinuedDate); // Possibly null
        namedParameters.addValue("company_id", newCompanyID); // Possibly null
        namedParameters.addValue("id", id);

        namedParameterJdbcTemplate.update(UPDATE_ENTRY_QUERY, namedParameters);
    }

    /**
     * Deletes the entry of the given computer.
     *
     * @param id
     *            the id of the relevant computer
     */
    public void delete(int id) {

        MapSqlParameterSource namedParameters = new MapSqlParameterSource("id", id);
        namedParameterJdbcTemplate.update(DELETE_ENTRY_QUERY, namedParameters);
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
     * @param orderBy
     *            the field by which to sort the result set, e.g.: "id" or "name"
     * @return the corresponding list of Computer objects
     * @throws PersistenceException
     */
    public List<Computer> listSomeMatching(int limit, int offset, String searchTerm, String orderBy)
            throws PersistenceException {

        // Avoid SQL injections
        if (!orderBy.equals("computer_id") && !orderBy.equals("computer_name")
                && !orderBy.equals("introduced") && !orderBy.equals("discontinued")
                && !orderBy.equals("company_name")) {

            throw new PersistenceException("Invalid column name");
        }

        String sql = "SELECT computer.id AS computer_id, computer.name AS computer_name, "
                + "introduced, discontinued, company.name AS company_name, company_id "
                + "FROM `computer` LEFT JOIN `company` ON computer.company_id = company.id "
                + "WHERE computer.name LIKE :searchTerm OR company.name LIKE :searchTerm "
                + "ORDER BY " + orderBy + " LIMIT :limit OFFSET :offset";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource("searchTerm",
                "%" + searchTerm + "%");
        namedParameters.addValue("limit", limit);
        namedParameters.addValue("offset", offset);
        return namedParameterJdbcTemplate.query(sql, namedParameters, mapper);
    }

    @Override
    protected String getCountEntriesWhereSQLStatement() {

        return "SELECT COUNT(*) FROM `computer` LEFT JOIN `company` "
                + "ON computer.company_id = company.id "
                + "WHERE computer.name LIKE :search_term OR company.name LIKE :search_term";
    }

    @Override
    protected String getListAllSQLStatement() {

        return "SELECT computer.id AS computer_id, computer.name AS computer_name, "
                + "introduced, discontinued, company.name AS company_name, company_id "
                + "FROM `computer` LEFT JOIN `company` ON computer.company_id = company.id";
    }

    @Override
    protected String getDoesEntryExistSQLStatement() {
        return "SELECT COUNT(1) FROM `computer` WHERE id = :id";
    }

}
