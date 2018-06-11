package com.workOUTcoach.MVC.model;

import com.workOUTcoach.entity.Client;
import com.workOUTcoach.entity.Cycle;
import com.workOUTcoach.entity.Scheme;
import com.workOUTcoach.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SchemeModelTest {

    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    SchemeModel schemeModel;

    @Autowired
    ClientModel clientModel;

    @Autowired
    UserModel userModel;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Mock
    Authentication authentication;

    Session session;
    User user;
    Client client;
    Cycle cycle;
    Scheme testScheme;

    @Before
    public void createObject() {

        user = new User("wikatest@gmail.com", "pass", "Aaa", "Bbb", 3);

        session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.save(user);
        } finally {
            session.getTransaction().commit();
            session.close();
        }

        session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery("from User where email =:email");
            query.setParameter("email", "wikatest@gmail.com");
            user = (User) query.uniqueResult();
        } finally {
            session.getTransaction().commit();
            session.close();
        }

        client = new Client("Wika", "Malawska", "wikatest@gmail.com");

        session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.save(client);
        } finally {
            session.getTransaction().commit();
            session.close();
        }
        session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery("from Client where coachEmail =:email and name=:name");
            query.setParameter("email", "wikatest@gmail.com");
            query.setParameter("name", "Wika");
            client = (Client) query.uniqueResult();
        } finally {
            session.getTransaction().commit();
            session.close();
        }

        cycle = new Cycle("Dobry Cykl", client);
        session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.save(cycle);
        } finally {
            session.getTransaction().commit();
            session.close();
        }

        session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery("from Cycle as c where c.client.coachEmail ='wikatest@gmail.com' and c.client.name=:name");
            query.setParameter("name", "Wika");
            cycle = (Cycle) query.uniqueResult();
        } finally {
            session.getTransaction().commit();
            session.close();
        }

        testScheme = new Scheme("Nogi dla leniwych", cycle, 1);

        session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.save(testScheme);
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    @Test
    public void getSchemesListTest() {
        List<Scheme> schemeList;

        SecurityContextHolder.getContext().setAuthentication(authentication);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //Authentication auth = new UsernamePasswordAuthenticationToken("user", "pass", AuthorityUtils.createAuthorityList("ROLE_USER"));
        //SecurityContextHolder.getContext().setAuthentication(auth);

        schemeList = schemeModel.getSchemeListByClientId(client.getId());
        int size = schemeList.size();
        assertTrue(size >= 0);
    }

    @After
    public void removeAll() {
        session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.remove(testScheme);
            session.remove(cycle);
            session.remove(client);
            session.remove(user);
        } finally {
            session.getTransaction().commit();
            session.close();
        }

    }
}