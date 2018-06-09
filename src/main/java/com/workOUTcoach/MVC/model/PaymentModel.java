package com.workOUTcoach.MVC.model;

import com.workOUTcoach.entity.Payment;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PaymentModel {

    @Autowired
    private SessionFactory sessionFactory;

    public void setNewPayment(Payment payment){
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        try{
            session.save(payment);
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    public List<Payment> getPaymentsByUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Session session = sessionFactory.openSession();

        try{
            session.beginTransaction();
            Query query = session.createQuery("select p from Payment as p left join p.appointment as a left join a.client as c where c.coachEmail=:email");
            query.setParameter("email", auth.getName());

            return query.list();
        }finally {
            session.getTransaction().commit();
            session.close();
        }
    }
}
