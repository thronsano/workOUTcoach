package com.workOUTcoach.MVC.model;

import com.workOUTcoach.entity.Appointment;
import com.workOUTcoach.entity.Client;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public class AppointmentModel {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private ClientModel clientModel;


    public void setAppointment(int id, LocalDateTime startDate, LocalDateTime endDate, boolean repeat, boolean scheme) throws Exception {
        if (verifyTimeline(startDate, endDate)) {
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
        } else
            throw new Exception("Appointment overlaps another one!");
    }

    private boolean verifyTimeline(LocalDateTime localDateTimeStart, LocalDateTime localDateTimeEnd) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Query query = session.createQuery("select count(*) from Appointment as app where (" +
                "(app.startDate >=:newStartDate AND app.endDate >=:newEndDate) OR " +
                "(app.startDate <=:newStartDate AND app.endDate <=:newEndDate) OR " +
                "(app.startDate >=:newStartDate AND app.endDate <=:newEndDate) OR " +
                "(app.startDate <=:newStartDate AND app.endDate >=:newEndDate)" +
                ") AND app.client.coachEmail =:userEmail");

        query.setParameter("newStartDate", localDateTimeStart);
        query.setParameter("newEndDate", localDateTimeEnd);
        query.setParameter("userEmail", SecurityContextHolder.getContext().getAuthentication().getName());

        long count = (long) query.uniqueResult();

        session.getTransaction().commit();
        session.close();

        return count == 0;
    }
}
