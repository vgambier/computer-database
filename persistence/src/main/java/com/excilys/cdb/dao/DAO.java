package com.excilys.cdb.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

/**
 * @author Victor Gambier
 *
 * @param <T>
 *            should be an entity from the model package, e.g.: Computer, Page, etc.
 */
public abstract class DAO<T> {

    protected SessionFactory sessionFactory;

    public DAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Count the number of entries in the database. Works for both the computer database and the
     * company database.
     *
     * @return the number of entries in the database
     * @throws PersistenceException
     */
    public int countEntries() {
        return countEntriesMatching("");
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
    public int countEntriesMatching(String searchTerm) {

        Session session = sessionFactory.openSession();
        @SuppressWarnings("unchecked")
        Query<Long> query = session.createQuery(getCountEntriesWhereHQLStatement());
        query.setParameter("searchTerm", "%" + searchTerm + "%");
        long count = query.uniqueResult();

        session.close();

        return (int) count;
    }

    /**
     * Finds an entity in the database, and returns a corresponding Java object.
     *
     * @param id
     *            the id of the entity in the database
     * @return an entity model, with the same attributes as the entry in the database
     */
    public T find(int id) {

        Session session = sessionFactory.openSession();

        @SuppressWarnings("unchecked")
        Query<T> query = session.createQuery(getFindEntryHQLStatement());
        query.setParameter("id", id);
        T model = query.uniqueResult();

        session.close();
        return model;
    }

    /**
     * Checks if there is an entry of the given id number in the table.
     *
     * @param id
     *            the id of the entry to be checked
     * @return true if and only if there is an entry
     */
    public boolean doesEntryExist(int id) {

        Session session = sessionFactory.openSession();

        @SuppressWarnings("unchecked")
        Query<Long> query = session.createQuery(getDoesEntryExistHQLStatement());
        query.setParameter("id", id);
        long count = query.uniqueResult();

        session.close();

        return count == 1L;
        // this is true if the query returned one entry
        // since the result set returned either 0 or 1 entry

    }

    protected abstract String getCountEntriesWhereHQLStatement();
    protected abstract String getDoesEntryExistHQLStatement();
    protected abstract String getFindEntryHQLStatement();

}
