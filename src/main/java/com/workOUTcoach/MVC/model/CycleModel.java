package com.workOUTcoach.MVC.model;

import com.workOUTcoach.entity.Client;
import com.workOUTcoach.entity.Cycle;
import com.workOUTcoach.utility.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

@Repository
public class CycleModel {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    @Lazy
    private ClientModel clientModel;

    public void saveNewCycle(Cycle cycle) {
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            session.save(cycle);
        } catch (Exception e) {
            Logger.logError("Exception during saving new cycle into database");
        } finally {
            session.getTransaction().commit();
            session.close();
        }

    }

    public Cycle getCycleByClientId(int clientId) {
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            Client client = clientModel.getClientById(clientId);
            Query query = session.createQuery("from Cycle where client=:client");
            query.setParameter("client", client);

            return (Cycle) query.uniqueResult();
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }
}
