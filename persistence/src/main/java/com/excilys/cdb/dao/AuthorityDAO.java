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

    private static final String GET_ALL_AUTHORITIES_HQL = "FROM Authority";
    private static final String GET_AUTHORITY = "FROM Authority where authority=:authority";

    private final SessionFactory sessionFactory;

    @Autowired
    public AuthorityDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Authority getAuthority (String authority){
        Session session = sessionFactory.openSession();
        TypedQuery<Authority> query = session.createQuery(GET_AUTHORITY,Authority.class).setParameter("authority",authority);
        Authority result = query.getSingleResult();
        session.close();
        return result;
    }

    public List<Authority> listAll() {

        Session session = sessionFactory.openSession();

        TypedQuery<Authority> query = session.createQuery(GET_ALL_AUTHORITIES_HQL,Authority.class);
        List<Authority> authorityList = query.getResultList();

        session.close();
        return authorityList;
    }
}
