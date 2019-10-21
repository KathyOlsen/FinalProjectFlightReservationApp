package com.example.demo;

import javax.persistence.*;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "flight_class", nullable = false)
    private String flightClass;

    @Column(name = "number_passengers", nullable = false)
    private int numberPassengers;

    @Column(name = "price")
    private double price;
//    Price is one-way price for one passenger; it is a calculated field
//          based on flight price and passenger class.
//    It may be better to eliminate this field and make it a temp field in
//          the home controller section for processing search results.

    @ManyToOne
    @JoinColumn (name = "user_id")
    private User user;
//    matching language in User.java should be:
//        @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
//        public Set<Reservation> reservations;

    @ManyToOne
    @JoinColumn (name = "flight_id")
    private Flight flight;
//    matching language in Flight.java should be:
//        @OneToMany(mappedBy = "flight", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
//        public Set<Reservation> reservations;

    @ManyToOne
    @JoinColumn (name = "trip_id")
    private Trip trip;

//    should there be a join for passengers? I'm thinking no b/c will join passengers with trip.

    public Reservation() {
    }

    public Reservation(String flightClass, int numberPassengers, double price) {
        this.flightClass = flightClass;
        this.numberPassengers = numberPassengers;
        this.price = price;
    }


    public Reservation(String flightClass, int numberPassengers, double price, User user, Flight flight, Trip trip) {
        this.flightClass = flightClass;
        this.numberPassengers = numberPassengers;
        this.price = price;
        this.user = user;
        this.flight = flight;
        this.trip = trip;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFlightClass() {
        return flightClass;
    }

    public void setFlightClass(String flightClass) {
        this.flightClass = flightClass;
    }

    public int getNumberPassengers() {
        return numberPassengers;
    }

    public void setNumberPassengers(int numberPassengers) {
        this.numberPassengers = numberPassengers;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }
}
