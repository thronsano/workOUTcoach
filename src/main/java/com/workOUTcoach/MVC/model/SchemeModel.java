package com.workOUTcoach.MVC.model;

import com.workOUTcoach.entity.*;
import com.workOUTcoach.utility.Logger;
import javassist.NotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class SchemeModel {
    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private ClientModel clientModel;

    @Autowired
    private CycleModel cycleModel;

    @Autowired
    @Lazy
    private AppointmentModel appointmentModel;

    public Scheme getSchemeById(int id) {
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            Query query = session.createQuery("from Scheme where id=:schemeID");
            query.setParameter("schemeID", id);

            return (Scheme) query.uniqueResult();
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    public void addSchemeToCycle(int clientId, int schemeId) throws Exception {
        if (schemeId == -1)
            throw new Exception("Scheme hasn't been chosen!");

        Cycle cycle = cycleModel.getCycleByClientId(clientId);
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            Scheme scheme = session.get(Scheme.class, schemeId);

            scheme.setCycle(cycle);
            List<Scheme> schemesList = getUsedSchemeListByClient(clientId);

            if (schemesList.isEmpty())
                scheme.setSequence(1);
            else {
                int maxSequence = 0;
                for (Scheme s : schemesList)
                    if (s.getSequence() > maxSequence)
                        maxSequence = s.getSequence();

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

        try {
            session.beginTransaction();
            Query query = session.createQuery("from Scheme as s where s.cycle.client=:currentClient AND sequence=0");
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

        try {
            session.beginTransaction();
            Query query = session.createQuery("from Scheme as s where s.cycle.client=:currentClient AND sequence != 0 order by sequence");
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
    public List<Exercise> getExerciseList(int appointmentID) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
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
        int numberOfSequence;

        try {
            session.beginTransaction();
            Scheme scheme = session.get(Scheme.class, schemeId);
            numberOfSequence = scheme.getSequence();
            scheme.setSequence(0);
        } finally {
            session.getTransaction().commit();
            session.close();
        }

        List<Scheme> schemesList = getSchemeListBySequenceBiggerThan(clientId, numberOfSequence);

        for (Scheme s : schemesList)
            changeSequenceByOneBackwards(s);
    }

    private void changeSequenceByOneBackwards(Scheme s) {
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            Scheme scheme = session.get(Scheme.class, s.getId());
            scheme.setSequence(scheme.getSequence() - 1);
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    public Scheme getSchemeBySequence(int sequence, int clientId) {
        Session session = sessionFactory.openSession();
        Client client = clientModel.getClientById(clientId);

        try {
            session.beginTransaction();
            Query query = session.createQuery("from Scheme as schm where schm.sequence=:seq and schm.cycle.client=:client");
            query.setParameter("seq", sequence);
            query.setParameter("client", client);

            return (Scheme) query.uniqueResult();
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    public List<Scheme> getSchemeListBySequenceBiggerThan(int clientId, int numberOfSequence) {
        List<Scheme> schemes = null;
        Client client = clientModel.getClientById(clientId);
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            Query query = session.createQuery("from Scheme as s where s.cycle.client=:currentClient AND sequence>:biggerSequence");
            query.setParameter("currentClient", client);
            query.setParameter("biggerSequence", numberOfSequence);

            schemes = query.list();
        } catch (Exception e) {
            Logger.logError("Exception during access to list of schemes");
        } finally {
            session.getTransaction().commit();
            session.close();
        }

        return schemes;
    }

    public List<Scheme> getSchemeListByClientId(int clientId) {
        Client client = clientModel.getClientById(clientId);
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            Query query = session.createQuery("from Scheme as s where s.cycle.client=:currentClient order by sequence");
            query.setParameter("currentClient", client);

            return query.list();
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }


    public List<Scheme> listSchemesByAppointmentId(int appointmentID) {
        Appointment appointment = appointmentModel.getAppointmentById(appointmentID);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        try {
            Query query = session.createQuery("from Scheme as scheme where scheme.cycle.client.id =:clientID AND scheme.cycle.client.coachEmail =:email");
            query.setParameter("clientID", appointment.getClient().getId());
            query.setParameter("email", auth.getName());

            return query.list();
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    public void editTitleById(int schemeID, String title) throws Exception {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            Scheme scheme = session.get(Scheme.class, schemeID);
            scheme.setTitle(title);
            System.out.println("-------------------------------------" + scheme.getTitle());
        }catch (Exception e){
            System.out.println("-----------------------------------------------"+e.getMessage());
            throw new Exception("Couldn't change title!");
        }finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    public Scheme getPriorScheme(LocalDateTime newAppointmentDate, int clientId) throws NotFoundException {
        List<Appointment> appointments = appointmentModel.getAppointmentListByClientId(clientId).stream().filter(a -> !a.getIsCancelled() && a.isPartOfCycle()).collect(Collectors.toList());
        Appointment closestApp = null;

        if (appointments.size() > 0) {
            appointments.sort(Comparator.comparing(Appointment::getStartDate));

            if (appointments.get(0).getStartDate().isBefore(newAppointmentDate)) {
                closestApp = appointments.get(0);

                for (int i = 1; i < appointments.size(); i++) {
                    if (appointments.get(i).getStartDate().isBefore(newAppointmentDate))
                        closestApp = appointments.get(i);
                    else
                        break;
                }
            }
        }

        if (closestApp == null) {
            throw new NotFoundException("No previous closest appointment!");
        } else {
            return closestApp.getScheme();
        }
    }
}
