package com.workOUTcoach.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @NotBlank
    @Column
    private String name;

    @NotBlank
    @Column
    private String surname;

    @NotBlank
    @Column
    private String coachEmail;

    @Column
    private String gymName;

    @Column
    private String goal;

    @Column
    private String healthCondition;

    @Column
    private boolean isActive;

    @Column
    private String phoneNumber;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "client", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Appointment> appointmentList = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "client", orphanRemoval = true, fetch = FetchType.EAGER)
    private Cycle cycle;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "client", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Payment> paymentList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "client", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Scheme> schemeList = new ArrayList<>();

    public Client() {
    }

    public Client(int id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }

    public Client(@NotBlank String name, @NotBlank String surname, @NotBlank String coachEmail) {
        this.name = name;
        this.surname = surname;
        this.coachEmail = coachEmail;
    }

    public Client(@NotBlank String name, @NotBlank String surname, @NotBlank String coachEmail, String gymName, String goal, String healthCondition, boolean isActive, String phoneNumber) {
        this.name = name;
        this.surname = surname;
        this.coachEmail = coachEmail;
        this.gymName = gymName;
        this.goal = goal;
        this.healthCondition = healthCondition;
        this.isActive = isActive;
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getCoachEmail() {
        return coachEmail;
    }

    public void setCoachEmail(String coachEmail) {
        this.coachEmail = coachEmail;
    }

    public String getGymName() {
        return gymName;
    }

    public void setGymName(String gymName) {
        this.gymName = gymName;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getHealthCondition() {
        return healthCondition;
    }

    public void setHealthCondition(String healthCondition) {
        this.healthCondition = healthCondition;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Appointment> getAppointmentList() {
        return appointmentList;
    }

    public void setAppointmentList(List<Appointment> appointmentList) {
        this.appointmentList = appointmentList;
    }

    public Cycle getCycle() {
        return cycle;
    }

    public void setCycle(Cycle cycle) {
        this.cycle = cycle;
    }

    public List<Payment> getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(List<Payment> paymentList) {
        this.paymentList = paymentList;
    }

    public List<Scheme> getSchemeList() {
        return schemeList;
    }

    public void setSchemeList(List<Scheme> schemeList) {
        this.schemeList = schemeList;
    }
}
