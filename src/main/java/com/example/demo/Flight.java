package com.example.demo;

import javax.persistence.*;

@Entity
@Table(name = "flight")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "number", nullable = false)
    private String number;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToOne(mappedBy = "airport")
    @JoinColumn(name = "id")
    @Column(name = "departure_airport_id", nullable = false)
    private long departureAirportId;

    @OneToOne(mappedBy = "airport")
    @JoinColumn(name = "id")
    @Column(name = "arrival_airport_id", nullable = false)
    private long arrivalAirportId;

/*
departdatetime
arrivaldatetime
duration
baseprice
 */
}

