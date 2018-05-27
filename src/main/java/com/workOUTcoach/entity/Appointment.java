package com.workOUTcoach.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column
    private LocalDate startDate;

    @Column
    private LocalDate endDate;

    @OneToOne(mappedBy = "appointment", orphanRemoval = true, fetch = FetchType.EAGER)
    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "clientID")
    private Client client;

    @OneToOne
    @JoinColumn(name = "schemeID")
    private Scheme scheme;


    public Appointment() {
    }

    public Appointment(LocalDate startDate, Client client) {
        this.startDate = startDate;
        this.endDate = LocalDate.of(9999, 12, 31);
        this.client = client;
    }

    public Appointment(LocalDate startDate, LocalDate endDate, Client client) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.client = client;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
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
