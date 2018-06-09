package com.workOUTcoach.MVC.model;

import com.workOUTcoach.entity.Appointment;
import com.workOUTcoach.entity.Exercise;
import com.workOUTcoach.entity.Scheme;
import com.workOUTcoach.utility.Logger;
import javassist.NotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
            throw new NotFoundException("Scheme not found!");
        return scheme;
    }

    @SuppressWarnings("unchecked")
    public List<Exercise> listExercise(int appointmentID) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        try {
            //Query query = session.createQuery("select (select exerciseList from Scheme ) from Appointment as app where app.id =:appID AND app.client.coachEmail =:coachEmail");
            Query query = session.createQuery("from Appointment as app where app.id =:appID AND app.client.coachEmail =:coachEmail");
            query.setParameter("appID", appointmentID);
            query.setParameter("coachEmail", auth.getName());

            Appointment appointment = (Appointment) query.uniqueResult();

            //Logger.log(query.list().size()+"");

            return appointment.getScheme().getExerciseList();
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

}
