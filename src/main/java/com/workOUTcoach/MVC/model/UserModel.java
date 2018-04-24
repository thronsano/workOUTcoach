package com.workOUTcoach.MVC.model;

import com.workOUTcoach.entity.Authority;
import com.workOUTcoach.entity.User;
import com.workOUTcoach.utility.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserModel {
    @Autowired
    private SessionFactory sessionFactory;

    public User getUserByUsername(String username) {
        return sessionFactory.openSession().get(User.class, username);
    }
    public User getUserByEmail(String email){ return sessionFactory.openSession().get(User.class, email); }

    public boolean addUser(User user, Authority authority){
        try {


            Session session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
            session.close();
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(authority);
            session.getTransaction().commit();
        }catch (Exception e){
            Logger.logError("Exception during adding new user to database");
            return false;
        }
        return true;
    }

}
