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


    public void setAppointment(int id, LocalDateTime startDate, LocalDateTime endDate, boolean cyclic, boolean scheme) throws Exception {
        if(endDate.isBefore(startDate))
            throw new Exception("Appointment ends before it starts!");

        if (verifyTimeline(startDate, endDate)) {
            Client client = clientModel.getClientById(id);
            Appointment appointment;

            appointment = new Appointment(startDate, endDate, client, cyclic);

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
                "(app.startDate <=:newStartDate AND app.endDate >=:newStartDate) OR " +
                "(app.startDate >=:newStartDate AND app.endDate <=:newEndDate) OR " +
                "(app.startDate <=:newEndDate AND app.endDate >=:newEndDate)" +
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
