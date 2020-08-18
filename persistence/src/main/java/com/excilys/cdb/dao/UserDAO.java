package com.excilys.cdb.dao;

import com.excilys.cdb.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository("userDAOBean")
public class UserDAO {

    private final static String SELECT_ALL_USERS_HQL = "FROM User";
    private final static String GET_USER_BY_USERNAME = "FROM User WHERE username=:username";

    private final SessionFactory sessionFactory;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserDAO(SessionFactory sessionFactory, PasswordEncoder passwordEncoder) {
        this.sessionFactory = sessionFactory;
        this.passwordEncoder = passwordEncoder;
    }


    public User getByUserName(String username) {
        Session session = sessionFactory.openSession();
        TypedQuery<User> query = session.createQuery(GET_USER_BY_USERNAME,User.class).setParameter("username",username);
        User result = query.getSingleResult();
        session.close();
        return result;
    }


    public List<User> listAll() {

        Session session = sessionFactory.openSession();

        TypedQuery<User> query = session.createQuery(SELECT_ALL_USERS_HQL,User.class);
        List<User> users = query.getResultList();

        session.close();
        return users;
    }

    @Transactional
    public void add (String username, String password){
        User newUser = new User(username,passwordEncoder.encode(password));
        Session session = sessionFactory.openSession();
        Transaction t = session.beginTransaction();
        session.save(newUser);
        t.commit();
        session.close();
    }


}
