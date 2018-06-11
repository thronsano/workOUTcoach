package com.workOUTcoach.MVC.model;

import com.workOUTcoach.entity.Appointment;
import com.workOUTcoach.entity.Client;
import com.workOUTcoach.entity.Payment;
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
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
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
    private PaymentModel paymentModel;

    @Autowired
    private UserModel userModel;

    @Autowired
    Environment env;

    public void setAppointment(int clientId, LocalDateTime startDate, LocalDateTime endDate, boolean cyclic, int repeatAmount, boolean partOfCycle, int schemeId) throws Exception {
        if (endDate.isBefore(startDate))
            throw new Exception("Appointment ends before it starts!");

        if (cyclic && repeatAmount <= 0)
            throw new Exception("Incorrect repetition amount!");

        if (!partOfCycle && schemeId == -1)
            throw new Exception("Scheme hasn't been chosen!");

        int batch_size = Integer.parseInt(env.getProperty("hibernate.jdbc.batch_size"));
        Client client = clientModel.getClientById(clientId);

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        List<Payment> payments = new LinkedList<>();

        try {
            float amount = calculateAmount(startDate, endDate);

            for (int i = 0; i < repeatAmount; i++) {
                LocalDateTime newStartDate = startDate.plusWeeks(i);
                LocalDateTime newEndDate = endDate.plusWeeks(i);

                if (!timelineClear(newStartDate, newEndDate, -1)) {
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

                Payment payment = new Payment(appointment, amount);

                appointment.setPayment(payment);
                session.save(appointment);

                payments.add(payment);

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

    public void updateAppointment(int appointmentId, LocalDateTime startDate, LocalDateTime endDate, int schemeId) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Appointment appointment;

        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            Query query = session.createQuery("from Appointment as app where app.id =:id AND app.client.coachEmail =:email");
            query.setParameter("email", auth.getName());
            query.setParameter("id", appointmentId);
            appointment = (Appointment) query.uniqueResult();

            if (!appointment.getStartDate().isEqual(startDate) || !appointment.getEndDate().isEqual(endDate)) {
                if (startDate.isBefore(endDate)) {
                    if (timelineClear(startDate, endDate, appointmentId)) {
                        appointment.setStartDate(startDate);
                        appointment.setEndDate(endDate);
                    } else {
                        throw new Exception("Appointment overlaps another one!");
                    }
                } else {
                    throw new Exception("Appointment ends before it starts!");
                }
            }

            if (appointment.getScheme().getId() != schemeId)
                appointment.setScheme(schemeModel.getSchemeById(schemeId));

        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    private boolean timelineClear(LocalDateTime localDateTimeStart, LocalDateTime localDateTimeEnd, int excludedAppointment) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        long count;

        try {
            Query query = session.createQuery("select count(*) from Appointment as app where (" +
                    "(app.startDate <=:newStartDate AND app.endDate >=:newStartDate) OR " +
                    "(app.startDate >=:newStartDate AND app.endDate <=:newEndDate) OR " +
                    "(app.startDate <=:newEndDate AND app.endDate >=:newEndDate)" +
                    ") AND app.client.coachEmail =:userEmail AND app.isCancelled = false" +
                    (excludedAppointment != -1 ? " AND app.id !=:excludedApp" : ""));

            query.setParameter("newStartDate", localDateTimeStart);
            query.setParameter("newEndDate", localDateTimeEnd);
            query.setParameter("userEmail", SecurityContextHolder.getContext().getAuthentication().getName());

            if (excludedAppointment != -1)
                query.setParameter("excludedApp", excludedAppointment);

            count = (long) query.uniqueResult();
        } finally {
            session.getTransaction().commit();
            session.close();
        }

        return count == 0;
    }

    public Appointment getAppointment(int id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        try {
            Query query = session.createQuery("from Appointment as app where app.id =:id AND app.client.coachEmail =:email");
            query.setParameter("email", auth.getName());
            query.setParameter("id", id);
            return (Appointment) query.uniqueResult();
        } finally {
            session.getTransaction().commit();
            session.close();
        }
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

    public void deleteAppointment(int appointmentID) throws Exception {
        Appointment appointment = getAppointment(appointmentID);
        if (appointment == null)
            throw new NullPointerException("Appointment not found!");

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            session.delete(appointment);
        } catch (Exception ex) {
            throw new Exception("Exception during deleting appointment from database");
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    public LocalDateTime setBegginingDate(int offset) {
        return LocalDateTime.now().plusWeeks(offset);
    }

    public LocalDateTime setEndingDate(int offset) {
        return LocalDateTime.now().plusWeeks(offset + 1);
    }

    private float calculateAmount(LocalDateTime start, LocalDateTime end) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        float hourlyRate = userModel.getUserByEmail(auth.getName()).getHourlyRate();
        float duration = (float) start.until(end, ChronoUnit.MINUTES) / 60;
        return duration * hourlyRate;
    }
}
