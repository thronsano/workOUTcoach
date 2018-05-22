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
    public List<Client> getAllUserClients() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            Query query = session.createQuery("from Client where coachEmail =:email");
            query.setParameter("email", auth.getName());

            return query.list();
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    public boolean saveNewClient(Client client) {
        try {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(client);
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            Logger.logError("Exception during saving new client into database");
            return false;
        }
        return true;
    }

    public Client getClient(String id) throws NullPointerException, NumberFormatException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Session session = sessionFactory.openSession();
        int clientID;
        Client client;

        clientID = Integer.parseInt(id);

        session.beginTransaction();

        Query query = session.createQuery("from Client where coachEmail =:email AND id=:clientID");
        query.setParameter("email", auth.getName());
        query.setParameter("clientID", clientID);
        client = (Client) query.uniqueResult();

        session.getTransaction().commit();
        session.close();

        if (client == null)
            throw new NullPointerException("Client not found!");

        return client;
    }

    public String createClient(String name, String surname, String coachEmail, String gymName, String goal, String condition, String phoneNumber) {

        if (validateString(name) && validateString(surname)) {
            Client client = new Client(name, surname, coachEmail, gymName, goal, condition, true, phoneNumber);

            if (saveNewClient(client)) {
                return "correct";
            }else {
                return "databaseError";
            }
        } else {
            return "dataError";
        }
    }

    private boolean validateString(String text) {
        return text != null && !text.isEmpty();
    }
}