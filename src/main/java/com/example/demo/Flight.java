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

    @Column(name = "duration_hours")
    private int durationHours;

    @Column(name = "baseprice")
    private double baseprice;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDepartureAirportId() {
        return departureAirportId;
    }

    public void setDepartureAirportId(long departureAirportId) {
        this.departureAirportId = departureAirportId;
    }

    public long getArrivalAirportId() {
        return arrivalAirportId;
    }

    public void setArrivalAirportId(long arrivalAirportId) {
        this.arrivalAirportId = arrivalAirportId;
    }

    public int getDurationHours() {
        return durationHours;
    }

    public void setDurationHours(int durationHours) {
        this.durationHours = durationHours;
    }

    public double getBaseprice() {
        return baseprice;
    }

    public void setBaseprice(double baseprice) {
        this.baseprice = baseprice;
    }
}

