package com.excilys.cdb.dao;

import com.excilys.cdb.model.Authority;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.TypedQuery;
import java.util.List;

@Component("authorityDAOBean")
public class AuthorityDAO {

    private static final String SELECT_ALL_USERS_HQL = "FROM Authority";

    private final SessionFactory sessionFactory;

    @Autowired
    public AuthorityDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Authority> listAll() {

        Session session = sessionFactory.openSession();

        TypedQuery<Authority> query = session.createQuery(SELECT_ALL_USERS_HQL,Authority.class);
        List<Authority> authorityList = query.getResultList();

        session.close();
        return authorityList;
    }
}
