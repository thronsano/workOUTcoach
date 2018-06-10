package com.workOUTcoach.MVC.model;

import com.workOUTcoach.entity.Appointment;
import com.workOUTcoach.utility.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Repository
public class TimetableModel {
    @Autowired
    private SessionFactory sessionFactory;


    @SuppressWarnings("unchecked")
    public List<Appointment> listAppointments(int offset, boolean showCancelled) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Session session = sessionFactory.openSession();

        try {
            LocalDateTime bDate = this.setBeginningDate(offset);
            LocalDateTime eDate = this.setEndingDate(offset);
            session.beginTransaction();
            Query query = session.createQuery("from Appointment as app where app.client.coachEmail =:userEmail AND app.startDate >=:beginningDate AND app.endDate <=:endingDate " + (!showCancelled ? "AND app.isCancelled = false " : "") + "order by app.endDate");
            query.setParameter("userEmail", auth.getName());
            query.setParameter("beginningDate", bDate);
            query.setParameter("endingDate", eDate);

            return query.list();
        } catch (Exception ex) {
            Logger.logError(ex.getMessage());
            return Collections.emptyList();
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    public LocalDateTime setBeginningDate(int offset) {
        return LocalDateTime.now().plusWeeks(offset);
    }

    public LocalDateTime setEndingDate(int offset) {
        return LocalDateTime.now().plusWeeks(offset + 1);
    }

}
