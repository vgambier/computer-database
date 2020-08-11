package com.excilys.cdb.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.cdb.model.Company;

/**
 * @author Victor Gambier
 *
 */
@Component("companyDAOBean")
public class CompanyDAO extends DAO<Company> {

    private static final String SELECT_ALL_COMPANIES_HQL = "from Company";

    @Autowired
    public CompanyDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    /**
     * Deletes the entry of the given company as well as all of its computers.
     *
     * @param company
     *            a Company object, representing the entry that must be deleted
     */
    @Transactional(rollbackFor = {Exception.class})
    public void delete(Company company) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.delete(company);
        session.getTransaction().commit();
        session.close();
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
    protected String getCountEntriesWhereHQLStatement() {
        return "select count(c) from Company c where name like :searchTerm";
    }

    @Override
    protected String getFindEntryHQLStatement() {
        return "from Company as company where company.id = :id";
    }

    @Override
    protected String getDoesEntryExistHQLStatement() {
        return "select count(1) from Company where id = :id";
    }

}
