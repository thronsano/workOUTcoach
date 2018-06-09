package com.workOUTcoach.MVC.model;

import com.workOUTcoach.entity.Appointment;
import com.workOUTcoach.entity.Client;
import com.workOUTcoach.entity.Scheme;
import com.workOUTcoach.utility.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
public class AppointmentModel {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private SchemeModel schemeModel;

    @Autowired
    private ClientModel clientModel;

    @Autowired
    Environment env;

    public void setAppointment(int id, LocalDateTime startDate, LocalDateTime endDate, boolean cyclic, int repeatAmount, boolean partOfCycle, int schemeId) throws Exception {
        if (endDate.isBefore(startDate))
            throw new Exception("Appointment ends before it starts!");

        if (cyclic && repeatAmount <= 0)
            throw new Exception("Incorrect repetition amount!");

        if (!partOfCycle && schemeId == -1)
            throw new Exception("Scheme hasn't been chosen!");

        int batch_size = Integer.parseInt(env.getProperty("hibernate.jdbc.batch_size"));
        Client client = clientModel.getClientById(id);
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        try {
            for (int i = 0; i < repeatAmount; i++) {
                LocalDateTime newStartDate = startDate.plusWeeks(i);
                LocalDateTime newEndDate = endDate.plusWeeks(i);

                if (!timelineClear(newStartDate, newEndDate)) {
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                    throw new Exception("Appointment from " + newStartDate.format(dateFormatter) + " to " + newEndDate.format(timeFormatter) + " overlaps another one!");
                }

                Appointment appointment;

                if (!partOfCycle) {
                    Scheme scheme = schemeModel.getSchemeById(schemeId);
                    appointment = new Appointment(newStartDate, newEndDate, client, scheme);
                } else {
                    appointment = new Appointment(newStartDate, newEndDate, client);
                }

                session.save(appointment);

                if (i % batch_size == 0) { //Flushes the hibernate session to prevent running out of memory
                    session.flush();
                    session.clear();
                }
            }
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }


    private boolean timelineClear(LocalDateTime localDateTimeStart, LocalDateTime localDateTimeEnd) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Query query = session.createQuery("select count(*) from Appointment as app where (" +
                "(app.startDate <=:newStartDate AND app.endDate >=:newStartDate) OR " +
                "(app.startDate >=:newStartDate AND app.endDate <=:newEndDate) OR " +
                "(app.startDate <=:newEndDate AND app.endDate >=:newEndDate)" +
                ") AND app.client.coachEmail =:userEmail AND app.isCancelled = false");

        query.setParameter("newStartDate", localDateTimeStart);
        query.setParameter("newEndDate", localDateTimeEnd);
        query.setParameter("userEmail", SecurityContextHolder.getContext().getAuthentication().getName());

        long count = (long) query.uniqueResult();

        session.getTransaction().commit();
        session.close();

        return count == 0;
    }

    public Appointment getAppointment(int id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Session session = sessionFactory.openSession();
        Appointment appointment;


        session.beginTransaction();
        Query query = session.createQuery("from Appointment as app where app.id =:id AND app.client.coachEmail =:email");
        query.setParameter("email", auth.getName());
        query.setParameter("id", id);
        appointment = (Appointment) query.uniqueResult();

        session.getTransaction().commit();
        session.close();

        if (appointment == null)
            throw new NullPointerException("Appointment not found!");

        return appointment;
    }

    public void setCancelledValue(boolean value, int appointmentID) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        try {
            Query query = session.createQuery("from Appointment where id =:appointmentID");
            query.setParameter("appointmentID", appointmentID);

            Appointment appointment = (Appointment) query.uniqueResult();

            if (appointment == null)
                throw new NullPointerException("Appointment not found!");
            else
                appointment.setIsCancelled(value);
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }
}
