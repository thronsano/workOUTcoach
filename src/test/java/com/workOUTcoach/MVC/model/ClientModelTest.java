package com.workOUTcoach.MVC.model;

import com.workOUTcoach.entity.Client;
import com.workOUTcoach.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClientModelTest {

    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    ClientModel clientModel;

    @Test
    public void saveNewClientTest() {
        User user = new User("test@test.com", "TestPassword", "TestCoachName", "TestCoachSurname");
        Client client = new Client("TestName", "TestSurname", user.getEmail(), "Cracow Gym", "strength", "healthy", true, "696-969-696");

        Session session = sessionFactory.openSession();

        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();

        clientModel.saveNewClient(client);

        session.beginTransaction();
        Query query = session.createQuery("from Client where coachEmail =:email");
        query.setParameter("email", user.getEmail());
        Client savedClient = (Client) query.uniqueResult();
        session.getTransaction().commit();

        assertEquals(client.getCoachEmail(), savedClient.getCoachEmail());
        assertEquals(client.getGoal(), savedClient.getGoal());
        assertEquals(client.getGymName(), savedClient.getGymName());
        assertEquals(client.getHealthCondition(), savedClient.getHealthCondition());
        assertEquals(client.getPhoneNumber(), savedClient.getPhoneNumber());
        assertEquals(client.isActive(), savedClient.isActive());
        assertEquals(client.getCoachEmail(), savedClient.getCoachEmail());


        assertNotNull(savedClient.getName());
        assertNotNull(savedClient.getSurname());
        assertNotSame("", savedClient.getName());
        assertNotSame("", savedClient.getSurname());

        session.beginTransaction();
        session.delete(savedClient);
        session.getTransaction().commit();

        session.beginTransaction();
        session.delete(user);
        session.getTransaction().commit();

        session.close();
    }

    @Test(expected = Exception.class)
    public void createClientTest() throws Exception {
        clientModel.createClient("", "", "", "", "", "", "");
    }
}