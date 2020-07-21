package com.excilys.cdb.persistence;

import org.hibernate.SessionFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

/**
 * @author Victor Gambier
 *
 * @param <T>
 *            should be an entity from the model package, e.g.: Computer, Page, etc.
 */
public abstract class DAO<T> {

    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    protected SessionFactory sessionFactory;
    protected RowMapper<T> mapper;

    public DAO(NamedParameterJdbcTemplate namedParameterJdbcTemplate, SessionFactory sessionFactory,
            RowMapper<T> mapper) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.sessionFactory = sessionFactory;
        this.mapper = mapper;
    }

    /**
     * Count the number of entries in the database. Works for both the computer database and the
     * company database.
     *
     * @return the number of entries in the database
     * @throws PersistenceException
     */
    public int countEntries() {
        return countEntriesWhere("");
    }

    /**
     * Count the number of entries in the database that match the search term. Works for both the
     * computer database and the company database.
     *
     * @param searchTerm
     *            the search term - an entry match if it contains this string anywhere in its name
     *            or its company name
     * @return the number of entries in the database
     */
    public int countEntriesWhere(String searchTerm) {

        String sql = getCountEntriesWhereSQLStatement();

        SqlParameterSource namedParameters = new MapSqlParameterSource("search_term",
                "%" + searchTerm + "%");

        return namedParameterJdbcTemplate.queryForObject(sql, namedParameters, Integer.class);
    }

    /**
     * Checks if there is an entry of the given id number in the table.
     *
     * @param id
     *            the id of the entry to be checked
     * @return true if and only if there is an entry
     */
    public boolean doesEntryExist(int id) {

        String sql = getDoesEntryExistSQLStatement();

        SqlParameterSource namedParameters = new MapSqlParameterSource("id", id);

        return namedParameterJdbcTemplate.queryForObject(sql, namedParameters, Integer.class) == 1;
        // this is true if the query returned one entry
        // since the result set returned either 0 or 1 entry
    }

    protected abstract String getCountEntriesWhereSQLStatement();
    protected abstract String getDoesEntryExistSQLStatement();

}
