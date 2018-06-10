package com.workOUTcoach.MVC.model;

import com.workOUTcoach.entity.*;
import com.workOUTcoach.utility.Logger;
import javassist.NotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SchemeModel {
    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private ClientModel clientModel;

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

        Scheme scheme = null;
        try {
            Query query = session.createQuery("from Scheme where id=:schemeID");

            query.setParameter("schemeID", id);
            scheme = (Scheme) query.uniqueResult();
        } catch (Exception e) {
            Logger.logError("Exception during access to scheme");
            session.getTransaction().commit();
            session.close();

            if (scheme == null)
                throw new NotFoundException("Scheme not found!");
        }
        return scheme;
    }

    public void addSchemeToCycle(int clientId, int schemeId) throws Exception {
        if (schemeId == -1)
            throw new Exception("Scheme hasn't been chosen!");

        Cycle cycle = cycleModel.getCycleByClientId(clientId);
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        try {
            Scheme scheme = session.get(Scheme.class, schemeId);
            scheme.setCycle(cycle);
            List<Scheme> schemesList = getUsedSchemeListByClient(clientId);
            if (schemesList.isEmpty())
                scheme.setSequence(1);
            else {
                int maxSequence = 0;
                for (Scheme s : schemesList) {
                    if (s.getSequence() > maxSequence) {
                        maxSequence = s.getSequence();
                    }
                }
                scheme.setSequence(maxSequence + 1);
            }
        } catch (Exception e) {
            Logger.logError("Exception during adding new scheme into cycle");
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    public List<Scheme> getUnusedSchemeListByClient(int clientId) {
        List<Scheme> schemes = null;
        Client client = clientModel.getClientById(clientId);

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        try {
            Query query = session.createQuery("from Scheme where client=:currentClient AND sequence=0");
            query.setParameter("currentClient", client);

            schemes = query.list();
        } catch (Exception e) {
            Logger.logError("Exception during an access to cycle");
        } finally {
            session.getTransaction().commit();
            session.close();
        }
        return schemes;
    }

    public List<Scheme> getUsedSchemeListByClient(int clientId) {
        List<Scheme> schemes = null;
        Client client = clientModel.getClientById(clientId);

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        try {
            Query query = session.createQuery("from Scheme where client=:currentClient AND sequence is not 0");
            query.setParameter("currentClient", client);

            schemes = query.list();
        } catch (Exception e) {
            Logger.logError("Exception during access to cycle");
        } finally {
            session.getTransaction().commit();
            session.close();
        }
        return schemes;
    }


    @SuppressWarnings("unchecked")
    public List<Exercise> listExercise(int appointmentID) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        try {
            Query query = session.createQuery("from Appointment as app where app.id =:appID AND app.client.coachEmail =:coachEmail");
            query.setParameter("appID", appointmentID);
            query.setParameter("coachEmail", auth.getName());

            Appointment appointment = (Appointment) query.uniqueResult();

            return appointment.getScheme().getExerciseList();
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    public void removeSchemeFromCycle(int clientId, int schemeId) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        int numberOfSequence;
        try {
            Scheme scheme = session.get(Scheme.class, schemeId);
            numberOfSequence = scheme.getSequence();
            scheme.setSequence(0);
        } finally {
            session.getTransaction().commit();
            session.close();
        }
        List<Scheme> schemesList = getSchemeListBySequenceBiggerThan(clientId, numberOfSequence);
        System.out.println("Przed zmiana");
        for (Scheme s : schemesList) {
            System.out.println("liiiiiiiista             "+s.getSequence());
        }
        System.out.println("Po zmianie");

        for (Scheme s : schemesList) {
            changeSequenceByOneBackwards(s);
        }
    }

    private void changeSequenceByOneBackwards(Scheme s) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            Scheme scheme = session.get(Scheme.class, s.getId());
            scheme.setSequence(scheme.getSequence() - 1);
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    public List<Scheme> getSchemeListBySequenceBiggerThan(int clientId, int numberOfSequence) {
        List<Scheme> schemes = null;
        Client client = clientModel.getClientById(clientId);

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        try {
            Query query = session.createQuery("from Scheme where client=:currentClient AND sequence>:biggerSequence");
            query.setParameter("currentClient", client);
            query.setParameter("biggerSequence", numberOfSequence);
            schemes = query.list();
        } catch (Exception e) {
            Logger.logError("Exception during acces to list of schemes");
        } finally {
            session.getTransaction().commit();
            session.close();
        }
        return schemes;
    }


}
