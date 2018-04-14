package com.workOUTcoach;

import com.workOUTcoach.entity.User;
import com.workOUTcoach.utility.DatabaseConnector;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WorkOuTcoachApplication {

    public static void main(String[] args) {
        //SpringApplication.run(WorkOuTcoachApplication.class, args);
        //DatabaseConnector databaseConnector = new DatabaseConnector();

        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(User.class)
                .buildSessionFactory();


        Session session = factory.getCurrentSession();
        User user = new User("User2", "password", "John", "Doe", "Who am I?", "An user", "email2@email.com");

        session.beginTransaction();

        System.out.println("Saving...");
        session.save(user);

        session.getTransaction().commit();
        factory.close();
    }

}
