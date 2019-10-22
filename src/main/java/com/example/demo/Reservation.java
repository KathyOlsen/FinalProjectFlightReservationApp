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

    @Column (name = "round_trip", nullable = false)
    private boolean isRoundTrip;

    @Column (name = "departure_date", nullable = false)
    private Date departureDate;

    @Column (name = "return_date", nullable = false)
    private Date returnDate;

    @Column(name = "flight_class", nullable = false)
    private String flightClass;

    @Column(name = "number_passengers", nullable = false)
    private int numberPassengers;

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
    public Reservation() {
    }

    public Reservation(boolean isRoundTrip,
                       Date departureDate,
                       Date returnDate,
                       String flightClass,
                       int numberPassengers,
                       User user,
                       Flight departureFlight,
                       Flight arrivalFlight,
                       Set<Passenger> passengers) {
        this.isRoundTrip = isRoundTrip;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.flightClass = flightClass;
        this.numberPassengers = numberPassengers;
        this.user = user;
        this.departureFlight = departureFlight;
        this.arrivalFlight = arrivalFlight;
        this.passengers = passengers;
    }

    public boolean isRoundTrip() {
        return isRoundTrip;
    }

    public void setRoundTrip(boolean roundTrip) {
        isRoundTrip = roundTrip;
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
