package com.workOUTcoach.MVC.model;

import com.workOUTcoach.entity.Cycle;
import com.workOUTcoach.entity.Scheme;
import com.workOUTcoach.utility.Logger;
import javassist.NotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SchemeModel {
    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private CycleModel cycleModel;

    public List<Scheme> schemeList() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Query query = session.createQuery("from Scheme");
        List<Scheme> schemes = query.list();

        session.getTransaction().commit();
        session.close();

        return schemes;
    }

    public Scheme getSchemeById(int id) throws NotFoundException {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Scheme scheme;

        Query query = session.createQuery("from Scheme where id=:schemeID");

        query.setParameter("schemeID", id);
        scheme = (Scheme) query.uniqueResult();

        session.getTransaction().commit();
        session.close();

        if (scheme == null)
            throw new NotFoundException("Client not found!");
        return scheme;
    }

    public boolean addCycleToScheme(int clientId, int schemeId) throws Exception {
        Cycle cycle = cycleModel.getCycleByClientId(clientId);
        Session session = sessionFactory.openSession();
        if (schemeId == -1)
            throw new Exception("Scheme hasn't been chosen!");

        try {
            session.beginTransaction();
            Scheme scheme = session.get(Scheme.class, schemeId);
            scheme.setCycle(cycle);

        } catch (Exception e) {
            Logger.logError("Exception during adding new scheme into cycle");
            return false;
        } finally {
            session.getTransaction().commit();
            session.close();
        }

        return true;
    }
}
