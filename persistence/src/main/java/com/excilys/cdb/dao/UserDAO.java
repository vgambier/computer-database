package com.excilys.cdb.dao;

import com.excilys.cdb.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("userDAOBean")
public class UserDAO  {

    private static final String SELECT_ALL_USERS_HQL = "FROM User";
    private SessionFactory sessionFactory;

    @Autowired
    public UserDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<User> listAll() {

        Session session = sessionFactory.openSession();

        @SuppressWarnings("unchecked")
        Query<User> query = session.createQuery(SELECT_ALL_USERS_HQL);
        List<User> users = query.list();

        session.close();
        return users;
    }


}
