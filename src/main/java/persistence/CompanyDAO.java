package persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import mapper.CompanyMapper;
import model.Company;

@Component("companyDAOBean")
public class CompanyDAO extends DAO<Company> {

    private static final String DELETE_COMPUTERS_MATCHING_QUERY = "DELETE FROM `computer` WHERE company_id = :company_id";
    private static final String DELETE_COMPANY_MATCHING_QUERY = "DELETE FROM `company` WHERE id = :id";

    @Autowired
    public CompanyDAO(DatabaseConnector databaseConnector,
            NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(databaseConnector, namedParameterJdbcTemplate, CompanyMapper.getInstance());
    }

    @Transactional(rollbackFor = {Exception.class})
    public void delete(Integer companyID) {

        MapSqlParameterSource computerNamedParameters = new MapSqlParameterSource("company_id",
                companyID);
        namedParameterJdbcTemplate.update(DELETE_COMPUTERS_MATCHING_QUERY, computerNamedParameters);

        MapSqlParameterSource companyNamedParameters = new MapSqlParameterSource("id", companyID);
        namedParameterJdbcTemplate.update(DELETE_COMPANY_MATCHING_QUERY, companyNamedParameters);
    }

    @Override
    protected String getCountEntriesWhereSQLStatement() {
        return "SELECT COUNT(*) FROM `company` WHERE name LIKE :search_term";
    }

    @Override
    protected String getListAllSQLStatement() {
        return "SELECT id, name FROM company";
    }

    @Override
    protected String getDoesEntryExistSQLStatement() {
        return "SELECT COUNT(1) FROM `company` WHERE id = :id";
    }
}