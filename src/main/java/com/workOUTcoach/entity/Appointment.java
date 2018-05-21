package com.workOUTcoach.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Date;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @NotBlank
    @Column
    private Date startDate;

    @NotBlank
    @Column
    private Date endDate;

    @NotBlank
    @Column
    private int clientID;

    @Column
    private int schemeID;

    public Appointment() {}

    public Appointment(@NotBlank Date startDate, @NotBlank Date endDate, @NotBlank int clientID) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.clientID = clientID;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public Date getStartDate() { return startDate; }

    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }

    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public int getCoachEmail() { return clientID; }

    public void setCoachEmail(int clientID) { this.clientID = clientID; }

    public int getSchemeID() { return schemeID; }

    public void setSchemeID(int schemeID) { this.schemeID = schemeID; }
}
