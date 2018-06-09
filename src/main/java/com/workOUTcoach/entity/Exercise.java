package com.workOUTcoach.entity;

import javax.persistence.*;

@Entity
@Table(name = "exercises")
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column
    private String name;

    @Column
    private int repetitions;

    @ManyToOne
    @JoinColumn(name = "schemeID")
    private Cycle scheme;
}
