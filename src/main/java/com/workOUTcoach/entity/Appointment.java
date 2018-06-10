package com.workOUTcoach.entity;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column
    private LocalDateTime startDate;

    @Column
    private LocalDateTime endDate;

    @Column
    private boolean isCancelled;

    @OneToOne(mappedBy = "appointment", orphanRemoval = true, fetch = FetchType.EAGER)
    @Cascade(value = {org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE})
    @Fetch(FetchMode.SELECT)
    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "clientID")
    private Client client;

    @OneToOne
    @JoinColumn(name = "schemeID")
    private Scheme scheme;

    public Appointment() {
    }

    public Appointment(LocalDateTime startDate, LocalDateTime endDate, Client client) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.client = client;
    }

    public Appointment(LocalDateTime startDate, LocalDateTime endDate, Client client, Scheme scheme) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.client = client;
        this.scheme = scheme;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public boolean getIsCancelled() {
        return isCancelled;
    }

    public void setIsCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Scheme getScheme() {
        return scheme;
    }

    public void setScheme(Scheme scheme) {
        this.scheme = scheme;
    }
}
