package com.workOUTcoach.MVC.model;

import com.workOUTcoach.entity.Payment;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class PaymentModel {

    @Autowired
    private SessionFactory sessionFactory;

    public List<Payment> getPaymentsByUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            Query query = session.createQuery("from Payment as pay where pay.appointment.client.coachEmail=:email");
            query.setParameter("email", auth.getName());

            return query.list();
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    public void editIsPaid(int paymentID) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            Payment payment = session.get(Payment.class, paymentID);

            if (payment.getIsPaid()) {
                payment.setIsPaid(false);
                payment.setPaymentDate(null);
            } else {
                payment.setIsPaid(true);
                payment.setPaymentDate(LocalDate.now());
            }
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }
}
