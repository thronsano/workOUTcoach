package com.workOUTcoach.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column
    private LocalDate paymentDate;

    @Column
    private boolean isPaid;

    @Column
    private float amount;

    @OneToOne
    @JoinColumn(name = "appointmentID")
    private Appointment appointment;

    @ManyToOne
    @JoinColumn(name = "clientID")
    private Client client;

    public Payment(@NotBlank LocalDate paymentDate, boolean isPaid, float amount) {
        this.paymentDate = paymentDate;
        this.isPaid = isPaid;
        this.amount = amount;
    }

    public Payment() {}

    public Payment(Appointment appointment, float amount){
        this.appointment = appointment;
        this.amount = amount;
        this.isPaid = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
