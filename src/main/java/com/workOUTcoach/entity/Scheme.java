package com.workOUTcoach.entity;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "schemes")
public class Scheme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column
    private String title;

    @Column
    private int sequence;

    @ManyToOne
    @JoinColumn(name = "cycleID")
    private Cycle cycle;

    @OneToMany(mappedBy = "scheme", orphanRemoval = true, fetch = FetchType.EAGER)
    @Cascade(value = {org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE})
    @Fetch(FetchMode.SELECT)
    private List<Exercise> exerciseList;

    @OneToOne
    private Appointment appointment;

    public Scheme(String title, int sequence) {
        this.title = title;
        this.sequence = sequence;
    }

    public Scheme(String title, Cycle cycle, int sequence) {
        this.title = title;
        this.cycle = cycle;
        this.sequence = sequence;
    }

    public Scheme() {
    }

    public Scheme(String title, Cycle cycle) {
        this.title = title;
        this.cycle = cycle;
        this.sequence = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public Cycle getCycle() {
        return cycle;
    }

    public void setCycle(Cycle cycle) {
        this.cycle = cycle;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public List<Exercise> getExerciseList() {
        return exerciseList;
    }

    public void setExerciseList(List<Exercise> exerciseList) {
        this.exerciseList = exerciseList;
    }
}
