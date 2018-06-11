package com.workOUTcoach.MVC.model;

import com.workOUTcoach.entity.Cycle;
import com.workOUTcoach.entity.Scheme;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

    @Test
    public void getSchemesList() {
        List<Scheme> schemeList;
        Cycle cycle = new Cycle(1, "Rzezba");
        Cycle cycle2 = new Cycle(2, "Rzezba Lekka");
        Scheme testScheme = new Scheme("Nogi dla leniwych", cycle, 1);
        Scheme testScheme2 = new Scheme("Nogi dla zaawasowanych", cycle, 2);
        Scheme testScheme3 = new Scheme("Rece dla leniwych", cycle2, 1);

        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.save(testScheme);
            session.save(testScheme2);
            session.save(testScheme3);
        } finally {
            session.getTransaction().commit();
            session.close();
        }
        schemeList = schemeModel.schemeList();
        assertTrue(schemeList.size() >= 3);
    }
}