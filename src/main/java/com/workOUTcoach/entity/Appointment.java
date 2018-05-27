package com.workOUTcoach.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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

    public Appointment(LocalDateTime startDate, Client client) {
        this.startDate = startDate;
        this.endDate = LocalDateTime.of(LocalDate.of(9999, 12, 31), LocalTime.of(23, 59));
        this.client = client;
    }

    public Appointment(LocalDateTime startDate, LocalDateTime endDate, Client client) {
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
