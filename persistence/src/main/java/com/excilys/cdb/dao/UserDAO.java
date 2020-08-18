package com.excilys.cdb.dao;

import com.excilys.cdb.model.Authority;
import com.excilys.cdb.model.User;
import javassist.bytecode.stackmap.BasicBlock;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository("userDAOBean")
public class UserDAO {

    private final static String SELECT_ALL_USERS_HQL = "FROM User";
    private final static String GET_USER_BY_USERNAME = "FROM User WHERE username=:username";

    private final SessionFactory sessionFactory;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityDAO authorityDAO;

    @Autowired
    public UserDAO(SessionFactory sessionFactory, PasswordEncoder passwordEncoder, AuthorityDAO authorityDAO) {
        this.sessionFactory = sessionFactory;
        this.passwordEncoder = passwordEncoder;
        this.authorityDAO = authorityDAO;
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
        Authority authority = authorityDAO.getAuthority("ROLE_USER");
        User newUser = new User(username,passwordEncoder.encode(password), Collections.singleton(authority));
        Session session = sessionFactory.openSession();
        Transaction t = session.beginTransaction();
        session.save(newUser);
        t.commit();
        session.close();
    }


    public void setEnable (String username, String enable){
        User user = getByUserName(username);
        user.setEnabled(enable);
        Session session = sessionFactory.openSession();
        Transaction t = session.beginTransaction();
        session.saveOrUpdate(user);
        t.commit();
        session.close();
    }


    public void manageRole (String username, String[] roles){
        Set<Authority> authorities = null;
        try {
            authorities = Arrays.stream(roles).map(authorityDAO::getAuthority).collect(Collectors.toSet());
        } catch (Exception e){
            //TODO Generate role does not existe (le faire dans le service)
        }
        User user = getByUserName(username);
        user.setAuthoritySet(authorities);
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.saveOrUpdate(user);
        session.getTransaction().commit();
        session.close();

    }


}
