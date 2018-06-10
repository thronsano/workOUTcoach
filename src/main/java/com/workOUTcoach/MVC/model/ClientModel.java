package com.workOUTcoach.MVC.model;

import com.workOUTcoach.entity.Cycle;
import com.workOUTcoach.entity.Scheme;
import com.workOUTcoach.utility.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
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
        session.beginTransaction();

        try {
            session.save(client);
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    public Client getClientById(int id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        try {
            Query query = session.createQuery("from Client where coachEmail =:email AND id=:clientID");
            query.setParameter("email", auth.getName());
            query.setParameter("clientID", id);

            return (Client) query.uniqueResult();
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    public void editClientDetails(int clientID, String newValueOfElement, String elementToChange) throws Exception {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        try {
            Query query = session.createQuery("from Client where id=:clientID");
            query.setParameter("clientID", clientID);

            Client client = (Client) query.uniqueResult();

            session.getTransaction().commit();

            switch (elementToChange) {
                case "goal":
                    client.setGoal(newValueOfElement);
                    break;
                case "health":
                    client.setHealthCondition(newValueOfElement);
                    break;
                case "phone":
                    client.setPhoneNumber(newValueOfElement);
                    break;
                case "gym":
                    client.setGymName(newValueOfElement);
                    break;
                default:
                    throw new Exception("Chosen an incorrect parameter to edit!");
            }

            session.beginTransaction();
            session.update(client);
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }


    public void deleteById(int clientId) {
        Client client = getClientById(clientId);

        if (client == null)
            throw new NullPointerException("Client not found!");

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        try {
            session.delete(client);
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }


    public boolean isActiveById(int clientId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        try {
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

    public void setActiveById(int clientId) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        try {
            Query query = session.createQuery("from Client where id=:clientID");
            query.setParameter("clientID", clientId);
            Client client = (Client) query.uniqueResult();

            client.setActive(true);
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    public void archiveById(int id) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        try {
            Query query = session.createQuery("from Client where id=:clientID");
            query.setParameter("clientID", id);
            Client client = (Client) query.uniqueResult();

            client.setActive(false);
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
}