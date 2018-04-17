package com.workOUTcoach.MVC.model;

import com.workOUTcoach.entity.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserModel {
    @Autowired
    private SessionFactory sessionFactory;

    public User getUserByUsername(String username) {
        return sessionFactory.getCurrentSession().get(User.class, username);
    }
}
