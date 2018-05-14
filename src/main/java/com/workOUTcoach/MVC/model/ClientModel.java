package com.workOUTcoach.MVC.model;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.workOUTcoach.entity.Client;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public class ClientModel {

    @Autowired
    private SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    public List<Client> getAllClients() {
        return sessionFactory.openSession().createQuery("from Client").list();
    }

    @SuppressWarnings("unchecked")
    public List<Client> getAllUserClients() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return sessionFactory.openSession().createQuery("from Client where coachEmail = '" + auth.getName() + "'").list();
    }
}