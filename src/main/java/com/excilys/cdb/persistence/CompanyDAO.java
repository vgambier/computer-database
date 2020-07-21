package com.excilys.cdb.persistence;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.cdb.mapper.CompanyMapper;
import com.excilys.cdb.model.Company;

/**
 * @author Victor Gambier
 *
 */
@Component("companyDAOBean")
public class CompanyDAO extends DAO<Company> {

    private static final String DELETE_COMPUTERS_MATCHING_QUERY = "DELETE FROM `computer` WHERE company_id = :company_id";
    private static final String DELETE_COMPANY_MATCHING_QUERY = "DELETE FROM `company` WHERE id = :id";
    private static final String SELECT_ALL_COMPANIES_HQL = "from Company";

    @Autowired
    public CompanyDAO(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
            SessionFactory sessionFactory) {
        super(namedParameterJdbcTemplate, sessionFactory, CompanyMapper.getInstance());
    }

    @Transactional(rollbackFor = {Exception.class})
    public void delete(Integer companyID) {

        MapSqlParameterSource computerNamedParameters = new MapSqlParameterSource("company_id",
                companyID);
        namedParameterJdbcTemplate.update(DELETE_COMPUTERS_MATCHING_QUERY, computerNamedParameters);

        MapSqlParameterSource companyNamedParameters = new MapSqlParameterSource("id", companyID);
        namedParameterJdbcTemplate.update(DELETE_COMPANY_MATCHING_QUERY, companyNamedParameters);
    }

    public List<Company> listAll() {

        Session session = sessionFactory.openSession();

        @SuppressWarnings("unchecked")
        Query<Company> query = session.createQuery(SELECT_ALL_COMPANIES_HQL);
        List<Company> companies = query.list();

        session.close();
        return companies;
    }

    @Override
    protected String getCountEntriesWhereSQLStatement() {
        return "SELECT COUNT(*) FROM `company` WHERE name LIKE :search_term";
    }

    @Override
    protected String getDoesEntryExistSQLStatement() {
        return "SELECT COUNT(1) FROM `company` WHERE id = :id";
    }
}