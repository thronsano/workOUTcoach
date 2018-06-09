package com.workOUTcoach.MVC.model;

import com.workOUTcoach.entity.Cycle;
import com.workOUTcoach.utility.Logger;
import javassist.NotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CycleModel {

    @Autowired
    private SessionFactory sessionFactory;

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

    public Cycle getCycleByClientId(int clientId) throws NotFoundException {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Cycle cycle;

        Query query = session.createQuery("from Cycle where clientID=:clientId");

        query.setParameter("clientId", clientId);
        cycle = (Cycle) query.uniqueResult();

        session.getTransaction().commit();
        session.close();

        if (cycle == null)
            throw new NotFoundException("Cycle not found!");
        return cycle;
    }
}
