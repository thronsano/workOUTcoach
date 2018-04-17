package com.workOUTcoach.MVC.model;

import com.workOUTcoach.entity.Client;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
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


}