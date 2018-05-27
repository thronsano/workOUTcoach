package com.workOUTcoach.MVC.model;

import com.workOUTcoach.entity.Appointment;
import com.workOUTcoach.entity.Client;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;

@Repository
public class AppointmentModel {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private ClientModel clientModel;


    public void setAppointment(int id, LocalDateTime startDate, LocalDateTime endDate, boolean repeat, boolean scheme) {
        Client client = clientModel.getClientById(id);
        Appointment appointment;

        if (repeat)
            appointment = new Appointment(startDate, client);
        else
            appointment = new Appointment(startDate, endDate, client);

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.save(appointment);

        session.getTransaction().commit();
        session.close();
    }

    private boolean verifyTimeline(LocalDateTime localDateTimeStart, LocalDateTime localDateTimeEnd) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Appointment appointment;

        Query query = session.createQuery("from Appointment where startDate >=:startingDate AND endDate <=:endingDate");
        query.setParameter("startingDate", localDateTimeStart);

        session.getTransaction().commit();
        session.close();

        return false;
    }
}
