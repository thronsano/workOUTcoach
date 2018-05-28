package com.workOUTcoach.MVC.model;

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


    public boolean saveNewClient(Client client) {
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            session.save(client);
        } catch (Exception e) {
            Logger.logError("Exception during saving new client into database");
            return false;
        } finally {
            session.getTransaction().commit();
            session.close();
        }

        return true;
    }

    public Client getClientById(int id) throws NullPointerException, NumberFormatException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Client client;

        Query query = session.createQuery("from Client where coachEmail =:email AND id=:clientID");

        query.setParameter("email", auth.getName());
        query.setParameter("clientID", id);
        client = (Client) query.uniqueResult();

        session.getTransaction().commit();
        session.close();

        if (client == null)
            throw new NullPointerException("Client not found!");

        return client;
    }

    public void editClientDetails(int clientID, String newValueOfElement, String elementToChange) throws Exception {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

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
                throw new Exception("Chosen incorrect parameter to edit");
        }

        session.beginTransaction();

        session.update(client);

        session.getTransaction().commit();
        session.close();

        if (client == null)
            throw new NullPointerException("Client not found!");
    }


    public boolean deleteById(int id) throws NullPointerException, NumberFormatException {
        Client client = getClientById(id);

        if (client == null)
            throw new NullPointerException("Client not found!");

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        try {
            session.delete(client);
        } catch (Exception e) {
            Logger.logError("Exception during deleting client from database");
            return false;
        } finally {
            session.getTransaction().commit();
            session.close();
        }

        return true;
    }


    public boolean isActiveById(int id) throws NullPointerException, NumberFormatException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Client client;
        Boolean isActive = false;

        Query query = session.createQuery("from Client where coachEmail =:email AND id=:clientID");
        query.setParameter("email", auth.getName());
        query.setParameter("clientID", id);
        client = (Client) query.uniqueResult();

        isActive = client.isActive();

        session.getTransaction().commit();
        session.close();

        if (client == null)
            throw new NullPointerException("Client not found!");

        return isActive;
    }

    public boolean setActiveById(int id) throws NullPointerException, NumberFormatException {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Session session = sessionFactory.openSession();
            session.beginTransaction();

            Client client;

            Query query = session.createQuery("from Client where id=:clientID");
            query.setParameter("clientID", id);
            client = (Client) query.uniqueResult();

            client.setActive(true);

            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            Logger.logError("Exception during saving user into database");
            return false;
        }

        return true;
    }

    public boolean archiveById(int id) throws NullPointerException, NumberFormatException {
        try {
            Session session = sessionFactory.openSession();
            Client client;

            session.beginTransaction();

            Query query = session.createQuery("from Client where id=:clientID");
            query.setParameter("clientID", id);
            client = (Client) query.uniqueResult();

            client.setActive(false);

            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            Logger.logError("Exception during saving user into database");
            return false;
        }

        return true;
    }

    public String createClient(String name, String surname, String coachEmail, String gymName, String goal, String condition, String phoneNumber) {
        if (validateString(name) && validateString(surname) && validateString(coachEmail)) {
            Client client = new Client(name, surname, coachEmail, gymName, goal, condition, true, phoneNumber);

            if (saveNewClient(client))
                return "correct";
            else
                return "databaseError";

        } else
            return "dataError";

    }

    private boolean validateString(String text) {
        return text != null && !text.isEmpty();
    }
}