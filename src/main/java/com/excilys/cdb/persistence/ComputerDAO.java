package com.excilys.cdb.persistence;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.excilys.cdb.model.Computer;

/**
 * @author Victor Gambier
 *
 */
@Component("computerDAOBean")
public class ComputerDAO extends DAO<Computer> {

    @Autowired
    public ComputerDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    /**
     * Adds an entry for a new computer.
     *
     * @param computer
     *            a new Computer object, representing the new entry
     */
    public void add(Computer computer) {

        Session session = sessionFactory.openSession();
        session.save(computer);
        session.close();
    }

    /**
     * Updates the database entry for an existing computer.
     *
     * @param computer
     *            a Computer object, representing the updated entry
     */
    public void update(Computer computer) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(computer);
        session.getTransaction().commit();
        session.close();
    }

    /**
     * Deletes the entry of the given computer.
     *
     * @param computer
     *            a Computer object, representing the entry that must be deleted
     */
    public void delete(Computer computer) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.delete(computer);
        session.getTransaction().commit();
        session.close();
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
    public List<Computer> findMatchesWithinRange(int limit, int offset, String searchTerm,
            String orderBy) throws PersistenceException {

        // Avoid SQL injections
        if (!orderBy.equals("computer.id") && !orderBy.equals("computer.name")
                && !orderBy.equals("introduced") && !orderBy.equals("discontinued")
                && !orderBy.equals("computer.company.name")) {
            throw new PersistenceException("Invalid column name");
        }

        // We force a LEFT JOIN
        String sql = "from Computer computer left join fetch computer.company where computer.name like :searchTerm "
                + "OR computer.company.name like :searchTerm order by " + orderBy;

        Session session = sessionFactory.openSession();

        @SuppressWarnings("unchecked")
        Query<Computer> query = session.createQuery(sql);
        query.setParameter("searchTerm", "%" + searchTerm + "%");
        query.setMaxResults(limit);
        query.setFirstResult(offset);
        List<Computer> computers = query.list();

        session.close();

        return computers;
    }

    @Override
    protected String getCountEntriesWhereHQLStatement() {
        return "select count(computer) from Computer computer left join computer.company "
                + "where computer.name like :searchTerm OR computer.company.name like :searchTerm";
    }

    @Override
    protected String getFindEntryHQLStatement() {
        return "from Computer as computer where computer.id = :id";
    }

    @Override
    protected String getDoesEntryExistHQLStatement() {
        return "select count(1) from Computer where id = :id";
    }
}
