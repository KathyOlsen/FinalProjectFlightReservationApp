package com.example.demo;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column (name = "departure_date", nullable = false)
    private Date departureDate;

    @Column (name = "return_date", nullable = false)
    private Date returnDate;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn (name = "user_id")
    private User user;
//    matching language in User.java should be:
//        @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
//        public Set<Reservation> reservations;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn (name = "flight_id")
    private Flight departureFlight;
//    matching language in Flight.java should be:
//        @OneToMany(mappedBy = "flight", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
//        public Set<Reservation> reservations;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn (name = "flight_id")
    private Flight arrivalFlight;
//    matching language in Flight.java should be:
//        @OneToMany(mappedBy = "flight", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
//        public Set<Reservation> reservations;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    public Set<Passenger> passengers;
//    matching language in Passenger.java should be:
//          @ManyToOne(fetch = FetchType.EAGER)
//          @JoinColumn (name = "passenger_id")
//          private Passenger passenger;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn (name = "trip_id")
//    private Trip trip;

    public Reservation() {
    }

    public Reservation(Date departureDate,
                       Date returnDate,
                       String flightClass,
                       int numberPassengers,
                       double price,
                       User user,
                       Flight departureFlight,
                       Flight arrivalFlight) {
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.flightClass = flightClass;
        this.numberPassengers = numberPassengers;
        this.price = price;
        this.user = user;
        this.departureFlight = departureFlight;
        this.arrivalFlight = arrivalFlight;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
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

    public Flight getDepartureFlight() {
        return departureFlight;
    }

    public void setDepartureFlight(Flight departureFlight) {
        this.departureFlight = departureFlight;
    }

    public Flight getArrivalFlight() {
        return arrivalFlight;
    }

    public void setArrivalFlight(Flight arrivalFlight) {
        this.arrivalFlight = arrivalFlight;
    }

    public Set<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(Set<Passenger> passengers) {
        this.passengers = passengers;
    }
}
