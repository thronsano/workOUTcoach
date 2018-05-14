package com.workOUTcoach.MVC.controller;

import com.workOUTcoach.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RegistrationControllerTest {

    @Autowired
    RegistrationController registrationController;

    @Mock
    RedirectAttributes redirectAttributes;

    @Autowired
    SessionFactory sessionFactory;

    ModelAndView modelAndView;

    @Before
    public void setUp() {
        modelAndView = new ModelAndView("register");
    }

    @Test
    public void getRegisterPage() {
        assertEquals(modelAndView.getViewName(), registrationController.getRegisterPage(new ModelAndView()).getViewName());
    }

    @Test
    public void createAccount() {
        modelAndView = registrationController.createAccount("testaccount@test.pl", "password", "password", "Name", "Surname", modelAndView, redirectAttributes);
        String query = "from User u where u.email = 'testaccount@test.pl'";
        Session session = sessionFactory.openSession();
        User user = (User)session.createQuery(query).uniqueResult();
        session.close();

//        query = "from Authority";
//        session = sessionFactory.openSession();
//        List<Authority> authorities= session.createQuery(query).getResultList();
//        session.close();

        assertNotNull(user);
        assertEquals("testaccount@test.pl", user.getEmail());
        assertNotNull(user.getPassword());
        assertEquals("Name", user.getName());
        assertEquals("Surname", user.getSurname());

        //@TODO: delete user from database
//        session = sessionFactory.openSession();
//        session.beginTransaction();
//        session.delete(authority);
//        session.getTransaction().commit();
//        session.close();

//        session = sessionFactory.openSession();
//        session.beginTransaction();
//        //session.delete(authority);
//        session.delete(user);
//        session.getTransaction().commit();
//        session.close();
    }
}