package com.workOUTcoach.MVC.model;

import com.workOUTcoach.entity.Appointment;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class TimetableModel {
    @Autowired
    private SessionFactory sessionFactory;


    @SuppressWarnings("unchecked")
    public List<Appointment> listAppointments(int offset) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Session session = sessionFactory.openSession();

        try {
            LocalDateTime bDate=this.setBeginingDate(offset);
            LocalDateTime eDate = this.setEndingDate(offset);
            session.beginTransaction();
            Query query = session.createQuery("from Appointment as app where app.client.coachEmail =:userEmail AND app.startDate >=:begginingDate AND app.endDate <=:endingDate order by app.endDate");
            query.setParameter("userEmail", auth.getName());
            query.setParameter("begginingDate", bDate);
            query.setParameter("endingDate", eDate);


            return query.list();
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    public LocalDateTime setBeginingDate(int offset){
        return LocalDateTime.now().plusWeeks(offset);
    }
    public LocalDateTime setEndingDate(int offset){
        return LocalDateTime.now().plusWeeks(offset+1);
    }

}
