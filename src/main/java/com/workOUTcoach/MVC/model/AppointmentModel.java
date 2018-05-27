package com.workOUTcoach.MVC.model;

import com.workOUTcoach.entity.Appointment;
import com.workOUTcoach.entity.Client;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public class AppointmentModel {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private ClientModel clientModel;


    public void setAppointment(int id, LocalDate date, boolean repeat, boolean scheme) {
        Client client = clientModel.getClientById(id);
        Appointment appointment;

        if (repeat)
            appointment = new Appointment(date, client);
        else
            appointment = new Appointment(date, date, client);

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.save(appointment);

        session.getTransaction().commit();
        session.close();
    }

    private void verifyTimeline() {

    }
}
