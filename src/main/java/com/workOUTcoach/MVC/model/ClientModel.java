package com.workOUTcoach.MVC.model;

import com.workOUTcoach.entity.Appointment;
import com.workOUTcoach.entity.Cycle;
import com.workOUTcoach.utility.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.workOUTcoach.entity.Client;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public class ClientModel {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private CycleModel cycleModel;

    @SuppressWarnings("unchecked")
    public List<Client> getArchivedUserClients() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            Query query = session.createQuery("from Client where coachEmail =:email and isActive=false");
            query.setParameter("email", auth.getName());

            return query.list();
        } finally {
            if (session.getTransaction().getStatus().equals(TransactionStatus.ACTIVE))
                session.getTransaction().commit();
            session.close();
        }
    }

    public List<Client> getActiveUserClients() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            Query query = session.createQuery("from Client where coachEmail =:email and isActive=true");
            query.setParameter("email", auth.getName());

            return query.list();
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    public void saveNewClient(Client client) {
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            session.save(client);
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    public Client getClientById(int clientId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            Query query = session.createQuery("from Client where coachEmail =:email AND id=:clientID");
            query.setParameter("email", auth.getName());
            query.setParameter("clientID", clientId);

            return (Client) query.uniqueResult();
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    public void editClientDetails(int clientId, String goal, String healthCondition, String phoneNumber, String gymName) throws Exception {
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            Query query = session.createQuery("from Client where id=:clientId");
            query.setParameter("clientId", clientId);
            Client client = (Client) query.uniqueResult();

            client.setGoal(goal);
            client.setHealthCondition(healthCondition);
            client.setPhoneNumber(phoneNumber);
            client.setGymName(gymName);

            session.beginTransaction();
            session.update(client);
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    public void deleteById(int clientId) {
        Client client;
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            client = session.load(Client.class, clientId);
            session.delete(client);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    public boolean isActiveById(int clientId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            Query query = session.createQuery("from Client where coachEmail =:email AND id=:clientID");
            query.setParameter("email", auth.getName());
            query.setParameter("clientID", clientId);
            Client client = (Client) query.uniqueResult();

            return client.isActive();
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    public void activateById(int clientId) {
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            Query query = session.createQuery("from Client where id=:clientID");
            query.setParameter("clientID", clientId);
            Client client = (Client) query.uniqueResult();

            client.setActive(true);
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    public void archiveById(int clientId) {
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            Query query = session.createQuery("from Client where id=:clientID");
            query.setParameter("clientID", clientId);

            Client client = (Client) query.uniqueResult();
            client.setActive(false);

            deleteFutureAppointments(clientId);
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    public void deleteFutureAppointments(int clientId) {
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            Query appointmentsToDelete = session.createQuery("from Appointment as app where app.startDate >=:currentDate and app.client.id=:clientId");
            appointmentsToDelete.setParameter("currentDate", LocalDateTime.now());
            appointmentsToDelete.setParameter("clientId", clientId);
            List<Appointment> appointments = appointmentsToDelete.list();

            for (int i = 0; i < appointments.size(); i++)
                session.delete(appointments.get(i));
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    public void createClient(String name, String surname, String coachEmail, String gymName, String goal, String condition, String phoneNumber) throws Exception {
        if (validateString(name) && validateString(surname) && validateString(coachEmail)) {
            Client client = new Client(name, surname, coachEmail, gymName, goal, condition, true, phoneNumber);

            saveNewClient(client);
            Cycle cycle = new Cycle(client);

            cycleModel.saveNewCycle(cycle);
        } else {
            throw new Exception("You have to fill name and surname field!");
        }
    }

    private boolean validateString(String text) {
        return text != null && !text.isEmpty();
    }

    public void editProgress(int clientId, int goalValue) throws Exception {
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            Query query = session.createQuery("from Client where id=:clientId");
            query.setParameter("clientId", clientId);
            Client client = (Client) query.uniqueResult();

            if (goalValue > 100)
                goalValue = 100;
            else if (goalValue < 0)
                goalValue = 0;

            client.setGoalValue(goalValue);

            session.update(client);
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }
}