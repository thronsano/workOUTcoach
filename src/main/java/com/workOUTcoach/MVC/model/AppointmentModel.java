package com.workOUTcoach.MVC.model;

import com.workOUTcoach.entity.Appointment;
import com.workOUTcoach.entity.Client;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Repository
public class AppointmentModel {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private ClientModel clientModel;

    @Autowired
    Environment env;

    public void setAppointment(int id, LocalDateTime startDate, LocalDateTime endDate, boolean cyclic, boolean scheme, int repeatAmount) throws Exception {
        if (endDate.isBefore(startDate))
            throw new Exception("Appointment ends before it starts!");

        if (cyclic && repeatAmount <= 0)
            throw new Exception("Incorrect repetition amount!");

        int batch_size = Integer.parseInt(env.getProperty("hibernate.jdbc.batch_size"));
        Client client = clientModel.getClientById(id);
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        for (int i = 0; i < repeatAmount; i++) {
            LocalDateTime newStartDate = startDate.plusWeeks(i);
            LocalDateTime newEndDate = endDate.plusWeeks(i);

            if (!timelineClear(newStartDate, newEndDate)) {
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                throw new Exception("Appointment from " + newStartDate.format(dateFormatter) + " to " + newEndDate.format(timeFormatter) + " overlaps another one!");
            }

            Appointment appointment = new Appointment(newStartDate, newEndDate, client);
            session.save(appointment);

            if (i % batch_size == 0) { //Flushes the hibernate session to prevent running out of memory
                session.flush();
                session.clear();
            }
        }

        session.getTransaction().commit();
        session.close();
    }

    private boolean timelineClear(LocalDateTime localDateTimeStart, LocalDateTime localDateTimeEnd) {
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
