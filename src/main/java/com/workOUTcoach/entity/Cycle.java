package com.workOUTcoach.entity;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "cycles")
public class Cycle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column
    private String title;

    @OneToMany(mappedBy = "cycle", orphanRemoval = true, fetch = FetchType.EAGER)
    @Cascade(value = {org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE})
    @Fetch(FetchMode.SELECT)
    private List<Scheme> schemeList;

    @OneToOne
    @JoinColumn(name = "clientID")
    private Client client;

    public Cycle(String title) {
        this.title = title;
    }

    public Cycle(Client client) {
        this.client = client;
    }

    public Cycle(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public Cycle(String title, Client client) {
        this.title = title;
        this.client = client;
    }

    public Cycle() {
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

    public List<Scheme> getSchemeList() {
        return schemeList;
    }

    public void setSchemeList(List<Scheme> schemeList) {
        this.schemeList = schemeList;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
