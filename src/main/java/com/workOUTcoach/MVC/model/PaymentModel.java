package com.workOUTcoach.MVC.model;

import com.workOUTcoach.entity.Payment;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
}
