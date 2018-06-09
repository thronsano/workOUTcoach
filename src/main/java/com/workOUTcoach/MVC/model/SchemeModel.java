package com.workOUTcoach.MVC.model;

import com.workOUTcoach.entity.Scheme;
import javassist.NotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SchemeModel {
    @Autowired
    private SessionFactory sessionFactory;

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
}
